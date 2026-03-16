Introduction to Spring Data JPA

Spring Data JPA is a module of Spring that makes database operations very easy.

Normally, when we interact with a database we write many SQL queries.

Spring Data JPA automatically generates queries for us.

Example without Spring Data JPA
INSERT INTO student VALUES(1,'Ram','Java');
With Spring Data JPA

Just write:

studentRepository.save(student);

Spring automatically converts it to SQL.

Why it is used

Reduces database code

Automatic CRUD operations

Easy integration with Spring Boot

Less SQL writing

3. What is Hibernate

Hibernate is an ORM framework for Java.

It converts Java objects into database tables.

Example

Java Object

Student s = new Student();
s.setName("Ram");

Hibernate converts this into:

INSERT INTO student (name) VALUES ('Ram');
Important Point

Hibernate implements JPA.

JPA → Specification
Hibernate → Implementation
4. What is Object Relational Mapping (ORM)

ORM means mapping Java objects to database tables.

Simple Meaning
Java	Database
Class	Table
Object	Row
Variable	Column
Example

Java Class

public class Student {

    private int id;
    private String name;

}

Database Table

id	name
1	Ram
2	Ravi

ORM automatically maps these.

Advantage

You work with Java objects instead of SQL tables.

5. Spring Boot Project Setup

The easiest way to create a project is using Spring Initializr.

Steps

Open start.spring.io

Choose

Project: Maven
Language: Java
Spring Boot Version: Latest

Add Dependencies

Spring Web
Spring Data JPA
MySQL Driver

Click Generate

Project structure

src
 ├─ main
 │   ├─ java
 │   └─ resources
 │        └─ application.properties
6. Connecting with Database

We connect the database in application.properties.

Example for MySQL

spring.datasource.url=jdbc:mysql://localhost:3306/studentdb
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
Explanation
Property	Meaning
datasource.url	Database location
username	DB username
password	DB password
ddl-auto	Creates/updates tables
show-sql	Shows SQL in console
7. Creating Entity Class

Entity class represents database table.

Example

import jakarta.persistence.*;

@Entity
public class Student {

    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String course;

}

This class becomes a table in database.

8. What is JPA

JPA means Java Persistence API.

It is a specification that defines how Java objects should interact with databases.

Important point:

JPA is not a framework

It is just rules or interface.

Frameworks like Hibernate implement JPA.

9. JPA vs Hibernate
Feature	JPA	Hibernate
Type	Specification	Framework
Purpose	Defines rules	Implements rules
Developer	Oracle	Hibernate Team
Usage	Interfaces	Actual implementation

Simple idea

JPA = Rules
Hibernate = Engine that follows rules
10. Dependencies in Maven

Dependencies are libraries required for our project.

In pom.xml we add:

<dependencies>

<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
<groupId>mysql</groupId>
<artifactId>mysql-connector-j</artifactId>
</dependency>

</dependencies>
Meaning
Dependency	Purpose
spring-boot-starter-data-jpa	Database operations
mysql-connector	Connect MySQL database
11. application.properties Configuration

This file contains project configuration settings.

Example

spring.datasource.url=jdbc:mysql://localhost:3306/studentdb
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
Important Settings
Property	Purpose
datasource	Database connection
hibernate.ddl-auto	Table creation
show-sql	Show SQL queries
database-platform	Database type
12. Entity Annotations

Annotations tell JPA how to map class to table.

️⃣ @Entity

Marks class as database table.

@Entity
public class Student
️⃣ @Table

Used to define table name.

@Table(name="students")
️⃣ @Id

Defines primary key.

@Id
private int id;
️⃣ @GeneratedValue

Automatically generates ID.

@GeneratedValue
private int id;
@Column

Defines column properties.

@Column(name="student_name")
private String name;
Simple Flow of Spring Data JPA
Java Entity
     ↓
Hibernate ORM
     ↓
JPA
     ↓
Database Table
