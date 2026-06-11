//Employeerepo.java
package web;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Employeerepo extends JpaRepository<Employeeentity, Integer>{
	
}
//Employeecontroller.java
package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employeesdetails")
public class Employeecontroller {
	@Autowired
	private Employeeservice service;
	
	@GetMapping("/{id}")
	public Employeedto getEmployeeid(@PathVariable int id) {
		return service.getEmployeeById(id);
	}
	
	@PostMapping
	public Employeedto addEmployee(@RequestBody Employeedto dto) {
		return  service.saveEmployee(dto);
	}
}
//Employeedto.java
package web;

public class Employeedto {
	private int emp_id;
	private String emp_name;
	public Employeedto() {
		
	}
	
	public Employeedto(int emp_id, String emp_name) {
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
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	
	
}
//Employeeserviceinterface.java
package web;

public interface Employeeserviceinterface {
	Employeedto getEmployeeById(int id);
	Employeedto saveEmployee(Employeedto dto);
}
//Employeeservice.java
package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Employeeservice implements Employeeserviceinterface{
	@Autowired
	private Employeerepo repo;

	@Override
	public Employeedto getEmployeeById(int id) {
		// TODO Auto-generated method stub
		Employeeentity e = repo.findById(id).orElseThrow();
		Employeedto dto = new Employeedto();
		dto.setEmp_id(e.getEmp_id());
		dto.setEmp_name(e.getEmp_name());
		return dto;
//				findByid: select * from Employeeentity where emp_id = ?
	}

	@Override
	public Employeedto saveEmployee(Employeedto dto) {
		// TODO Auto-generated method stub
		Employeeentity e = new Employeeentity();
		e.setEmp_name(dto.getEmp_name());
		e.setEmp_id(dto.getEmp_id());
		Employeeentity saved = repo.save(e);
		return dto;
	}
	
	
}
