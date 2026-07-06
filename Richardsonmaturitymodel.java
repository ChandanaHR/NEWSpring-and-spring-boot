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

//Level1
Separate resources (URLs)
/students
/teachers
 Only one HTTP method (POST) is used.
The server decides the operation using the "action" field.
No GET, PUT, or DELETE.

    //Student.java
    package com.example.level1.model;

public class Student {

    private int id;
    private String name;
    private String course;

    public Student() {
    }

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

//Teacher.java
package com.example.level1.model;

public class Teacher {

    private int id;
    private String name;
    private String subject;

    public Teacher() {
    }

    public Teacher(int id, String name, String subject) {
        this.id = id;
        this.name = name;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getSubject(){
        return subject;
    }

    public void setSubject(String subject){
        this.subject=subject;
    }
}

StudentRequest.java
    package com.example.level1.model;

public class StudentRequest {

    private String action;
    private Integer id;
    private String name;
    private String course;

    public StudentRequest() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id){
        this.id=id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getCourse(){
        return course;
    }

    public void setCourse(String course){
        this.course=course;
    }
}

//TeacherRequest.java
package com.example.level1.model;

public class TeacherRequest {

    private String action;
    private Integer id;
    private String name;
    private String subject;

    public TeacherRequest() {
    }

    public String getAction(){
        return action;
    }

    public void setAction(String action){
        this.action=action;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id=id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getSubject(){
        return subject;
    }

    public void setSubject(String subject){
        this.subject=subject;
    }
}

//StudentService.java
package com.example.level1.service;

import com.example.level1.model.Student;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {

    Map<Integer, Student> database = new HashMap<>();

    public String add(Student student) {
        database.put(student.getId(), student);
        return "Student Added Successfully";
    }

    public Student get(int id) {
        return database.get(id);
    }

    public String update(Student student) {
        database.put(student.getId(), student);
        return "Student Updated Successfully";
    }

    public String delete(int id) {
        database.remove(id);
        return "Student Deleted Successfully";
    }
}

//TeacherService.java
package com.example.level1.service;

import com.example.level1.model.Teacher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TeacherService {

    Map<Integer, Teacher> database = new HashMap<>();

    public String add(Teacher teacher) {
        database.put(teacher.getId(), teacher);
        return "Teacher Added Successfully";
    }

    public Teacher get(int id) {
        return database.get(id);
    }

    public String update(Teacher teacher) {
        database.put(teacher.getId(), teacher);
        return "Teacher Updated Successfully";
    }

    public String delete(int id) {
        database.remove(id);
        return "Teacher Deleted Successfully";
    }
}

//StudentController.java
package com.example.level1.controller;

import com.example.level1.model.Student;
import com.example.level1.model.StudentRequest;
import com.example.level1.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {

    @Autowired
    private StudentService service;

    @PostMapping("/students")
    public Object studentOperation(@RequestBody StudentRequest request) {

        switch (request.getAction()) {

            case "add":

                return service.add(
                        new Student(
                                request.getId(),
                                request.getName(),
                                request.getCourse()));

            case "get":

                return service.get(request.getId());

            case "update":

                return service.update(
                        new Student(
                                request.getId(),
                                request.getName(),
                                request.getCourse()));

            case "delete":

                return service.delete(request.getId());

            default:

                return "Invalid Action";
        }

    }
}

//TeacherController.java
package com.example.level1.controller;

import com.example.level1.model.Teacher;
import com.example.level1.model.TeacherRequest;
import com.example.level1.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TeacherController {

    @Autowired
    private TeacherService service;

    @PostMapping("/teachers")
    public Object teacherOperation(@RequestBody TeacherRequest request) {

        switch (request.getAction()) {

            case "add":

                return service.add(
                        new Teacher(
                                request.getId(),
                                request.getName(),
                                request.getSubject()));

            case "get":

                return service.get(request.getId());

            case "update":

                return service.update(
                        new Teacher(
                                request.getId(),
                                request.getName(),
                                request.getSubject()));

            case "delete":

                return service.delete(request.getId());

            default:

                return "Invalid Action";
        }

    }
}

Add Student
POST /students
{
    "action":"add",
    "id":101,
    "name":"Rahul",
    "course":"Java"
}
Get Student
    POST /students
{
    "action":"get",
    "id":101
}
Update Student
POST /students
{
    "action":"update",
    "id":101,
    "name":"Rahul",
    "course":"Spring Boot"
}
Delete Student
POST /students
{
    "action":"delete",
    "id":101
}

Teacher Resource
    Add Teacher
POST /teachers
{
    "action":"add",
    "id":1,
    "name":"Ramesh",
    "subject":"Mathematics"
}

Get Teacher
POST /teachers
{
    "action":"get",
    "id":1
}
