//College management system
We'll use a simple College Management System:
Student
Address
Department
Course
StudentProfile

Relationship model:
Student ──────── OneToOne ──────── StudentProfile
Student ──────── ManyToOne ─────── Department
Department ───── OneToMany ─────── Student
Student ──────── ManyToMany ────── Course

  a) @OneToOne
Meaning
One record in Entity A is associated with one record in Entity B.
Example:
Student
   |
   | 1 : 1
   |
StudentProfile
A student has exactly one profile.

Step 1: StudentProfile Entity
package com.example.demo.entity;
import jakarta.persistence.*;
@Entity
@Table(name = "student_profiles")
public class StudentProfile {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String phone;
    private String address;


    // Constructors
    public StudentProfile() {
    }


    public StudentProfile(String phone, String address) {
        this.phone = phone;
        this.address = address;
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }
}

Step 2: Student Entity
package com.example.demo.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "students")
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    @OneToOne
    @JoinColumn(name = "profile_id")
    private StudentProfile profile;


    public Student() {
    }


    public Student(String name, StudentProfile profile) {
        this.name = name;
        this.profile = profile;
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public StudentProfile getProfile() {
        return profile;
    }


    public void setProfile(StudentProfile profile) {
        this.profile = profile;
    }
}
students
+----+---------+------------+
| id | name    | profile_id |
+----+---------+------------+
| 1  | Rahul   | 10         |
| 2  | Priya   | 11         |
+----+---------+------------+

student_profiles
+----+------------+-------------+
| id | phone      | address     |
+----+------------+-------------+
| 10 | 9999999999 | Bangalore   |
| 11 | 8888888888 | Chennai     |
+----+------------+-------------+

  Create repositories
public interface StudentRepository
        extends JpaRepository<Student, Long> {
}
public interface StudentProfileRepository
        extends JpaRepository<StudentProfile, Long> {
}

Service class
  @Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentProfileRepository profileRepository;

    public StudentService(
            StudentRepository studentRepository,
            StudentProfileRepository profileRepository) {

        this.studentRepository = studentRepository;
        this.profileRepository = profileRepository;
    }

    public Student createStudent() {

        // 1. Create profile
        StudentProfile profile =
                new StudentProfile(
                        "9876543210",
                        "Bangalore"
                );

        // 2. Save profile
        StudentProfile savedProfile =
                profileRepository.save(profile);

        // 3. Create student and attach profile
        Student student =
                new Student(
                        "Rahul",
                        savedProfile
                );

        // 4. Save student
        return studentRepository.save(student);
    }
}

Add REST API

Now let's make this accessible through Postman.

@RestController
@RequestMapping("/students")
public class StudentController {


    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping
    public Student createStudent() {
        return studentService.createStudent();
    }
}
Better approach — receive JSON from Postman

In a real application, you would send:

{
    "name": "Rahul",
    "phone": "9876543210",
    "address": "Bangalore"
}

Create a DTO:

public class StudentRequest {


    private String name;
    private String phone;
    private String address;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }
}

Then controller:

@PostMapping
public Student createStudent(
        @RequestBody StudentRequest request) {


    return studentService.createStudent(request);
}

Service:

public Student createStudent(StudentRequest request) {


    StudentProfile profile =
            new StudentProfile(
                    request.getPhone(),
                    request.getAddress()
            );


    StudentProfile savedProfile =
            profileRepository.save(profile);


    Student student =
            new Student(
                    request.getName(),
                    savedProfile
            );


    return studentRepository.save(student);
}

@ManytoOne
  Many Students belong to One Department

                Department
                    │
          ┌─────────┼─────────┐
          │         │         │
          ▼         ▼         ▼
       Student   Student   Student


        Many  ────────>  One
  Suppose we have two tables:

department
----------------
id
name

and:

student
----------------
id
name
department_id

We could have:

Computer Science
       ↑
       │
 ┌─────┼─────┐
 │     │     │
Rahul Priya  Amit
  @ManyToOne
@JoinColumn(name = "department_id")
private Department department;

Create Department Entity

Create:

entity/Department.java
package com.example.demo.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "departments")
public class Department {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    // Default constructor required by JPA
    public Department() {
    }


    public Department(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
Create Student Entity

Now the important part.

package com.example.demo.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "students")
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


    public Student() {
    }


    public Student(String name, Department department) {
        this.name = name;
        this.department = department;
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Department getDepartment() {
        return department;
    }


    public void setDepartment(Department department) {
        this.department = department;
    }
}
Create DepartmentRepository
package com.example.demo.repository;


import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DepartmentRepository
        extends JpaRepository<Department, Long> {
}
Create StudentRepository
package com.example.demo.repository;


import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository
        extends JpaRepository<Student, Long> {
}
Insert Department

Before creating students, we need a department.

We can create it through a service.

package com.example.demo.service;


import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.stereotype.Service;


@Service
public class DepartmentService {


    private final DepartmentRepository departmentRepository;


    public DepartmentService(
            DepartmentRepository departmentRepository) {


        this.departmentRepository = departmentRepository;
    }


    public Department createDepartment(String name) {


        Department department =
                new Department(name);


        return departmentRepository.save(department);
    }
}
Create Department Controller
package com.example.demo.controller;


import com.example.demo.entity.Department;
import com.example.demo.service.DepartmentService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/departments")
public class DepartmentController {


    private final DepartmentService departmentService;


    public DepartmentController(
            DepartmentService departmentService) {


        this.departmentService = departmentService;
    }


    @PostMapping
    public Department createDepartment(
            @RequestParam String name) {


        return departmentService.createDepartment(name);
    }
}
Now send:

POST http://localhost:8080/departments?name=Computer%20Science
Now create a Student

We need the department ID.

For example:

Department ID = 1

Create service:

package com.example.demo.service;


import com.example.demo.entity.Department;
import com.example.demo.entity.Student;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;


@Service
public class StudentService {


    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;


    public StudentService(
            StudentRepository studentRepository,
            DepartmentRepository departmentRepository) {


        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
    }


    public Student createStudent(
            String name,
            Long departmentId) {


        // Find department
        Department department =
                departmentRepository
                        .findById(departmentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Department not found"
                                )
                        );


        // Create student
        Student student =
                new Student();


        student.setName(name);
        student.setDepartment(department);


        // Save student
        return studentRepository.save(student);
    }
}
Student Controller
package com.example.demo.controller;


import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/students")
public class StudentController {


    private final StudentService studentService;


    public StudentController(
            StudentService studentService) {


        this.studentService = studentService;
    }


    @PostMapping
    public Student createStudent(
            @RequestParam String name,
            @RequestParam Long departmentId) {


        return studentService.createStudent(
                name,
                departmentId
        );
    }
}
POST http://localhost:8080/students?name=Rahul&departmentId=1
  
