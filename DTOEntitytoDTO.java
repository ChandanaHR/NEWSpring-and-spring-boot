//Controller
package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/employeesdetails")
public class Employeecontroller {
	
	@Autowired
	private Employeeservice service;
	
	@Autowired
	private Employeerepo repo;
	
	public Employeecontroller(Employeeservice service) {
		super();
		this.service = service;
	}

//	@GetMapping("/{id}")
//	public Employeedto getEmployeeid(@PathVariable int id) {
//		return service.getEmployeeById(id);
//	}
	
	@PostMapping
	public Employeeentity addEmployee(@RequestBody Employeeentity entity) {
		return service.saveEmployee(entity);
	}
	
//	@PostMapping
//	public Employeedto2 addEmployee(@RequestBody Employeedto2 dto) {
//		return  service.saveEmployee(dto);
//	}
	

}
//Employeedto3.java
package web;

public class Employeedto3 {
	private String emp_name;
//	private String emp_email;
//	private String emp_password;
	
	public Employeedto3() {
		
	}

	public Employeedto3(String emp_name, String emp_email, String emp_password) {
		super();
		this.emp_name = emp_name;
//		this.emp_email = emp_email;
//		this.emp_password = emp_password;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

//	public String getEmp_email() {
//		return emp_email;
//	}
//
//	public void setEmp_email(String emp_email) {
//		this.emp_email = emp_email;
//	}
//
//	public String getEmp_password() {
//		return emp_password;
//	}
//
//	public void setEmp_password(String emp_password) {
//		this.emp_password = emp_password;
//	}
	
	

}
//Employeeentity
package web;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employeeentity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer emp_id;
	private String emp_name;
	private String emp_email;
	private String emp_password;
	
    public Employeeentity() {
    	
    }
	
	public Employeeentity(Integer emp_id, String emp_name, String emp_email, String emp_password) {
		super();
		this.emp_id = emp_id;
		this.emp_name = emp_name;
		this.emp_email = emp_email;
		this.emp_password = emp_password;
	}

	public int getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

	public String getEmp_email() {
		return emp_email;
	}

	public void setEmp_email(String emp_email) {
		this.emp_email = emp_email;
	}

	public String getEmp_password() {
		return emp_password;
	}

	public void setEmp_password(String emp_password) {
		this.emp_password = emp_password;
	}
	

}
//Employeeprofile1
package web;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Employeeprofile1 {
	@Id
	private Integer emp_id;
	private String emp_name;
//	private String emp_email;
	
	public Employeeprofile1() {
		
	}

//	public Employeeprofile(int emp_id, String emp_name, String emp_email) {
//		super();
//		this.emp_id = emp_id;
//		this.emp_name = emp_name;
////		this.emp_email = emp_email;
//	}
	
	public Employeeprofile1(Integer emp_id, String emp_name) {
		super();
		this.emp_id = emp_id;
		this.emp_name = emp_name;
	}

	public int getEmp_id() {
		return emp_id;
	}


	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}
	

	public String getEmp_name() {
		return emp_name;
	}

//	public Employeeprofile1(String emp_name) {
//	super();
//	this.emp_name = emp_name;
//}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

//	public String getEmp_email() {
//		return emp_email;
//	}
//
//	public void setEmp_email(String emp_email) {
//		this.emp_email = emp_email;
//	}
	
	

}
//Employeeprofilerepo
package web;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Employeerprofilerepo extends JpaRepository<Employeeprofile1, Integer> {

}
//Employeerepo
package web;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Employeerepo extends JpaRepository<Employeeentity, Integer>{

}
//Employeeservice
package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Employeeservice implements Employeeserviceinterface {
	@Autowired
	private Employeerepo repo;
	private Employeerprofilerepo profilerepo;
	
	public Employeeservice(Employeerepo repo, Employeerprofilerepo profilerepo) {
		super();
		this.repo = repo;
		this.profilerepo = profilerepo;
	}
	
	@Override 
	public Employeedto getEmployeeById(int id) {
		Employeeentity e = repo.findById(id).orElseThrow();
		Employeedto dto = new Employeedto();
		dto.setEmp_id(e.getEmp_id());
		dto.setEmp_name(e.getEmp_name());
		return dto;
	}

	
	@Override
	public Employeeentity saveEmployee(Employeeentity entity) {
//		1: Map DTO to primary Employee Entity
//		Employeeentity e = new Employeeentity();
//		Employeeentity push = repo.save(entity);
		Employeeentity push = new Employeeentity();
		push.setEmp_name(entity.getEmp_name());
		push.setEmp_email(entity.getEmp_email());
		push.setEmp_password(entity.getEmp_password());
		Employeeentity push1 = repo.save(push);
		Integer generatedId = push1.getEmp_id();
		Employeedto3 dtoobj = new Employeedto3();
		dtoobj.setEmp_name(push1.getEmp_name());
//		e.setEmp_name(dto3.getEmp_name());
//		e.setEmp_email(dto3.getEmp_email());
//		e.setEmp_password(dto3.getEmp_password());
		
//		2: Save primary entity to generate auto-incremented ID
//		Employeeentity saved = repo.save(e);
//		Employeedto3 saved = profilerepo.saveAll(dtoobj);
		
//		3: Map DTO subset fields to the second Entity
		Employeeprofile1 p = new Employeeprofile1();
		p.setEmp_id(generatedId);
		p.setEmp_name(dtoobj.getEmp_name());
//		p.setEmp_email(dto3.getEmp_email());
		
//		4: Save Employee Profile
		profilerepo.save(p);
		
		return push;
//		return "Employee records saved successfully in both tables with ID: " + generatedId;
		
	}
	
//	@Autowired
//	private Employeerepo repo;
//
//	@Override
//	public Employeedto getEmployeeById(int id) {
//		// TODO Auto-generated method stub
//		Employeeentity e = repo.findById(id).orElseThrow();
//		Employeedto dto = new Employeedto();
//		dto.setEmp_id(e.getEmp_id());
//		dto.setEmp_name(e.getEmp_name());
//		return dto;
////				findByid: select * from Employeeentity where emp_id = ?
//	}
//
//	@Override
//	public Employeedto2 saveEmployee(Employeedto2 dto) {
//		// TODO Auto-generated method stub
//		Employeeentity e = new Employeeentity();
//		e.setEmp_name(dto.getEmp_name());
//		Employeeentity saved = repo.save(e);
////		dto.setEmp_id(saved.getEmp_id());
//		return dto;
//	}
	

}
//Employeeserviceinterface.java
package web;

public interface Employeeserviceinterface {
	Employeedto getEmployeeById(int id);
//	Employeedto2 saveEmployee(Employeedto2 dto);
	Employeeentity saveEmployee(Employeeentity entity);

}
