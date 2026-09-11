p1) Pagination
  Repository

Extend JpaRepository:

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByDepartment(String department, Pageable pageable);

}
Service
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public Page<Employee> getEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

  public Page<Employee> getDepartment(
            String department,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.findByDepartment(
                department,
                pageable
        );
    }
}
Controller
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    public Page<Employee> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return service.getEmployees(page, size);
    }

   @GetMapping
    public Page<Employee> getDepartment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam() String department) {

        return service.getDepartment(page, size,department);
    }
}
GET /employees?page=0&size=5
  GET /employees?department=IT&page=0&size=2

  p2) Useful page methods
  package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String department;

    private double salary;

    public Employee() {
    }

    public Employee(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
Repository
  package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    Page<Employee> findByDepartment(
            String department,
            Pageable pageable
    );
}
Service
  @Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public Page<Employee> getEmployees(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return repository.findAll(pageable);
    }
}
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    public Page<Employee> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return service.getEmployees(page, size);
    }
}


correct code Pagination
  Employee18repository
  package web;

import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Employee18repository extends JpaRepository<Employee, Integer> {
	@Query("""
			Select e from Employee e where e.empdesignation = :empdesignation
			""")
	org.springframework.data.domain.Page<Employee> findByDepartment(@Param("empdesignation") String empdesignation, PageRequest pageable);

}
//Employee.java
package web;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "employee18")
public class Employee {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int eid;
	private String empname;
	private String empdesignation;
	
	public Employee() {
		
	}
	
	public Employee(int eid, String empname, String empdesignation) {
		super();
		this.eid = eid;
		this.empname = empname;
		this.empdesignation = empdesignation;
	}
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public String getEmpname() {
		return empname;
	}
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	public String getEmpdesignation() {
		return empdesignation;
	}
	public void setEmpdesignation(String empdesignation) {
		this.empdesignation = empdesignation;
	}
	
	
}
//Employee18service.java
package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class Employee18service<Pageable> {
	@Autowired
	private Employee18repository employee18repository;
	
	public Page<Employee> getEmployees(int page,int size) {
		PageRequest pageable = PageRequest.of(page, size);
		return employee18repository.findAll(pageable);
	}
	
//	public Page<Employee> getDepartment(String department, int page,int size) {
//		PageRequest pageable = PageRequest.of(page, size);
//		return employee18repository.findByDepartment(department, pageable);
//	}

	public Page<Employee> getDepartment(String empdesignation, int page, int size) {
		// TODO Auto-generated method stub
		PageRequest pageable = PageRequest.of(page, size);
		return employee18repository.findByDepartment(empdesignation, pageable);
	}
	
//	page=0,size=5 offset = page*size=0
//			page=1,size=5, offset=page*size=5
//	>
	
}
//Employee19controller
package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("employeeganesha")
public class Employee19controller {
	@Autowired
	private Employee18service employee18service;  
	
	@GetMapping("/firstpage")
	public Page<Employee> getemployees(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="5") int size) {
		return employee18service.getEmployees(page,size);
	}
	
	@GetMapping("/secondpage")
	public Page<Employee> getDepartment(String empdesignation, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="5") int size) {
		return employee18service.getDepartment(empdesignation,page,size);
	}
}



