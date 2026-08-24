Example data:

emp_id	name	salary	department	age
1	Rahul	50000	IT	25
2	Priya	60000	HR	28
3	Amit	70000	IT	30
4	Sneha	45000	Finance	24
5	Ravi	80000	IT	35

  Employee entity
  package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empId;

    private String name;

    private double salary;

    private String department;

    private int age;

    private String email;

    public Employee() {
    }

    public Employee(String name, double salary,
                    String department, int age, String email) {
        this.name = name;
        this.salary = salary;
        this.department = department;
        this.age = age;
        this.email = email;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

Employeerepository
  package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Employee;

public interface EmployeeRepository
        extends JpaRepository<Employee, Integer> {

    List<Employee> findByName(String name);
    List<Employee> findByDepartment(String department);

}

a) Multiple Conditions — And
Suppose we want:
Find employees who work in IT AND have salary greater than 60000.
We can write:
List<Employee> findByDepartmentAndSalaryGreaterThan(
        String department,
        double salary
);
Equivalent SQL:
SELECT *
FROM employee
WHERE department = ?
AND salary > ?;
Usage:
List<Employee> employees =
    employeeRepository.findByDepartmentAndSalaryGreaterThan(
        "IT",
        60000
    );

b) Or
Suppose:
Find employees from IT OR HR.
List<Employee> findByDepartmentOrDepartment(
        String department1,
        String department2
);
This works, but there is a better approach using In, which we'll see later.
The generated query is conceptually:
SELECT *
FROM employee
WHERE department = ?
OR department = ?;

c) GreaterThan
Suppose:
Find employees whose salary is greater than 60000.
List<Employee> findBySalaryGreaterThan(double salary);
Equivalent SQL:
SELECT *
FROM employee
WHERE salary > ?;
Usage:
List<Employee> employees =
        employeeRepository.findBySalaryGreaterThan(60000);

d) GreaterThanEqual
List<Employee> findBySalaryGreaterThanEqual(double salary);
Means:
WHERE salary >= ?
Example:
findBySalaryGreaterThanEqual(60000);
means:
salary >= 60000

e) LessThan
List<Employee> findBySalaryLessThan(double salary);
Means:
WHERE salary < ?
Example:
findBySalaryLessThan(50000);

f) LessThanEqual
List<Employee> findBySalaryLessThanEqual(double salary);
Means:
WHERE salary <= ?

g) Between
Suppose:
Find employees whose salary is between ₹50,000 and ₹70,000.
List<Employee> findBySalaryBetween(
        double minSalary,
        double maxSalary
);
Spring generates approximately:
SELECT *
FROM employee
WHERE salary BETWEEN ? AND ?;
Usage:
List<Employee> employees =
    employeeRepository.findBySalaryBetween(50000, 70000);

h) Like
Suppose we want:
Find employees whose name starts with "Ra".
List<Employee> findByNameLike(String name);
Usage:
findByNameLike("Ra%");
Conceptually:
SELECT *
FROM employee
WHERE name LIKE 'Ra%';

i) Containing
Instead of manually writing %, we can use:
List<Employee> findByNameContaining(String name);
Usage:
findByNameContaining("vi");
This means:
WHERE name LIKE '%vi%';

j) StartingWith
List<Employee> findByNameStartingWith(String name);
Usage:
findByNameStartingWith("Ra");
Conceptually:
WHERE name LIKE 'Ra%';
Matches:
Rahul
Ravi

k) EndingWith
List<Employee> findByNameEndingWith(String name);
Usage:
findByNameEndingWith("vi");
Conceptually:
WHERE name LIKE '%vi';

l) IgnoreCase
Suppose database contains:
Rahul
and user searches:
rahul
We can use:
List<Employee> findByNameIgnoreCase(String name);
Usage:
findByNameIgnoreCase("rahul");
Spring handles case-insensitive comparison.

