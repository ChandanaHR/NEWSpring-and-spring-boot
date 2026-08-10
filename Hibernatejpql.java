The main difference is that JPQL is a JPA standard, whereas HQL is Hibernate-specific.
  JPQL = Java Persistence Query Language
  JPQL is the query language defined by JPA for querying entity objects.

  JPQL allows us to query Java entities and their fields instead of directly querying database tables and columns.
  JPQL works with:
Employee
name
salary

  Why do we need JPQL?
  Without JPQL, you could write database-specific SQL:
SELECT *
FROM employee
WHERE salary > 60000;
But this query depends on the database.
  JPQL allows you to write:
    SELECT e
    FROM Employee e
    WHERE e.salary > 60000
  The JPA provider converts that JPQL into SQL.
JPQL
  ↓
JPA Provider
  ↓
SQL
  ↓
Database
If Hibernate is your JPA provider:

JPQL
  ↓
Hibernate
  ↓
SQL
  ↓
MySQL

JPQL vs HQL
This is the most important thing to understand.
JPQL	HQL
JPA standard	Hibernate-specific
Defined by JPA	Defined by Hibernate
Portable between JPA providers	Mainly tied to Hibernate
Uses entities	Uses entities
Uses entity fields	Uses entity fields
Syntax is very similar	Syntax is very similar


JPQL
SELECT e FROM Employee e
HQL
FROM Employee

  JPQLDemo
│
├── src/main/java
│   │
│   └── com.example
│       │
│       ├── entity
│       │   └── Employee.java
│       │
│       ├── config
│       │   └── JPAUtil.java
│       │
│       └── jpql
│           ├── SelectExample.java
│           ├── WhereExample.java
│           ├── OrderByExample.java
│           ├── ParameterExample.java
│           ├── LikeExample.java
│           ├── InExample.java
│           ├── AggregateExample.java
│           ├── UpdateExample.java
│           └── DeleteExample.java
│
├── src/main/resources
│   └── META-INF
│       └── persistence.xml
│
└── pom.xml

 persistence.xml 
  src/main/resources/META-INF/persistence.xml

  <?xml version="1.0" encoding="UTF-8"?>

<persistence
    xmlns="https://jakarta.ee/xml/ns/persistence"
    version="3.2">

    <persistence-unit
        name="jpqlPU"
        transaction-type="RESOURCE_LOCAL">

        <provider>
            org.hibernate.jpa.HibernatePersistenceProvider
        </provider>

        <class>com.example.entity.Employee</class>

        <properties>

            <property
                name="jakarta.persistence.jdbc.driver"
                value="com.mysql.cj.jdbc.Driver"/>

            <property
                name="jakarta.persistence.jdbc.url"
                value="jdbc:mysql://localhost:3306/jpql_db"/>

            <property
                name="jakarta.persistence.jdbc.user"
                value="root"/>

            <property
                name="jakarta.persistence.jdbc.password"
                value="root"/>

            <property
                name="hibernate.hbm2ddl.auto"
                value="update"/>

            <property
                name="hibernate.show_sql"
                value="true"/>

            <property
                name="hibernate.format_sql"
                value="true"/>

        </properties>

    </persistence-unit>

</persistence>
  
 a) <persistence-unit>
<persistence-unit
    name="jpqlPU"
    transaction-type="RESOURCE_LOCAL">
A persistence unit is a group of configuration settings that tells JPA:
"Here is the database configuration and the entity classes that belong to this application."

  name="jpqlPU"
name="jpqlPU"
This is simply the name of your persistence unit.

  transaction-type="RESOURCE_LOCAL"
transaction-type="RESOURCE_LOCAL"
This tells JPA:
"This application will manage database transactions locally."

  <provider>
<provider>
    org.hibernate.jpa.HibernatePersistenceProvider
</provider>
  JPA is a specification, Hibernate is an implementation.
JPA says:
"Here are the rules for persistence."
Hibernate says:
"I will implement those rules."
So this line tells JPA:
"Use Hibernate as the JPA provider."

  <class>
<class>com.example.entity.Employee</class>
This tells JPA:
"Employee is one of my entity classes."

  JPAUtil
This class creates the EntityManagerFactory.

package com.example.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("jpqlPU");

    public static EntityManager getEntityManager() {

        return emf.createEntityManager();
    }
}


