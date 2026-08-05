HQL stands for Hibernate Query Language.

It is a query language provided by Hibernate to perform CRUD operations on Entity objects instead of database tables.
  HQL is used to retrieve, update, and delete data using Java Entity class names, not database table names.

  Imagine you have a company database.
Database Table
employee
Hibernate creates a Java class.
Employee.java
Instead of writing
SELECT * FROM employee;
you write
FROM Employee
  HQL always works with Entity classes.

  Why HQL?

Without Hibernate
Java
↓
SQL
↓
Database

  With Hibernate
Java
↓
HQL
↓
Hibernate
↓
SQL
↓
Database
Hibernate converts HQL into SQL automatically.
  | SQL               | HQL                  |
| ----------------- | -------------------- |
| Uses Table Name   | Uses Entity Class    |
| Uses Column Names | Uses Entity Fields   |
| Database Specific | Database Independent |

  SQL
  SELECT *
FROM employee;

HQL
  FROM Employee

  Maven dependencies
  <dependencies>

    <!-- Hibernate -->
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>7.0.6.Final</version>
    </dependency>

    <!-- MySQL -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>9.3.0</version>
    </dependency>

    <!-- Jakarta Persistence -->
    <dependency>
        <groupId>jakarta.persistence</groupId>
        <artifactId>jakarta.persistence-api</artifactId>
        <version>3.2.0</version>
    </dependency>

</dependencies>

  Employee entity
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

    @Override
    public String toString() {
        return empId + " " + name + " " + salary;
    }
}

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
            jdbc:mysql://localhost:3306/hibernate_db
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

    </session-factory>

</hibernate-configuration>

  Hibernate utility
  package com.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.example.entity.Employee;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {

        sessionFactory = new Configuration()

                .configure()

                .addAnnotatedClass(Employee.class)

                .buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {

        return sessionFactory;
    }
}

Insert Sample data
  package com.example.insert;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class InsertEmployee {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Transaction tx =
                session.beginTransaction();

        session.persist(new Employee("Chandana",70000));

        session.persist(new Employee("Rahul",60000));

        session.persist(new Employee("Ravi",50000));

        tx.commit();

        session.close();

        System.out.println("Employees Inserted Successfully");
    }
}

First HQL Program (SELECT)
  package com.example.hql;

import java.util.List;

import org.hibernate.Session;

import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class HQLSelectExample {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        List<Employee> employees =

                session.createQuery(
                        "FROM Employee",
                        Employee.class
                ).list();

        for(Employee employee : employees){

            System.out.println(employee);

        }

        session.close();
    }
}

SQL Generated by Hibernate
Even though you wrote
FROM Employee
Hibernate converts it into

select
    e.emp_id,
    e.name,
    e.salary
from
    employee e;
Notice
You never wrote SQL.
Hibernate generated it automatically.


  How HQL Works Internally
Step 1

You write

FROM Employee

↓

Step 2

Hibernate reads the Employee entity.

@Entity
@Table(name="employee")

↓

Step 3

Hibernate knows

Employee

↓

employee table

↓

Step 4

Hibernate generates SQL.

SELECT *
FROM employee;

↓

Step 5

MySQL returns records.

↓

Step 6

Hibernate converts each row into an Employee object.

Employee

↓

empId = 1

name = Chandana

salary = 70000

↓

Step 7

Returns

List<Employee>

to your Java program.


  What is WHERE Clause?

The WHERE clause is used to filter records based on a condition.

Simple Definition

WHERE clause retrieves only those records that satisfy the given condition.
Retrieve employees whose salary is greater than ₹60,000.
  package com.example.hql;

import java.util.List;

import org.hibernate.Session;

import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class HQLWhereExample {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        List<Employee> employees =

                session.createQuery(

                        "FROM Employee WHERE salary > 60000",

                        Employee.class

                ).list();

        for(Employee emp : employees){

            System.out.println(emp);

        }

        session.close();
    }
}

Retrieve employee by name.
  package com.example.hql;

import java.util.List;

import org.hibernate.Session;

import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class HQLWhereName {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        List<Employee> list =

                session.createQuery(

                        "FROM Employee WHERE name='Rahul'",

                        Employee.class

                ).list();

        list.forEach(System.out::println);

        session.close();

    }

}

Multiple Conditions
  List<Employee> list =

session.createQuery(

"FROM Employee WHERE salary > 50000 AND name='Rahul'",

Employee.class

).list();

ORDER BY CLAUSE
  Descending Order
  package com.example.hql;

import java.util.List;

import org.hibernate.Session;

import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class HQLOrderByDesc {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        List<Employee> list =

                session.createQuery(

                        "FROM Employee ORDER BY salary DESC",

                        Employee.class

                ).list();

        list.forEach(System.out::println);

        session.close();

    }

}

Sort by Name
  List<Employee> list =

session.createQuery(

"FROM Employee ORDER BY name",

Employee.class

).list();

Combining WHERE + ORDER BY

Retrieve employees with salary greater than ₹50,000 and sort them by salary in descending order.
  package com.example.hql;

import java.util.List;

import org.hibernate.Session;

import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class HQLWhereOrderBy {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        List<Employee> list =

                session.createQuery(

                        "FROM Employee WHERE salary > 50000 ORDER BY salary DESC",

                        Employee.class

                ).list();

        list.forEach(System.out::println);

        session.close();

    }

}