m)OrderBy
Suppose:
Find all IT employees and sort them by salary descending.
List<Employee> findByDepartmentOrderBySalaryDesc(
        String department
);
Usage:
findByDepartmentOrderBySalaryDesc("IT");
Conceptually:
SELECT *
FROM employee
WHERE department = 'IT'
ORDER BY salary DESC;
For ascending:
List<Employee> findByDepartmentOrderBySalaryAsc(
        String department
);

n) In

Suppose:

Find employees whose department is either IT or HR.

Instead of:

findByDepartmentOrDepartment(...)

we can use:

List<Employee> findByDepartmentIn(
        List<String> departments
);

Usage:

List<String> departments =
        List.of("IT", "HR");

List<Employee> employees =
        employeeRepository.findByDepartmentIn(departments);

Conceptually:

SELECT *
FROM employee
WHERE department IN ('IT', 'HR');

This is very useful.

  o)Not

Suppose:

Find employees who are NOT from IT.

List<Employee> findByDepartmentNot(String department);

Usage:

findByDepartmentNot("IT");

Conceptually:

WHERE department <> 'IT';

p) IsNull

Suppose an employee can have a null email.

List<Employee> findByEmailIsNull();

Conceptually:

WHERE email IS NULL;
23. IsNotNull
List<Employee> findByEmailIsNotNull();

Conceptually:

WHERE email IS NOT NULL;

q) Exists-like Requirements

For many complex existence checks, Spring Data offers existsBy....

For example:

boolean existsByEmail(String email);

Usage:

boolean result =
        employeeRepository.existsByEmail("rahul@gmail.com");

If the employee exists:

true

Otherwise:

false

This is very useful when checking duplicate emails before registration.

  Repository
  package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Employee;