Insert sample data
package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import com.example.config.JPAUtil;
import com.example.entity.Employee;

public class InsertData {

    public static void main(String[] args) {

        EntityManager em =
                JPAUtil.getEntityManager();

        EntityTransaction tx =
                em.getTransaction();

        tx.begin();

        em.persist(
                new Employee(
                        "Chandana",
                        70000,
                        "IT"
                )
        );

        em.persist(
                new Employee(
                        "Rahul",
                        60000,
                        "HR"
                )
        );

        em.persist(
                new Employee(
                        "Ravi",
                        50000,
                        "IT"
                )
        );

        em.persist(
                new Employee(
                        "Anjali",
                        80000,
                        "Finance"
                )
        );

        em.persist(
                new Employee(
                        "Kiran",
                        45000,
                        "HR"
                )
        );

        tx.commit();

        em.close();

        System.out.println("Data inserted");
    }
}

Basic JPQL SELECT
  SELECT e FROM Employee e
  package com.example.jpql;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.example.config.JPAUtil;
import com.example.entity.Employee;

public class SelectExample {

    public static void main(String[] args) {

        EntityManager em =
                JPAUtil.getEntityManager();

        List<Employee> employees =
                em.createQuery(
                        "SELECT e FROM Employee e",
                        Employee.class
                ).getResultList();

        for (Employee employee : employees) {

            System.out.println(employee);

        }

        em.close();
    }
}

WHERE Clause

Suppose we want employees whose salary is greater than ₹60,000.

SELECT e
FROM Employee e
WHERE e.salary > 60000
Code
List<Employee> employees =
        em.createQuery(
                "SELECT e FROM Employee e " +
                "WHERE e.salary > 60000",
                Employee.class
        ).getResultList();

Order by
List<Employee> employees =
        em.createQuery(
                "SELECT e FROM Employee e " +
                "ORDER BY e.salary DESC",
                Employee.class
        ).getResultList();


Selecting Only One Column
Suppose we only need employee names.

SELECT e.name
FROM Employee e

Code:
List<String> names =
        em.createQuery(
                "SELECT e.name FROM Employee e",
                String.class
        ).getResultList();

names.forEach(System.out::println);

Selecting Multiple Columns
Suppose we need:

name
salary

JPQL:

SELECT e.name, e.salary
FROM Employee e
Because each row has two values, the result is:
List<Object[]>

Example:

List<Object[]> results =
        em.createQuery(
                "SELECT e.name, e.salary FROM Employee e"
        ).getResultList();

for (Object[] row : results) {

    System.out.println(
            "Name = " + row[0]
    );

    System.out.println(
            "Salary = " + row[1]
    );
}


Named Parameters
This is extremely important.

Instead of:

SELECT e
FROM Employee e
WHERE e.salary > 60000

we use:

SELECT e
FROM Employee e
WHERE e.salary > :salary

Then:

query.setParameter("salary", 60000);
package com.example.jpql;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import com.example.config.JPAUtil;
import com.example.entity.Employee;

public class NamedParameterExample {

    public static void main(String[] args) {

        EntityManager em =
                JPAUtil.getEntityManager();

        TypedQuery<Employee> query =
                em.createQuery(
                        "SELECT e FROM Employee e " +
                        "WHERE e.salary > :salary",
                        Employee.class
                );

        query.setParameter("salary", 60000);

        List<Employee> employees =
                query.getResultList();

        employees.forEach(
                System.out::println
        );

        em.close();
    }
}

LIKE
Suppose we want employees whose name starts with R.

SELECT e
FROM Employee e
WHERE e.name LIKE :pattern

Code:

TypedQuery<Employee> query =
        em.createQuery(
                "SELECT e FROM Employee e " +
                "WHERE e.name LIKE :pattern",
                Employee.class
        );

query.setParameter("pattern", "R%");

List<Employee> employees =
        query.getResultList();

IN
Suppose we need employees from IT or HR.

SELECT e
FROM Employee e
WHERE e.department IN :departments

Code:

List<String> departments =
        List.of("IT", "HR");

TypedQuery<Employee> query =
        em.createQuery(
                "SELECT e FROM Employee e " +
                "WHERE e.department IN :departments",
                Employee.class
        );

query.setParameter(
        "departments",
        departments
);

List<Employee> employees =
        query.getResultList();
