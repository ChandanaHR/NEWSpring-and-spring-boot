//Employee entity
package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name="employee")
public class Employee {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String name;

    private double salary;

    public Employee() {}

    public Employee(String name,double salary){
        this.name=name;
        this.salary=salary;
    }

    public int getId(){ return id; }

    public void setId(int id){ this.id=id; }

    public String getName(){ return name; }

    public void setName(String name){
        this.name=name;
    }

    public double getSalary(){
        return salary;
    }

    public void setSalary(double salary){
        this.salary=salary;
    }
}
//Save employee
Session session = sessionFactory.openSession();
Transaction tx = session.beginTransaction();

Employee employee =
new Employee("Chandana",60000);

session.persist(employee);

tx.commit();
session.close();

//Fetch employee
Session session = sessionFactory.openSession();

Employee employee =
session.find(Employee.class,1);

System.out.println(employee.getName());

session.close();

//Update employee
Session session = sessionFactory.openSession();
Transaction tx = session.beginTransaction();

Employee employee =
session.find(Employee.class,1);

employee.setSalary(85000);

session.merge(employee);

tx.commit();
session.close();

//Delete employee
Session session = sessionFactory.openSession();
Transaction tx = session.beginTransaction();

Employee employee =
session.find(Employee.class,1);

session.remove(employee);

tx.commit();
session.close();
