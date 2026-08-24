//Student18.java
package web;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="student18")
public class Student18 {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String email;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="student18course18",joinColumns=@JoinColumn(name="student18id"),
	inverseJoinColumns=@JoinColumn(name="course18id"))
	@JsonManagedReference
	private Set<Course18> courses18 = new HashSet<>();
	
	public Student18() {
		
	}

	public Student18(Long id, String name, String email, Set<Course18> courses18) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.courses18 = courses18;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Course18> getCourses18() {
		return courses18;
	}

	public void setCourses18(Set<Course18> courses18) {
		this.courses18 = courses18;
	}
	
	
	
	
}
//Course18.java
package web;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="course18")
public class Course18 {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String coursename;
	
	private double fees;
	
	@ManyToMany(mappedBy = "courses18")
	@JsonBackReference
	private Set<Student18> students18 = new HashSet<>();
	
	public Course18() {
		
	}

	public Course18(Long id, String coursename, double fees, Set<Student18> students18) {
		super();
		this.id = id;
		this.coursename = coursename;
		this.fees = fees;
		this.students18 = students18;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public double getFees() {
		return fees;
	}

	public void setFees(double fees) {
		this.fees = fees;
	}

	public Set<Student18> getStudents18() {
		return students18;
	}

	public void setStudents18(Set<Student18> students18) {
		this.students18 = students18;
	}
	
	
}
//Student18service
package web;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class Student18service {
	private Student18repository student18repository;
	private Course18repository  course18repository;
	
	public Student18service(Student18repository student18repository, Course18repository course18repository) {
		super();
		this.student18repository = student18repository;
		this.course18repository = course18repository;
	}

	public Student18 addstudent(Student18 student18) {
		// TODO Auto-generated method stub
		return student18repository.save(student18);
	}

	public List<Student18> getallstudents() {
		// TODO Auto-generated method stub
		return student18repository.findAll();
	}

	public Student18 enrollservice(Long studentid, Long courseid) {
		// TODO Auto-generated method stub
		Student18 student18 = student18repository.findById(studentid).orElseThrow(()->new RuntimeException("Student with id not found"));
		Course18 course18 = course18repository.findById(courseid).orElseThrow(()->new RuntimeException("Course with id not found"));
		student18.getCourses18().add(course18);
		return student18repository.save(student18);
		
	}

	public Student18 getstudentbyid(Long id) {
		// TODO Auto-generated method stub
		return student18repository.findById(id).orElseThrow(()-> new RuntimeException("Student with id is not found"));
	}
	
	
}
//Student18controller
package web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/student18")
public class Student18controller {
     private Student18service student18service;

	 public Student18controller(Student18service student18service) {
		super();
		this.student18service = student18service;
	 }
     
     @PostMapping
     public Student18 addstudent(@RequestBody Student18 student18) {
    	 	return student18service.addstudent(student18);
     }
     
     @GetMapping("/findall18")
     public List<Student18> getallstudents() {
    	 	return student18service.getallstudents();
     }
     
//     Enroll students into our course
      @PostMapping("/{studentid}/courses18new/{courseid}")
      public Student18 enrollcourse(@PathVariable Long studentid, @PathVariable Long courseid) {
    	  	return student18service.enrollservice(studentid,courseid);
      }
      
      //Get student by their id
      @GetMapping("/{id}")
      public Student18 getstudentbyid(@PathVariable Long id) {
    	  	return student18service.getstudentbyid(id);
      }
}
//Course18service
package web;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class Course18service {
	
	private Course18repository course18repository;
	
	
	public Course18service(Course18repository course18repository) {
		super();
		this.course18repository = course18repository;
	}


	public Course18 addCourse(Course18 course18) {
		// TODO Auto-generated method stub
		return course18repository.save(course18);
	}


	public List<Course18> getallcourses() {
		// TODO Auto-generated method stub
		return course18repository.findAll();
	}

}
//Course18controller
package web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/courses18")
public class Course18controller {
	private Course18service course18service;

	public Course18controller(Course18service course18service) {
		super();
		this.course18service = course18service;
	}
	
	@PostMapping
	public Course18 addCourse(@RequestBody Course18 course18) {
		return course18service.addCourse(course18);
	}
	
	@GetMapping("/findallcourses")
	public List<Course18> getallcourses() {
		return course18service.getallcourses();
	}
}
//Course18repository
package web;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Course18repository extends JpaRepository<Course18, Long>{
	
}
//Student18repository
package web;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Student18repository extends JpaRepository<Student18, Long>{

}

