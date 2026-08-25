//pom.xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
         http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.3</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>query-demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>

        <!-- Spring MVC -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- MySQL -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

    </dependencies>

    <build>
        <plugins>

            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

        </plugins>
    </build>

</project>

  //Department
  package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees = new ArrayList<>();

    public Department() {
    }

    public Department(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}

//Employee
package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double salary;

    private String email;

    private String city;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Employee() {
    }

    public Employee(String name,
                    double salary,
                    String email,
                    String city,
                    Department department) {

        this.name = name;
        this.salary = salary;
        this.email = email;
        this.city = city;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}

//EmployeedepartmentDTO
  package com.example.demo.dto;

public class EmployeeDepartmentDTO {

    private String employeeName;
    private double salary;
    private String departmentName;

    public EmployeeDepartmentDTO(
            String employeeName,
            double salary,
            String departmentName) {

        this.employeeName = employeeName;
        this.salary = salary;
        this.departmentName = departmentName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public double getSalary() {
        return salary;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}

//Employeerepository
  package com.example.demo.repository;

import com.example.demo.dto.EmployeeDepartmentDTO;
import com.example.demo.entity.Employee;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {


    // =====================================================
    // 1. BASIC SELECT
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           """)
    List<Employee> getAllEmployees();


    // =====================================================
    // 2. WHERE
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.city = :city
           """)
    List<Employee> findByCity(
            @Param("city") String city);


    // =====================================================
    // 3. NAMED PARAMETER
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.salary > :salary
           """)
    List<Employee> findSalaryGreaterThan(
            @Param("salary") double salary);


    // =====================================================
    // 4. POSITIONAL PARAMETER
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.salary > ?1
           AND e.city = ?2
           """)
    List<Employee> findBySalaryAndCity(
            double salary,
            String city);


