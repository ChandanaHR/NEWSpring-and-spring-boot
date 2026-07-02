//Level 0
//Student.java
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

StudentRequest.java-> This class contains action
  package com.example.model;

public class StudentRequest {

    private String action;
    private Integer id;
    private String name;
    private String course;

    public StudentRequest() {}

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}

StudentService.java
  package com.example.service;

import com.example.model.Student;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {

    Map<Integer, Student> database = new HashMap<>();

    public String addStudent(Student student) {

        database.put(student.getId(), student);

        return "Student Added Successfully";
    }

    public Student getStudent(int id) {

        return database.get(id);
    }

    public String updateStudent(Student student) {

        database.put(student.getId(), student);

        return "Student Updated Successfully";
    }

    public String deleteStudent(int id) {

        database.remove(id);

        return "Student Deleted Successfully";
    }
}

StudentController.java
  package com.example.controller;

import com.example.model.Student;
import com.example.model.StudentRequest;
import com.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {

    @Autowired
    StudentService service;

    @PostMapping("/student")
    public Object studentOperation(@RequestBody StudentRequest request) {

        if (request.getAction().equalsIgnoreCase("addStudent")) {

            Student student = new Student(
                    request.getId(),
                    request.getName(),
                    request.getCourse());

            return service.addStudent(student);
        }

        else if (request.getAction().equalsIgnoreCase("getStudent")) {

            return service.getStudent(request.getId());
        }

        else if (request.getAction().equalsIgnoreCase("updateStudent")) {

            Student student = new Student(
                    request.getId(),
                    request.getName(),
                    request.getCourse());

            return service.updateStudent(student);
        }

        else if (request.getAction().equalsIgnoreCase("deleteStudent")) {

            return service.deleteStudent(request.getId());
        }

        return "Invalid Action";
    }

}

API URL : POST http://localhost:8080/student
Request body: {
    "action":"addStudent",
    "id":101,
    "name":"Rahul",
    "course":"Java"
}

Get Student
  Request body: {
    "action":"getStudent",
    "id":101
}

Update Student:
  {
    "action":"updateStudent",
    "id":101,
    "name":"Rahul",
    "course":"Spring Boot"
}
Delete Student: {
    "action":"deleteStudent",
    "id":101
}
