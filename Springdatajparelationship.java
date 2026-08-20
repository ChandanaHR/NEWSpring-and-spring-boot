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
  
Postman code for ManyToOne
  Entity classes
Department.java
package com.example.demo.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "departments")
public class Department {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


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

Student.java
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

Repository
DepartmentRepository
package com.example.demo.repository;


import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DepartmentRepository
        extends JpaRepository<Department, Long> {
}
StudentRepository
package com.example.demo.repository;


import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository
        extends JpaRepository<Student, Long> {
}
Department Service

Create:

service/DepartmentService.java
package com.example.demo.service;


import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class DepartmentService {


    private final DepartmentRepository departmentRepository;


    public DepartmentService(
            DepartmentRepository departmentRepository) {


        this.departmentRepository = departmentRepository;
    }


    // CREATE
    public Department createDepartment(
            Department department) {


        return departmentRepository.save(department);
    }


    // GET ALL
    public List<Department> getAllDepartments() {


        return departmentRepository.findAll();
    }


    // GET BY ID
    public Department getDepartmentById(Long id) {


        return departmentRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Department not found with id: " + id
                        )
                );
    }
}
Department Controller
package com.example.demo.controller;


import com.example.demo.entity.Department;
import com.example.demo.service.DepartmentService;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/departments")
public class DepartmentController {


    private final DepartmentService departmentService;


    public DepartmentController(
            DepartmentService departmentService) {


        this.departmentService = departmentService;
    }


    // CREATE DEPARTMENT
    @PostMapping
    public Department createDepartment(
            @RequestBody Department department) {


        return departmentService.createDepartment(department);
    }


    // GET ALL DEPARTMENTS
    @GetMapping
    public List<Department> getAllDepartments() {


        return departmentService.getAllDepartments();
    }


    // GET DEPARTMENT BY ID
    @GetMapping("/{id}")
    public Department getDepartmentById(
            @PathVariable Long id) {


        return departmentService.getDepartmentById(id);
    }
}
Postman
  POST http://localhost:8080/departments
Body → raw → JSON

{
    "name": "Computer Science"
}
Create another Department
  POST http://localhost:8080/departments
Body:

{
    "name": "Mechanical"
}
Student Service

Now the important part.

When creating a student, the client will send:

{
    "name": "Rahul",
    "departmentId": 1
}

We need to find Department 1 and attach it to Rahul.

For this, create a DTO.

StudentRequest.java
package com.example.demo.dto;


public class StudentRequest {


    private String name;


    private Long departmentId;


    public StudentRequest() {
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Long getDepartmentId() {
        return departmentId;
    }


    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
Student Service
package com.example.demo.service;


import com.example.demo.dto.StudentRequest;
import com.example.demo.entity.Department;
import com.example.demo.entity.Student;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;


import java.util.List;


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


    // CREATE STUDENT
    public Student createStudent(
            StudentRequest request) {


        // 1. Find department
        Department department =
                departmentRepository
                        .findById(request.getDepartmentId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Department not found with id: "
                                                + request.getDepartmentId()
                                )
                        );


        // 2. Create Student
        Student student = new Student();


        student.setName(request.getName());


        // 3. Set Department
        student.setDepartment(department);


        // 4. Save Student
        return studentRepository.save(student);
    }


    // GET ALL STUDENTS
    public List<Student> getAllStudents() {


        return studentRepository.findAll();
    }


    // GET STUDENT BY ID
    public Student getStudentById(Long id) {


        return studentRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Student not found with id: " + id
                        )
                );
    }
}
tudent Controller
package com.example.demo.controller;


import com.example.demo.dto.StudentRequest;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController {


    private final StudentService studentService;


    public StudentController(
            StudentService studentService) {


        this.studentService = studentService;
    }


    // CREATE STUDENT
    @PostMapping
    public Student createStudent(
            @RequestBody StudentRequest request) {


        return studentService.createStudent(request);
    }


    // GET ALL STUDENTS
    @GetMapping
    public List<Student> getAllStudents() {


        return studentService.getAllStudents();
    }


    // GET STUDENT BY ID
    @GetMapping("/{id}")
    public Student getStudentById(
            @PathVariable Long id) {


        return studentService.getStudentById(id);
    }
}
Now create Student from Postman

We already created:

Department ID = 1
Department Name = Computer Science

Now use:

POST http://localhost:8080/students

Postman:

Body → raw → JSON

{
    "name": "Rahul",
    "departmentId": 1
}
You should get something similar to:

{
    "id": 1,
    "name": "Rahul",
    "department": {
        "id": 1,
        "name": "Computer Science"
    }
}