    // =====================================================
    // 5. AND
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.salary > :salary
           AND e.city = :city
           """)
    List<Employee> findUsingAnd(
            @Param("salary") double salary,
            @Param("city") String city);


    // =====================================================
    // 6. OR
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.city = :city1
           OR e.city = :city2
           """)
    List<Employee> findUsingOr(
            @Param("city1") String city1,
            @Param("city2") String city2);


    // =====================================================
    // 7. LIKE - STARTS WITH
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.name LIKE :name
           """)
    List<Employee> findNameStartingWith(
            @Param("name") String name);


    // =====================================================
    // 8. LIKE - CONTAINS
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))
           """)
    List<Employee> searchByName(
            @Param("name") String name);


    // =====================================================
    // 9. BETWEEN
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.salary BETWEEN :min AND :max
           """)
    List<Employee> findBySalaryRange(
            @Param("min") double min,
            @Param("max") double max);


    // =====================================================
    // 10. IN
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.city IN :cities
           """)
    List<Employee> findByCities(
            @Param("cities") List<String> cities);


    // =====================================================
    // 11. NOT IN
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.city NOT IN :cities
           """)
    List<Employee> findNotInCities(
            @Param("cities") List<String> cities);


    // =====================================================
    // 12. IS NULL
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.email IS NULL
           """)
    List<Employee> findEmployeesWithoutEmail();


    // =====================================================
    // 13. IS NOT NULL
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           WHERE e.email IS NOT NULL
           """)
    List<Employee> findEmployeesWithEmail();


    // =====================================================
    // 14. ORDER BY ASC
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           ORDER BY e.salary ASC
           """)
    List<Employee> sortBySalaryAscending();


    // =====================================================
    // 15. ORDER BY DESC
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           ORDER BY e.salary DESC
           """)
    List<Employee> sortBySalaryDescending();


    // =====================================================
    // 16. DISTINCT
    // =====================================================

    @Query("""
           SELECT DISTINCT e.city
           FROM Employee e
           """)
    List<String> findDistinctCities();


    // =====================================================
    // 17. COUNT
    // =====================================================

    @Query("""
           SELECT COUNT(e)
           FROM Employee e
           """)
    long countEmployees();


    // =====================================================
    // 18. SUM
    // =====================================================

    @Query("""
           SELECT SUM(e.salary)
           FROM Employee e
           """)
    Double getTotalSalary();


    // =====================================================
    // 19. AVG
    // =====================================================

    @Query("""
           SELECT AVG(e.salary)
           FROM Employee e
           """)
    Double getAverageSalary();


    // =====================================================
    // 20. MIN
    // =====================================================

    @Query("""
           SELECT MIN(e.salary)
           FROM Employee e
           """)
    Double getMinimumSalary();


    // =====================================================
    // 21. MAX
    // =====================================================

    @Query("""
           SELECT MAX(e.salary)
           FROM Employee e
           """)
    Double getMaximumSalary();


    // =====================================================
    // 22. GROUP BY
    // =====================================================

    @Query("""
           SELECT e.city, COUNT(e)
           FROM Employee e
           GROUP BY e.city
           """)
    List<Object[]> countEmployeesByCity();


    // =====================================================
    // 23. GROUP BY + HAVING
    // =====================================================

    @Query("""
           SELECT e.city, COUNT(e)
           FROM Employee e
           GROUP BY e.city
           HAVING COUNT(e) > :count
           """)
    List<Object[]> citiesWithMoreEmployees(
            @Param("count") long count);


    // =====================================================
    // 24. INNER JOIN
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           JOIN e.department d
           WHERE d.name = :department
           """)
    List<Employee> findEmployeesByDepartment(
            @Param("department") String department);


    // =====================================================
    // 25. JOIN + CONDITION
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           JOIN e.department d
           WHERE d.name = :department
           AND e.salary > :salary
           """)
    List<Employee> findDepartmentEmployeesWithSalary(
            @Param("department") String department,
            @Param("salary") double salary);


    // =====================================================
    // 26. LEFT JOIN
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           LEFT JOIN e.department d
           """)
    List<Employee> findEmployeesUsingLeftJoin();


    // =====================================================
    // 27. JOIN FETCH
    // =====================================================

    @Query("""
           SELECT e
           FROM Employee e
           JOIN FETCH e.department
           """)
    List<Employee> findEmployeesWithDepartment();


    // =====================================================
    // 28. DTO PROJECTION
    // =====================================================

    @Query("""
           SELECT new com.example.demo.dto.EmployeeDepartmentDTO(
               e.name,
               e.salary,
               d.name
           )
           FROM Employee e
           JOIN e.department d
           """)
    List<EmployeeDepartmentDTO> getEmployeeDepartmentDetails();


    // =====================================================
    // 29. UPDATE
    // =====================================================

    @Modifying
    @Query("""
           UPDATE Employee e
           SET e.salary = :salary
           WHERE e.id = :id
           """)
    int updateSalary(
            @Param("id") Long id,
            @Param("salary") double salary);


    // =====================================================
    // 30. UPDATE MULTIPLE FIELDS
    // =====================================================

    @Modifying
    @Query("""
           UPDATE Employee e
           SET e.salary = :salary,
               e.city = :city
           WHERE e.id = :id
           """)
    int updateSalaryAndCity(
            @Param("id") Long id,
            @Param("salary") double salary,
            @Param("city") String city);


    // =====================================================
    // 31. DELETE
    // =====================================================

    @Modifying
    @Query("""
           DELETE FROM Employee e
           WHERE e.id = :id
           """)
    int deleteEmployee(
            @Param("id") Long id);


    // =====================================================
    // 32. DELETE BY CITY
    // =====================================================

    @Modifying
    @Query("""
           DELETE FROM Employee e
           WHERE e.city = :city
           """)
    int deleteByCity(
            @Param("city") String city);


    // =====================================================
    // 33. NATIVE SQL
    // =====================================================

    @Query(
        value = """
                SELECT *
                FROM employee
                WHERE salary > :salary
                """,
        nativeQuery = true
    )
    List<Employee> findUsingNativeSQL(
            @Param("salary") double salary);


    // =====================================================
    // 34. NATIVE SQL COUNT
    // =====================================================

    @Query(
        value = """
                SELECT COUNT(*)
                FROM employee
                """,
        nativeQuery = true
    )
    long countUsingNativeSQL();
}

//Department Repository
package com.example.demo.repository;

import com.example.demo.entity.Department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    @Query("""
           SELECT d
           FROM Department d
           """)
    List<Department> getAllDepartments();
}

//Employeeservice
package com.example.demo.service;

