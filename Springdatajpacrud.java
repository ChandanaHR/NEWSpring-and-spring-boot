Maven dependencies
  <dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>

</dependencies>

  application.properties
  spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true

  Employee entity
  package com.example.employee.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empId;

    private String name;

    private double salary;

    public Employee() {
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
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
}

Repository
  package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository
        extends CrudRepository<Employee, Integer> {

}

Service class
  package com.example.employee.service;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    // CREATE / UPDATE
    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    // CREATE / UPDATE MULTIPLE
    public Iterable<Employee> saveAll(
            List<Employee> employees) {

        return repository.saveAll(employees);
    }

    // READ BY ID
    public Optional<Employee> findById(Integer id) {
        return repository.findById(id);
    }

    // READ ALL
    public Iterable<Employee> findAll() {
        return repository.findAll();
    }

    // READ MULTIPLE IDS
    public Iterable<Employee> findAllById(
            List<Integer> ids) {

        return repository.findAllById(ids);
    }

    // CHECK EXISTENCE
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    // COUNT
    public long count() {
        return repository.count();
    }

    // DELETE BY ID
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    // DELETE ENTITY
    public void delete(Employee employee) {
        repository.delete(employee);
    }

    // DELETE MULTIPLE BY ID
    public void deleteAllById(List<Integer> ids) {
        repository.deleteAllById(ids);
    }

    // DELETE ALL
    public void deleteAll() {
        repository.deleteAll();
    }
}

Controller
package com.example.employee.controller;

import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public Employee save(
            @RequestBody Employee employee) {

        return service.save(employee);
    }

    // READ ALL
    @GetMapping
    public Iterable<Employee> findAll() {

        return service.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(
            @PathVariable Integer id) {

        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(
            @PathVariable Integer id,
            @RequestBody Employee employee) {

        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        employee.setEmpId(id);

        Employee updated =
                service.save(employee);

        return ResponseEntity.ok(updated);
    }

    // DELETE BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id) {

        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    // COUNT
    @GetMapping("/count")
    public long count() {

        return service.count();
    }

    // EXISTS
    @GetMapping("/exists/{id}")
    public boolean exists(
            @PathVariable Integer id) {

        return service.existsById(id);
    }

    // FIND MULTIPLE IDS
    @PostMapping("/find-by-ids")
    public Iterable<Employee> findByIds(
            @RequestBody List<Integer> ids) {

        return service.findAllById(ids);
    }

    // SAVE MULTIPLE
    @PostMapping("/bulk")
    public Iterable<Employee> saveAll(
            @RequestBody List<Employee> employees) {

        return service.saveAll(employees);
    }

    // DELETE MULTIPLE
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAllById(
            @RequestBody List<Integer> ids) {

        service.deleteAllById(ids);

        return ResponseEntity.noContent().build();
    }
}
spring.jpa.properties.hibernate.format_sql=true

  29. Test CREATE

Send:

POST /employees

Body:

{
    "name": "Alice",
    "salary": 50000
}

Response:

{
    "empId": 1,
    "name": "Alice",
    "salary": 50000
}
30. Add More Employees
POST /employees
{
    "name": "Bob",
    "salary": 60000
}

Then:

POST /employees
{
    "name": "Charlie",
    "salary": 70000
}

Database:

+--------+---------+--------+
| emp_id | name    | salary |
+--------+---------+--------+
| 1      | Alice   | 50000  |
| 2      | Bob     | 60000  |
| 3      | Charlie | 70000  |
+--------+---------+--------+
31. Test findAll()
GET /employees

Response:

[
    {
        "empId": 1,
        "name": "Alice",
        "salary": 50000
    },
    {
        "empId": 2,
        "name": "Bob",
        "salary": 60000
    },
    {
        "empId": 3,
        "name": "Charlie",
        "salary": 70000
    }
]
32. Test findById()
GET /employees/2

Response:

{
    "empId": 2,
    "name": "Bob",
    "salary": 60000
}

Internally:

service.findById(2);

which calls:

repository.findById(2);
33. Test existsById()
GET /employees/exists/2

Response:

true

For:

GET /employees/exists/99

Response:

false
34. Test count()
GET /employees/count

Response:

3
35. Test UPDATE

Suppose Bob's salary changes.

PUT /employees/2

Body:

{
    "name": "Bob",
    "salary": 75000
}

The service does:

employee.setEmpId(2);
service.save(employee);

Because the entity has ID 2, this is treated as an update of the existing employee.

Result:

2 | Bob | 75000
36. Test DELETE
DELETE /employees/2

Flow:

DELETE /employees/2
        ↓
Controller
        ↓
Service
        ↓
existsById(2)
        ↓
deleteById(2)
        ↓
Hibernate
        ↓
DELETE SQL
        ↓
MySQL

Employee 2 is deleted.

37. Test saveAll()

Request:

POST /employees/bulk

Body:

[
    {
        "name": "David",
        "salary": 55000
    },
    {
        "name": "Emma",
        "salary": 65000
    },
    {
        "name": "Frank",
        "salary": 75000
    }
]

Controller:

service.saveAll(employees);

Service:

repository.saveAll(employees);
38. Test findAllById()

Request:

POST /employees/find-by-ids

Body:

[1, 3, 5]

Service:

repository.findAllById(ids);

This retrieves employees whose IDs are among the supplied IDs.

  Method	Purpose
save(entity)	Insert/update one entity
saveAll(entities)	Insert/update multiple entities
findById(id)	Find one entity
findAll()	Find all entities
findAllById(ids)	Find multiple entities by IDs
existsById(id)	Check whether ID exists
count()	Count records
deleteById(id)	Delete by ID
delete(entity)	Delete an entity
deleteAllById(ids)	Delete multiple by IDs
deleteAll(entities)	Delete specified entities
deleteAll()	Delete all entities

  Limitation
  CrudRepository gives you basic CRUD.

Suppose you want:

Find employee by name
Find employee by salary
Find employees whose salary > 50000
Sort employees
Pagination

CrudRepository itself isn't designed to give you all of those richer repository capabilities.

That's where you generally move to:

JpaRepository