public interface EmployeeRepository
        extends JpaRepository<Employee, Integer> {


    // =========================================================
    // 1. findBy
    // =========================================================

    List<Employee> findByName(String name);


    // =========================================================
    // 2. And
    // =========================================================

    List<Employee> findByDepartmentAndSalary(
            String department,
            double salary
    );


    // =========================================================
    // 3. Or
    // =========================================================

    List<Employee> findByDepartmentOrDepartment(
            String department1,
            String department2
    );


    // =========================================================
    // 4. GreaterThan
    // =========================================================

    List<Employee> findBySalaryGreaterThan(
            double salary
    );


    // =========================================================
    // 5. GreaterThanEqual
    // =========================================================

    List<Employee> findBySalaryGreaterThanEqual(
            double salary
    );


    // =========================================================
    // 6. LessThan
    // =========================================================

    List<Employee> findBySalaryLessThan(
            double salary
    );


    // =========================================================
    // 7. LessThanEqual
    // =========================================================

    List<Employee> findBySalaryLessThanEqual(
            double salary
    );


    // =========================================================
    // 8. Between
    // =========================================================

    List<Employee> findBySalaryBetween(
            double minSalary,
            double maxSalary
    );


    // =========================================================
    // 9. Like
    // =========================================================

    List<Employee> findByNameLike(
            String pattern
    );


    // =========================================================
    // 10. Containing
    // =========================================================

    List<Employee> findByNameContaining(
            String text
    );


    // =========================================================
    // 11. StartingWith
    // =========================================================

    List<Employee> findByNameStartingWith(
            String prefix
    );


    // =========================================================
    // 12. EndingWith
    // =========================================================

    List<Employee> findByNameEndingWith(
            String suffix
    );


    // =========================================================
    // 13. IgnoreCase
    // =========================================================

    List<Employee> findByNameIgnoreCase(
            String name
    );


    // =========================================================
    // 14. In
    // =========================================================

    List<Employee> findByDepartmentIn(
            List<String> departments
    );


    // =========================================================
    // 15. Not
    // =========================================================

    List<Employee> findByDepartmentNot(
            String department
    );


    // =========================================================
    // 16. IsNull
    // =========================================================

    List<Employee> findByEmailIsNull();


    // =========================================================
    // 17. IsNotNull
    // =========================================================

    List<Employee> findByEmailIsNotNull();


    // =========================================================
    // 18. OrderBy
    // =========================================================

    List<Employee> findByDepartmentOrderBySalaryDesc(
            String department
    );


    // =========================================================
    // 19. countBy
    // =========================================================

    long countByDepartment(
            String department
    );


    // =========================================================
    // 20. existsBy
    // =========================================================

    boolean existsByEmail(
            String email
    );


    // =========================================================
    // 21. deleteBy
    // =========================================================

    void deleteByEmail(
            String email
    );


    // =========================================================
    // 22. Complex Derived Query
    // =========================================================

    List<Employee>
    findByDepartmentAndSalaryGreaterThanAndAgeLessThan(
            String department,
            double salary,
            int age
    );
}
Employeeservice
  package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }


    // =========================================================
    // Save Employee
    // =========================================================

    public Employee saveEmployee(Employee employee) {

        return employeeRepository.save(employee);
    }


    // =========================================================
    // Get All Employees
    // =========================================================

    public List<Employee> getAllEmployees() {

        return employeeRepository.findAll();
    }


    // =========================================================
    // 1. findByName
    // =========================================================

    public List<Employee> findByName(String name) {

        return employeeRepository.findByName(name);
    }


    // =========================================================
    // 2. And
    // =========================================================

    public List<Employee> findByDepartmentAndSalary(
            String department,
            double salary) {

        return employeeRepository
                .findByDepartmentAndSalary(
                        department,
                        salary
                );
    }


    // =========================================================
    // 3. Or
    // =========================================================

    public List<Employee> findByDepartmentOrDepartment(
            String department1,
            String department2) {

        return employeeRepository
                .findByDepartmentOrDepartment(
                        department1,
                        department2
                );
    }


    // =========================================================
    // 4. GreaterThan
    // =========================================================

    public List<Employee> salaryGreaterThan(
            double salary) {

        return employeeRepository
                .findBySalaryGreaterThan(salary);
    }


    // =========================================================
    // 5. GreaterThanEqual
    // =========================================================

    public List<Employee> salaryGreaterThanEqual(
            double salary) {

        return employeeRepository
                .findBySalaryGreaterThanEqual(salary);
    }


    // =========================================================
    // 6. LessThan
    // =========================================================

    public List<Employee> salaryLessThan(
            double salary) {

        return employeeRepository
                .findBySalaryLessThan(salary);
    }


    // =========================================================
    // 7. LessThanEqual
    // =========================================================

    public List<Employee> salaryLessThanEqual(
            double salary) {

        return employeeRepository
                .findBySalaryLessThanEqual(salary);
    }


    // =========================================================
    // 8. Between
    // =========================================================

    public List<Employee> salaryBetween(
            double min,
            double max) {

        return employeeRepository
                .findBySalaryBetween(min, max);
    }


    // =========================================================
    // 9. Like
    // =========================================================

    public List<Employee> nameLike(String pattern) {

        return employeeRepository
                .findByNameLike(pattern);
    }


    // =========================================================
    // 10. Containing
    // =========================================================

    public List<Employee> nameContaining(String text) {

        return employeeRepository
                .findByNameContaining(text);
    }


    // =========================================================
    // 11. StartingWith
    // =========================================================

    public List<Employee> nameStartingWith(
            String prefix) {

        return employeeRepository
                .findByNameStartingWith(prefix);
    }


    // =========================================================
    // 12. EndingWith
    // =========================================================

    public List<Employee> nameEndingWith(
            String suffix) {

        return employeeRepository
                .findByNameEndingWith(suffix);
    }


    // =========================================================
    // 13. IgnoreCase
    // =========================================================

    public List<Employee> nameIgnoreCase(
            String name) {

        return employeeRepository
                .findByNameIgnoreCase(name);
    }


    // =========================================================
    // 14. IN
    // =========================================================

    public List<Employee> departmentIn(
            List<String> departments) {

        return employeeRepository
                .findByDepartmentIn(departments);
    }


    // =========================================================
    // 15. NOT
    // =========================================================

    public List<Employee> departmentNot(
            String department) {

        return employeeRepository
                .findByDepartmentNot(department);
    }


    // =========================================================
    // 16. IS NULL
    // =========================================================

    public List<Employee> emailIsNull() {

        return employeeRepository
                .findByEmailIsNull();
    }


    // =========================================================
    // 17. IS NOT NULL
    // =========================================================

    public List<Employee> emailIsNotNull() {

        return employeeRepository
                .findByEmailIsNotNull();
    }


    // =========================================================
    // 18. ORDER BY
    // =========================================================

    public List<Employee> departmentOrderBySalaryDesc(
            String department) {

        return employeeRepository
                .findByDepartmentOrderBySalaryDesc(
                        department
                );
    }


    // =========================================================
    // 19. COUNT
    // =========================================================

    public long countByDepartment(
            String department) {

        return employeeRepository
                .countByDepartment(department);
    }


    // =========================================================
    // 20. EXISTS
    // =========================================================

    public boolean existsByEmail(
            String email) {

        return employeeRepository
                .existsByEmail(email);
    }


    // =========================================================
    // 21. DELETE
    // =========================================================

    public void deleteByEmail(
            String email) {

        employeeRepository.deleteByEmail(email);
    }


    // =========================================================
    // 22. Complex Query
    // =========================================================

    public List<Employee>
    complexSearch(
            String department,
            double salary,
            int age) {

        return employeeRepository
                .findByDepartmentAndSalaryGreaterThanAndAgeLessThan(
                        department,
                        salary,
                        age
                );
    }
}

