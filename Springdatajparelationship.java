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