import com.example.demo.dto.EmployeeDepartmentDTO;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }


    public List<Employee> getAllEmployees() {
        return repository.getAllEmployees();
    }


    public List<Employee> findByCity(String city) {
        return repository.findByCity(city);
    }


    public List<Employee> findSalaryGreaterThan(double salary) {
        return repository.findSalaryGreaterThan(salary);
    }


    public List<Employee> findBySalaryAndCity(
            double salary,
            String city) {

        return repository.findBySalaryAndCity(
                salary,
                city);
    }


    public List<Employee> findUsingAnd(
            double salary,
            String city) {

        return repository.findUsingAnd(
                salary,
                city);
    }


    public List<Employee> findUsingOr(
            String city1,
            String city2) {

        return repository.findUsingOr(
                city1,
                city2);
    }


    public List<Employee> searchByName(String name) {
        return repository.searchByName(name);
    }


    public List<Employee> findBySalaryRange(
            double min,
            double max) {

        return repository.findBySalaryRange(
                min,
                max);
    }


    public List<Employee> findByCities(
            List<String> cities) {

        return repository.findByCities(cities);
    }


    public List<Employee> sortAscending() {
        return repository.sortBySalaryAscending();
    }


    public List<Employee> sortDescending() {
        return repository.sortBySalaryDescending();
    }


    public List<String> getDistinctCities() {
        return repository.findDistinctCities();
    }


    public long countEmployees() {
        return repository.countEmployees();
    }


    public Double getTotalSalary() {
        return repository.getTotalSalary();
    }


    public Double getAverageSalary() {
        return repository.getAverageSalary();
    }


    public Double getMinimumSalary() {
        return repository.getMinimumSalary();
    }


    public Double getMaximumSalary() {
        return repository.getMaximumSalary();
    }


    public List<Object[]> countEmployeesByCity() {
        return repository.countEmployeesByCity();
    }


    public List<Object[]> citiesWithMoreEmployees(long count) {
        return repository.citiesWithMoreEmployees(count);
    }


    public List<Employee> findByDepartment(
            String department) {

        return repository.findEmployeesByDepartment(
                department);
    }


    public List<Employee> findDepartmentEmployeesWithSalary(
            String department,
            double salary) {

        return repository.findDepartmentEmployeesWithSalary(
                department,
                salary);
    }


    public List<EmployeeDepartmentDTO>
    getEmployeeDepartmentDetails() {

        return repository.getEmployeeDepartmentDetails();
    }


    @Transactional
    public int updateSalary(
            Long id,
            double salary) {

        return repository.updateSalary(
                id,
                salary);
    }


    @Transactional
    public int updateSalaryAndCity(
            Long id,
            double salary,
            String city) {

        return repository.updateSalaryAndCity(
                id,
                salary,
                city);
    }


    @Transactional
    public int deleteEmployee(Long id) {
        return repository.deleteEmployee(id);
    }


    @Transactional
    public int deleteByCity(String city) {
        return repository.deleteByCity(city);
    }


    public List<Employee> findUsingNativeSQL(
            double salary) {

        return repository.findUsingNativeSQL(salary);
    }
}

Employeecontroller
  package com.example.demo.controller;