Employeecontroller
  package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(
            EmployeeService employeeService) {

        this.employeeService = employeeService;
    }


    // =========================================================
    // SAVE
    // =========================================================

    @PostMapping
    public Employee saveEmployee(
            @RequestBody Employee employee) {

        return employeeService
                .saveEmployee(employee);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public List<Employee> getAllEmployees() {

        return employeeService
                .getAllEmployees();
    }


    // =========================================================
    // 1. findByName
    // =========================================================

    @GetMapping("/name/{name}")
    public List<Employee> findByName(
            @PathVariable String name) {

        return employeeService
                .findByName(name);
    }


    // =========================================================
    // 2. AND
    // =========================================================

    @GetMapping("/department-and-salary")
    public List<Employee> departmentAndSalary(
            @RequestParam String department,
            @RequestParam double salary) {

        return employeeService
                .findByDepartmentAndSalary(
                        department,
                        salary
                );
    }


    // =========================================================
    // 3. OR
    // =========================================================

    @GetMapping("/department-or")
    public List<Employee> departmentOr(
            @RequestParam String department1,
            @RequestParam String department2) {

        return employeeService
                .findByDepartmentOrDepartment(
                        department1,
                        department2
                );
    }


    // =========================================================
    // 4. GREATER THAN
    // =========================================================

    @GetMapping("/salary-greater-than")
    public List<Employee> salaryGreaterThan(
            @RequestParam double salary) {

        return employeeService
                .salaryGreaterThan(salary);
    }


    // =========================================================
    // 5. GREATER THAN OR EQUAL
    // =========================================================

    @GetMapping("/salary-greater-than-equal")
    public List<Employee> salaryGreaterThanEqual(
            @RequestParam double salary) {

        return employeeService
                .salaryGreaterThanEqual(salary);
    }


    // =========================================================
    // 6. LESS THAN
    // =========================================================

    @GetMapping("/salary-less-than")
    public List<Employee> salaryLessThan(
            @RequestParam double salary) {

        return employeeService
                .salaryLessThan(salary);
    }


    // =========================================================
    // 7. LESS THAN OR EQUAL
    // =========================================================

    @GetMapping("/salary-less-than-equal")
    public List<Employee> salaryLessThanEqual(
            @RequestParam double salary) {

        return employeeService
                .salaryLessThanEqual(salary);
    }


    // =========================================================
    // 8. BETWEEN
    // =========================================================

    @GetMapping("/salary-between")
    public List<Employee> salaryBetween(
            @RequestParam double min,
            @RequestParam double max) {

        return employeeService
                .salaryBetween(min, max);
    }


    // =========================================================
    // 9. LIKE
    // =========================================================

    @GetMapping("/name-like")
    public List<Employee> nameLike(
            @RequestParam String pattern) {

        return employeeService
                .nameLike(pattern);
    }


    // =========================================================
    // 10. CONTAINING
    // =========================================================

    @GetMapping("/name-containing")
    public List<Employee> nameContaining(
            @RequestParam String text) {

        return employeeService
                .nameContaining(text);
    }


    // =========================================================
    // 11. STARTING WITH
    // =========================================================

    @GetMapping("/name-starting-with")
    public List<Employee> nameStartingWith(
            @RequestParam String prefix) {

        return employeeService
                .nameStartingWith(prefix);
    }


    // =========================================================
    // 12. ENDING WITH
    // =========================================================

    @GetMapping("/name-ending-with")
    public List<Employee> nameEndingWith(
            @RequestParam String suffix) {

        return employeeService
                .nameEndingWith(suffix);
    }


    // =========================================================
    // 13. IGNORE CASE
    // =========================================================

    @GetMapping("/name-ignore-case")
    public List<Employee> nameIgnoreCase(
            @RequestParam String name) {

        return employeeService
                .nameIgnoreCase(name);
    }


    // =========================================================
    // 14. IN
    // =========================================================

    @GetMapping("/department-in")
    public List<Employee> departmentIn(
            @RequestParam List<String> departments) {

        return employeeService
                .departmentIn(departments);
    }


    // =========================================================
    // 15. NOT
    // =========================================================

    @GetMapping("/department-not")
    public List<Employee> departmentNot(
            @RequestParam String department) {

        return employeeService
                .departmentNot(department);
    }


    // =========================================================
    // 16. IS NULL
    // =========================================================

    @GetMapping("/email-null")
    public List<Employee> emailIsNull() {

        return employeeService
                .emailIsNull();
    }


    // =========================================================
    // 17. IS NOT NULL
    // =========================================================

    @GetMapping("/email-not-null")
    public List<Employee> emailIsNotNull() {

        return employeeService
                .emailIsNotNull();
    }


    // =========================================================
    // 18. ORDER BY
    // =========================================================

    @GetMapping("/department-order")
    public List<Employee> departmentOrder(
            @RequestParam String department) {

        return employeeService
                .departmentOrderBySalaryDesc(
                        department
                );
    }


    // =========================================================
    // 19. COUNT
    // =========================================================

    @GetMapping("/count")
    public long count(
            @RequestParam String department) {

        return employeeService
                .countByDepartment(department);
    }


    // =========================================================
    // 20. EXISTS
    // =========================================================

    @GetMapping("/exists")
    public boolean exists(
            @RequestParam String email) {

        return employeeService
                .existsByEmail(email);
    }


    // =========================================================
    // 21. DELETE
    // =========================================================

    @DeleteMapping("/email/{email}")
    public String deleteByEmail(
            @PathVariable String email) {

        employeeService.deleteByEmail(email);

        return "Employee deleted successfully";
    }


    // =========================================================
    // 22. COMPLEX QUERY
    // =========================================================

    @GetMapping("/complex-search")
    public List<Employee> complexSearch(
            @RequestParam String department,
            @RequestParam double salary,
            @RequestParam int age) {

        return employeeService
                .complexSearch(
                        department,
                        salary,
                        age
                );
    }
}

