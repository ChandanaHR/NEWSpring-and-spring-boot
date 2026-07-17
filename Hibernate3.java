//Create key class
package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class StudentCourseId implements Serializable {

    private int studentId;

    private int courseId;

    public StudentCourseId() {
    }

    public StudentCourseId(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // getters and setters

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    // equals() and hashCode() should also be implemented
}
Entity
  package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name="student_course")
public class StudentCourse {

    @EmbeddedId
    private StudentCourseId id;

    private String semester;

    public StudentCourse() {
    }

    public StudentCourse(StudentCourseId id, String semester) {
        this.id = id;
        this.semester = semester;
    }

    // getters and setters
}
