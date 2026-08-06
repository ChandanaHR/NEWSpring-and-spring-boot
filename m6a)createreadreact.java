package spring_springboot.Myfirstproject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/students2")
@CrossOrigin(origins = "http://localhost:5173")
public class Reactcontroller {
	List<Student2> studentList = new ArrayList<>();
	
	@GetMapping
	public List<Student2> getallstudents() {
		return studentList;
	}
	@PostMapping
	public String addStudent(@RequestBody Student2 student) {
		System.out.println("Id : " + student.getId());
	    System.out.println("Name : " + student.getName());
	    System.out.println("Course : " + student.getCourse());
		studentList.add(student);
		return "Student details added successfully";
	}
}

Student2.java
  package spring_springboot.Myfirstproject;

public class Student2 {
	private int id;
	private String name;
	private String course;
	
	public Student2() {
		
	}

	public Student2(int id, String name, String course) {
		super();
		this.id = id;
		this.name = name;
		this.course = course;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	
	
	
}
//React codeimport { useState, useEffect } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'

function App() {
  const [students, setStudents] = useState([]);
  const [id, setId] = useState("");
  const [name, setName] = useState("");
  const [course, setCourse] = useState("");
  const baseUrl = "http://localhost:8080/students2";

  const fetchStudents = () => {
    fetch(baseUrl)
      .then(res => res.json())
      .then(data => setStudents(data));
    // .then(res => res.text())
    // .then(data => {
    // console.log(data);
    // fetchStudents();
  };

  useEffect(()=>{
    fetchStudents();
  },[])

  const addStudent = () => {
    fetch(baseUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ id:Number(id), name, course })
    })
    .then(res => res.text())
    .then(data => {
      console.log(data);
      fetchStudents();

      setId("");
      setName("");
      setCourse("");
    });
    // .then(() => fetchStudents());
  };
  return (
    <div>
       <h2>Student CRUD</h2>

      <input value={id} placeholder="Id" onChange={e => setId(e.target.value)} />
      <input value={name} placeholder="Name" onChange={e => setName(e.target.value)} />
      <input value={course} placeholder="Course" onChange={e => setCourse(e.target.value)} />
      <button onClick={addStudent}>Add</button>
      <ul>
        {students.map(s => (
          <li key={s.id}>
            {s.id} - {s.name} - {s.course}
            {/* <button onClick={() => deleteStudent(s.id)}>Delete</button> */}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default App

