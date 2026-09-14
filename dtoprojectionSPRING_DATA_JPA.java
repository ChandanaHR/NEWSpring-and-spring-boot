//Employee.java
package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String department;

    private double salary;

    private String phone;

    private String address;

    public Employee() {
    }

    public Employee(String name, String email,
                    String department, double salary,
                    String phone, String address) {

        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.phone = phone;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

// DTO: EmployeeDTO
package com.example.demo.dto;

public class EmployeeDTO {

    private Long id;
    private String name;
    private String department;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Long id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

// Repository: EmployeeRepository
package com.example.demo.repository;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    @Query("""
           SELECT new com.example.demo.dto.EmployeeDTO(
               e.id,
               e.name,
               e.department
           )
           FROM Employee e
           """)
    List<EmployeeDTO> getEmployeeDTOs();
}
JPA should create EmployeeDTO objects from the selected fields.

//Service: EmployeeService
  package com.example.demo.service;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeDTO> getEmployees() {

        return repository.getEmployeeDTOs();
    }
}

//Controller: EmployeeController
package com.example.demo.controller;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/employees")
    public List<EmployeeDTO> getEmployees() {

        return service.getEmployees();
    }
}

Database Data

Suppose the database contains:

id	name	email	department	salary	phone	address
1	John	john@gmail.com	IT	60000	9876543210	Bangalore
2	David	david@gmail.com	HR	50000	9876543211	Chennai
3	Smith	smith@gmail.com	IT	70000	9876543212	Mumbai

  The API response becomes:

[
    {
        "id": 1,
        "name": "John",
        "department": "IT"
    },
    {
        "id": 2,
        "name": "David",
        "department": "HR"
    },
    {
        "id": 3,
        "name": "Smith",
        "department": "IT"
    }
]


// Types of Projection
a) Interface-based projection
  Create an interface:
public interface EmployeeView {
    Long getId();
    String getName();
    String getDepartment();
}
Notice there is no implementation.
Spring Data JPA creates the projection automatically.

  public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {
    List<EmployeeView> findAllBy();
}

Service
@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeView> getEmployees() {

        return repository.findAllBy();
    }
}

Controller
@RestController
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/employees")
    public List<EmployeeView> getEmployees() {

        return service.getEmployees();
    }
}

//Projection with WHERE condition
Suppose we want only IT employees.

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    List<EmployeeView> findByDepartment(String department);
}

Then:
@GetMapping("/employees/{department}")
public List<EmployeeView> getEmployees(
        @PathVariable String department) {

    return service.getEmployeesByDepartment(department);
}
Service:
public List<EmployeeView> getEmployeesByDepartment(String department) {

    return repository.findByDepartment(department);
}
Request:
GET /employees/IT

  // Dynamic projection
  public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    <T> List<T> findByDepartment(
            String department,
            Class<T> type);
}

Add one projection
  public interface EmployeeView {

    Long getId();

    String getName();

    String getDepartment();
}

Add another projection
  public interface EmployeeSalaryView {

    Long getId();

    String getName();

    double getSalary();
}

Now we can dynamically select what we want.
List<EmployeeView> employees =
        repository.findByDepartment(
                "IT",
                EmployeeView.class);
Or:
List<EmployeeSalaryView> employees =
        repository.findByDepartment(
                "IT",
                EmployeeSalaryView.class);

