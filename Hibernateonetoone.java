//Laptop
package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "laptop")
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int laptopId;

    private String brand;

    private double price;

    public Laptop() {
    }

    public Laptop(String brand, double price) {
        this.brand = brand;
        this.price = price;
    }

    public int getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(int laptopId) {
        this.laptopId = laptopId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
//Employee
package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empId;

    private String name;

    private double salary;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "laptop_id")
    private Laptop laptop;

    public Employee() {
    }

    public Employee(String name, double salary, Laptop laptop) {
        this.name = name;
        this.salary = salary;
        this.laptop = laptop;
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

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }
}
//Main class
package com.example.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.example.entity.Employee;
import com.example.entity.Laptop;

public class App {

    public static void main(String[] args) {

        Configuration configuration = new Configuration();

        configuration.configure();

        configuration.addAnnotatedClass(Employee.class);
        configuration.addAnnotatedClass(Laptop.class);

        SessionFactory sessionFactory =
                configuration.buildSessionFactory();

        Session session =
                sessionFactory.openSession();

        Transaction tx =
                session.beginTransaction();

        Laptop laptop =
                new Laptop("Dell",65000);

        Employee employee =
                new Employee("Chandana",70000,laptop);

        session.persist(employee);

        tx.commit();

        session.close();

        sessionFactory.close();

    }
}
//Bidirectional one to one mapping
//Employee
package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empId;

    private String name;

    private double salary;

    @OneToOne(mappedBy = "employee",
              cascade = CascadeType.ALL,
              fetch = FetchType.LAZY)
    private Laptop laptop;

    public Employee() {
    }

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
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

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }
}
//Laptop
package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "laptop")
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int laptopId;

    private String brand;

    private double price;

    @OneToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    public Laptop() {
    }

    public Laptop(String brand, double price) {
        this.brand = brand;
        this.price = price;
    }

    public int getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(int laptopId) {
        this.laptopId = laptopId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
//Hibernate.cfg.xml
<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE hibernate-configuration PUBLIC
"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">

<hibernate-configuration>

    <session-factory>

        <property name="hibernate.connection.driver_class">
            com.mysql.cj.jdbc.Driver
        </property>

        <property name="hibernate.connection.url">
            jdbc:mysql://localhost:3306/hibernate_demo
        </property>

        <property name="hibernate.connection.username">
            root
        </property>

        <property name="hibernate.connection.password">
            root
        </property>

        <property name="hibernate.dialect">
            org.hibernate.dialect.MySQLDialect
        </property>

        <property name="hibernate.hbm2ddl.auto">
            update
        </property>

        <property name="hibernate.show_sql">
            true
        </property>

        <property name="hibernate.format_sql">
            true
        </property>

        <mapping class="com.example.entity.Employee"/>

        <mapping class="com.example.entity.Laptop"/>

    </session-factory>

</hibernate-configuration>
  //Main.java
  package com.example;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import com.example.entity.Employee;
import com.example.entity.Laptop;

public class Main {

    public static void main(String[] args) {

        Configuration configuration = new Configuration();

        configuration.configure();

        SessionFactory factory =
                configuration.buildSessionFactory();

        Session session =
                factory.openSession();

        Transaction tx =
                session.beginTransaction();

        Employee employee =
                new Employee("Chandana",70000);

        Laptop laptop =
                new Laptop("Dell",65000);

        // Connect both objects
        employee.setLaptop(laptop);

        laptop.setEmployee(employee);

        // Save only Employee
        session.persist(employee);

        tx.commit();

        session.close();

        factory.close();
    }
}