Named parameters
  A Named Parameter is a placeholder in an HQL query that starts with a colon (:).
Instead of writing a fixed value in the query, you give the parameter a name and later assign a value using setParameter().
Simple Definition
A Named Parameter is a variable used inside an HQL query whose value is assigned later.
Why do we use Named Parameters?
Suppose you write:
FROM Employee WHERE salary > 50000
Here, 50000 is hardcoded.
If tomorrow you want to search employees with salary greater than 70000, you'll have to change the code.
Instead, use a named parameter.
FROM Employee WHERE salary > :salary
Now you can pass any value at runtime.

  package com.example.hql;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class NamedParameterExample {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Query<Employee> query =
                session.createQuery(
                        "FROM Employee WHERE salary > :salary",
                        Employee.class
                );

        query.setParameter("salary", 60000);

        List<Employee> employees = query.list();

        for (Employee employee : employees) {
            System.out.println(employee);
        }

        session.close();
    }
}
Example 2: Find Employee by Name
  package com.example.hql;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class FindByName {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Query<Employee> query =
                session.createQuery(
                        "FROM Employee WHERE name = :name",
                        Employee.class
                );

        query.setParameter("name", "Rahul");

        List<Employee> list = query.list();

        list.forEach(System.out::println);

        session.close();
    }
}
Example 3: Multiple Named Parameters
  package com.example.hql;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class MultipleNamedParameters {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Query<Employee> query =
                session.createQuery(
                        "FROM Employee WHERE salary > :salary AND name = :name",
                        Employee.class
                );

        query.setParameter("salary", 50000);
        query.setParameter("name", "Rahul");

        List<Employee> employees = query.list();

        employees.forEach(System.out::println);

        session.close();
    }
}

You must write SQL manually.

  LIKE Operator
  The LIKE operator is used to search for data that matches a pattern.
  Wildcard	Meaning
%	Zero or more characters
_	Exactly one character
  package com.example.hql;

import java.util.List;
import org.hibernate.Session;
import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class HQLLikeExample {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Employee> list = session.createQuery(
                "FROM Employee WHERE name LIKE 'R%'",
                Employee.class
        ).list();

        list.forEach(System.out::println);

        session.close();
    }
}
List<Employee> list = session.createQuery(
        "FROM Employee WHERE name LIKE '%i'",
        Employee.class
).list();
List<Employee> list = session.createQuery(
        "FROM Employee WHERE name LIKE '%an%'",
        Employee.class
).list();


IN Operator
What is IN?

The IN operator checks whether a value exists in a given list.
  package com.example.hql;

import java.util.List;

import org.hibernate.Session;

import com.example.entity.Employee;
import com.example.util.HibernateUtil;

public class HQLInExample {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        List<Employee> list =

                session.createQuery(

                        "FROM Employee WHERE department IN ('IT','HR')",

                        Employee.class

                ).list();

        list.forEach(System.out::println);

        session.close();
    }
}

Aggregate Functions
Aggregate functions calculate values from multiple rows.
  package com.example.hql;

import org.hibernate.Session;

import com.example.util.HibernateUtil;

public class CountExample {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Long count = session.createQuery(

                "SELECT COUNT(*) FROM Employee",

                Long.class

        ).getSingleResult();

        System.out.println("Total Employees : " + count);

        session.close();

    }

}
SUM()
What is SUM()?

Calculates the total of a numeric column.

HQL
SELECT SUM(salary) FROM Employee
Program
Double totalSalary = session.createQuery(

        "SELECT SUM(salary) FROM Employee",

        Double.class

).getSingleResult();

System.out.println(totalSalary);
AVG()
What is AVG()?

Calculates the average value.

HQL
SELECT AVG(salary) FROM Employee
Program
Double average = session.createQuery(

        "SELECT AVG(salary) FROM Employee",

        Double.class

).getSingleResult();

System.out.println(average);
MAX()
What is MAX()?

Returns the highest value.

HQL
SELECT MAX(salary) FROM Employee
Program
Double maxSalary = session.createQuery(

        "SELECT MAX(salary) FROM Employee",

        Double.class

).getSingleResult();

System.out.println(maxSalary);
MIN()
What is MIN()?

Returns the smallest value.

HQL
SELECT MIN(salary) FROM Employee
Program
Double minSalary = session.createQuery(

        "SELECT MIN(salary) FROM Employee",

        Double.class

).getSingleResult();

System.out.println(minSalary);

UPDATE Query
What is UPDATE?
Updates existing records without loading each entity into memory.
  package com.example.hql;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.example.util.HibernateUtil;

public class HQLUpdateExample {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Transaction transaction =
                session.beginTransaction();

        Query query = session.createQuery(

                "UPDATE Employee SET salary = salary + 5000"

        );

        int rows = query.executeUpdate();

        transaction.commit();

        System.out.println("Updated Rows : " + rows);

        session.close();
    }
}

DELETE Query
What is DELETE?
Deletes records that match a condition.
  package com.example.hql;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.example.util.HibernateUtil;

public class HQLDeleteExample {

    public static void main(String[] args) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Transaction transaction =
                session.beginTransaction();

        Query query = session.createQuery(

                "DELETE FROM Employee WHERE name='Kiran'"

        );

        int rows = query.executeUpdate();

        transaction.commit();

        System.out.println("Deleted Rows : " + rows);

        session.close();
    }
}


