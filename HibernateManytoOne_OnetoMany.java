//Department entity
package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name="department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int deptId;

    private String deptName;

    public Department() {
    }

    public Department(String deptName) {
        this.deptName = deptName;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}

//Employee entity
package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name="employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empId;

    private String name;

    private double salary;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dept_id")
    private Department department;

    public Employee() {
    }

    public Employee(String name, double salary, Department department) {
        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}

//Main class
package com.example;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import com.example.entity.Department;
import com.example.entity.Employee;

public class Main {

    public static void main(String[] args) {

        SessionFactory factory =
                new Configuration()
                        .configure()
                        .addAnnotatedClass(Employee.class)
                        .addAnnotatedClass(Department.class)
                        .buildSessionFactory();

        Session session = factory.openSession();

        Transaction tx = session.beginTransaction();

        Department department =
                new Department("IT");

        Employee employee =
                new Employee(
                        "Chandana",
                        70000,
                        department
                );
      Employee employee1 =
                new Employee(
                        "Asha",
                        70000,
                        department
                );

        session.persist(employee);
      session.persist(employee1);

        tx.commit();

        session.close();

        factory.close();
    }
}