NOTE .......................MANYTOMANY................................
  Student entity
  package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    public Student() {
    }

    public Student(String name, String email) {
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
Course entity
  package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    private double fee;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    public Course() {
    }

    public Course(String courseName, double fee) {
        this.courseName = courseName;
        this.fee = fee;
    }

    public Long getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
Understand the Relationship
The important part is:
@ManyToMany
@JoinTable(
    name = "student_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
private Set<Course> courses;
This tells JPA:

Student
   |
   | Many
   |
   +------ Course
              |
              | Many
              |
           Student
A third table will automatically be created:

student_course
------------------------
student_id | course_id
------------------------
1          | 1
1          | 2
2          | 1
2          | 3
This is called the join table.

8. Why mappedBy?
In Course:
@ManyToMany(mappedBy = "courses")
private Set<Student> students;
mappedBy = "courses" means:
The Student entity owns the relationship.
Therefore, we normally add/update the relationship from the Student side.

  Student Repository
package com.example.demo.repository;
import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StudentRepository extends JpaRepository<Student, Long> {
}
10. Course Repository
package com.example.demo.repository;
import com.example.demo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CourseRepository extends JpaRepository<Course, Long> {
}

Student Service
  package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository) {

        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // Add student
    public Student addStudent(Student student) {

        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {

        return studentRepository.findAll();
    }

    // Get student by ID
    public Student getStudentById(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));
    }

    // Enroll student in course
    public Student enrollCourse(Long studentId, Long courseId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));

        student.getCourses().add(course);

        return studentRepository.save(student);
    }
}

Course Service
  package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Add course
    public Course addCourse(Course course) {

        return courseRepository.save(course);
    }

    // Get all courses
    public List<Course> getAllCourses() {

        return courseRepository.findAll();
    }

    // Get course by ID
    public Course getCourseById(Long id) {

        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));
    }
}

StudentController
  package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Add student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {

        return studentService.addStudent(student);
    }

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {

        return studentService.getAllStudents();
    }

    // Get student by ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {

        return studentService.getStudentById(id);
    }

    // Enroll student into course
    @PostMapping("/{studentId}/courses/{courseId}")
    public Student enrollCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        return studentService.enrollCourse(studentId, courseId);
    }
}

Course Controller
  package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Add course
    @PostMapping
    public Course addCourse(@RequestBody Course course) {

        return courseService.addCourse(course);
    }

    // Get all courses
    @GetMapping
    public List<Course> getAllCourses() {

        return courseService.getAllCourses();
    }

    // Get course by ID
    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {

        return courseService.getCourseById(id);
    }
}

Step 1: Add Students
POST
http://localhost:8080/students
Body → raw → JSON
{
    "name": "Rahul",
    "email": "rahul@gmail.com"
}
Step 2: Add Courses
POST
http://localhost:8080/courses
Body:
{
    "courseName": "Java",
    "fee": 15000
}
Step 3: Enroll Rahul in Java
Now we want:
Rahul → Java
Use:
POST
http://localhost:8080/students/1/courses/1
  Enroll Rahul in Python
POST
http://localhost:8080/students/1/courses/2

Step 4: Get Student
GET
http://localhost:8080/students/1

Because both entities reference each other, returning them directly can cause:
Student
   ↓
Course
   ↓
Student
   ↓
Course
   ↓
Student
...
This can cause:
Infinite recursion
and eventually:
StackOverflowError
So for a proper REST API, we should handle JSON serialization.

  Fix Infinite JSON Recursion

One simple approach is:

In Student.java:

@ManyToMany
@JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
)
@JsonManagedReference
private Set<Course> courses = new HashSet<>();

And in Course.java:
@ManyToMany(mappedBy = "courses")
@JsonBackReference
private Set<Student> students = new HashSet<>();

How to Add Multiple Courses at Once
Suppose Rahul wants:
Java
Python
Spring Boot
You could create an endpoint like:

@PostMapping("/{studentId}/courses")
public Student enrollMultipleCourses(
        @PathVariable Long studentId,
        @RequestBody Set<Long> courseIds) {


    return studentService.enrollMultipleCourses(
            studentId,
            courseIds
    );
}
ervice:

public Student enrollMultipleCourses(
        Long studentId,
        Set<Long> courseIds) {


    Student student = studentRepository.findById(studentId)
            .orElseThrow(() ->
                    new RuntimeException("Student not found"));


    for (Long courseId : courseIds) {


        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Course not found: " + courseId));


        student.getCourses().add(course);
    }


    return studentRepository.save(student);
}
POST
http://localhost:8080/students/1/courses

Body:

[
    1,
    2,
    3
]

One Important JPA Concept

Remember this pattern:

@ManyToMany
@JoinTable(...)
private Set<Course> courses;

is the owning side.

And:

@ManyToMany(mappedBy = "courses")
private Set<Student> students;

is the inverse side.
