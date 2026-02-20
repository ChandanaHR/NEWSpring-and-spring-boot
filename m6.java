import React, { useEffect, useState } from "react";

function App() {

  const [students, setStudents] = useState([]);
  const [id, setId] = useState("");
  const [name, setName] = useState("");
  const [course, setCourse] = useState("");

  const baseUrl = "http://localhost:8080/students";

  // GET ALL
  const fetchStudents = () => {
    fetch(baseUrl)
      .then(res => res.json())
      .then(data => setStudents(data));
  };

  useEffect(() => {
    fetchStudents();
  }, []);

  // POST
  const addStudent = () => {
    fetch(baseUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ id, name, course })
    }).then(() => fetchStudents());
  };

  // PUT
  const updateStudent = () => {
    fetch(`${baseUrl}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, course })
    }).then(() => fetchStudents());
  };

  // DELETE
  const deleteStudent = (id) => {
    fetch(`${baseUrl}/${id}`, {
      method: "DELETE"
    }).then(() => fetchStudents());
  };

  return (
    <div>
      <h2>Student CRUD</h2>

      <input placeholder="Id" onChange={e => setId(e.target.value)} />
      <input placeholder="Name" onChange={e => setName(e.target.value)} />
      <input placeholder="Course" onChange={e => setCourse(e.target.value)} />

      <button onClick={addStudent}>Add</button>
      <button onClick={updateStudent}>Update</button>

      <ul>
        {students.map(s => (
          <li key={s.id}>
            {s.id} - {s.name} - {s.course}
            <button onClick={() => deleteStudent(s.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
}
//Studentcontroller
package com.example.controller;

import com.example.model.Student;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin   // To allow React to call backend
public class StudentController {

    List<Student> studentList = new ArrayList<>();

    // ✅ GET ALL STUDENTS
    @GetMapping
    public List<Student> getAllStudents() {
        return studentList;
    }

    // ✅ GET STUDENT BY ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentList.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // ✅ ADD STUDENT
    @PostMapping
    public String addStudent(@RequestBody Student student) {
        studentList.add(student);
        return "Student Added Successfully";
    }

    // ✅ UPDATE STUDENT
    @PutMapping("/{id}")
    public String updateStudent(@PathVariable int id,
                                @RequestBody Student updatedStudent) {

        for (Student s : studentList) {
            if (s.getId() == id) {
                s.setName(updatedStudent.getName());
                s.setCourse(updatedStudent.getCourse());
                return "Student Updated Successfully";
            }
        }
        return "Student Not Found";
    }

    // ✅ DELETE STUDENT
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id) {
        studentList.removeIf(s -> s.getId() == id);
        return "Student Deleted Successfully";
    }
}
export default App;
//Student model
package com.example.model;

public class Student {

    private int id;
    private String name;
    private String course;

    public Student() {}

    public Student(int id, String name, String course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
}
