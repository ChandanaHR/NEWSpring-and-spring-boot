//Without Exception handling
//Entity class
package mvc.demo1;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EntityStudent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	private int age;
	
	public EntityStudent() {
		
	}

	public EntityStudent(Integer id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	
}

//Repository class
package mvc.demo1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<EntityStudent,Integer>{
	
}

//Inserting data
// We are sending data from postman
//In postman
http://localhost:8080/insertdata
[
    {
        "name":"chandana",
        "age":25
    },
    {
        "name":"asha",
        "age":34
    },
    {
        "name":"ravi",
        "age":56
    }
]
Controller code
	package mvc.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class Controllerstudent {
	@Autowired
	private StudentRepository repo;
	
	@PostMapping("/insertdata")
	public List<EntityStudent> savestudents(@RequestBody List<EntityStudent> students) {
		return repo.saveAll(students);
	}
}