import com.example.demo.dto.EmployeeDepartmentDTO;
import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }


    // =============================================
    // GET ALL
    // =============================================

    @GetMapping
    public List<Employee> getAll() {
        return service.getAllEmployees();
    }


    // =============================================
    // WHERE
    // =============================================

    @GetMapping("/city/{city}")
    public List<Employee> getByCity(
            @PathVariable String city) {

        return service.findByCity(city);
    }


    // =============================================
    // SALARY GREATER THAN
    // =============================================

    @GetMapping("/salary/{salary}")
    public List<Employee> salaryGreaterThan(
            @PathVariable double salary) {

        return service.findSalaryGreaterThan(
                salary);
    }


    // =============================================
    // AND
    // =============================================

    @GetMapping("/search")
    public List<Employee> search(
            @RequestParam double salary,
            @RequestParam String city) {

        return service.findUsingAnd(
                salary,
                city);
    }


    // =============================================
    // NAME SEARCH
    // =============================================

    @GetMapping("/name")
    public List<Employee> searchName(
            @RequestParam String name) {

        return service.searchByName(name);
    }


    // =============================================
    // SALARY RANGE
    // =============================================

    @GetMapping("/salary-range")
    public List<Employee> salaryRange(
            @RequestParam double min,
            @RequestParam double max) {

        return service.findBySalaryRange(
                min,
                max);
    }


    // =============================================
    // SORT ASCENDING
    // =============================================

    @GetMapping("/sort/asc")
    public List<Employee> sortAscending() {
        return service.sortAscending();
    }


    // =============================================
    // SORT DESCENDING
    // =============================================

    @GetMapping("/sort/desc")
    public List<Employee> sortDescending() {
        return service.sortDescending();
    }


    // =============================================
    // DISTINCT CITIES
    // =============================================

    @GetMapping("/cities")
    public List<String> distinctCities() {
        return service.getDistinctCities();
    }


    // =============================================
    // COUNT
    // =============================================

    @GetMapping("/count")
    public long count() {
        return service.countEmployees();
    }


    // =============================================
    // SUM
    // =============================================

    @GetMapping("/total-salary")
    public Double totalSalary() {
        return service.getTotalSalary();
    }


    // =============================================
    // AVG
    // =============================================

    @GetMapping("/average-salary")
    public Double averageSalary() {
        return service.getAverageSalary();
    }


    // =============================================
    // MIN
    // =============================================

    @GetMapping("/minimum-salary")
    public Double minimumSalary() {
        return service.getMinimumSalary();
    }


    // =============================================
    // MAX
    // =============================================

    @GetMapping("/maximum-salary")
    public Double maximumSalary() {
        return service.getMaximumSalary();
    }


    // =============================================
    // GROUP BY
    // =============================================

    @GetMapping("/group-by-city")
    public List<Object[]> groupByCity() {
        return service.countEmployeesByCity();
    }


    // =============================================
    // HAVING
    // =============================================

    @GetMapping("/cities/having")
    public List<Object[]> having(
            @RequestParam long count) {

        return service.citiesWithMoreEmployees(
                count);
    }


    // =============================================
    // JOIN
    // =============================================

    @GetMapping("/department/{department}")
    public List<Employee> getByDepartment(
            @PathVariable String department) {

        return service.findByDepartment(
                department);
    }


    // =============================================
    // JOIN + SALARY
    // =============================================

    @GetMapping("/department-search")
    public List<Employee> departmentSearch(
            @RequestParam String department,
            @RequestParam double salary) {

        return service
                .findDepartmentEmployeesWithSalary(
                        department,
                        salary);
    }


    // =============================================
    // DTO PROJECTION
    // =============================================

    @GetMapping("/details")
    public List<EmployeeDepartmentDTO>
    employeeDetails() {

        return service
                .getEmployeeDepartmentDetails();
    }


    // =============================================
    // UPDATE SALARY
    // =============================================

    @PutMapping("/{id}/salary")
    public String updateSalary(
            @PathVariable Long id,
            @RequestParam double salary) {

        service.updateSalary(id, salary);

        return "Salary updated successfully";
    }


    // =============================================
    // UPDATE SALARY + CITY
    // =============================================

    @PutMapping("/{id}")
    public String updateEmployee(
            @PathVariable Long id,
            @RequestParam double salary,
            @RequestParam String city) {

        service.updateSalaryAndCity(
                id,
                salary,
                city);

        return "Employee updated successfully";
    }


    // =============================================
    // DELETE
    // =============================================

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id) {

        service.deleteEmployee(id);

        return "Employee deleted successfully";
    }


    // =============================================
    // DELETE BY CITY
    // =============================================

    @DeleteMapping("/city/{city}")
    public String deleteByCity(
            @PathVariable String city) {

        service.deleteByCity(city);

        return "Employees deleted successfully";
    }


    // =============================================
    // NATIVE SQL
    // =============================================

    @GetMapping("/native")
    public List<Employee> nativeQuery(
            @RequestParam double salary) {

        return service.findUsingNativeSQL(
                salary);
    }
}

Insert Sample Data

You can insert departments first:

INSERT INTO department
(name, location)
VALUES
('IT', 'Bangalore'),
('HR', 'Mumbai'),
('Finance', 'Delhi'),
('Sales', 'Chennai');

Then employees.

Assuming department IDs are 1, 2, 3, 4:

INSERT INTO employee
(name, salary, email, city, department_id)
VALUES
('Ravi', 50000, 'ravi@gmail.com', 'Bangalore', 1),
('Rahul', 80000, 'rahul@gmail.com', 'Bangalore', 1),
('John', 90000, 'john@gmail.com', 'Mumbai', 1),
('Anu', 70000, 'anu@gmail.com', 'Mumbai', 2),
('Priya', 60000, 'priya@gmail.com', 'Delhi', 3),
('Kiran', 45000, NULL, 'Chennai', 4),
('Amit', 55000, 'amit@gmail.com', 'Bangalore', 1);
