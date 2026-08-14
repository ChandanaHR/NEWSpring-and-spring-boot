Service
  package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }


    // 1. flush()
    public void flushExample() {

        Employee employee =
                repository.findById(1)
                        .orElseThrow();

        employee.setSalary(80000);

        repository.flush();
    }


    // 2. saveAndFlush()
    public Employee saveAndFlushExample() {

        Employee employee =
                new Employee("Anita", 55000);

        return repository.saveAndFlush(employee);
    }


    // 3. saveAllAndFlush()
    public List<Employee> saveAllAndFlushExample() {

        Employee e1 =
                new Employee("Raj", 45000);

        Employee e2 =
                new Employee("Sneha", 65000);

        Employee e3 =
                new Employee("Vijay", 75000);

        return repository.saveAllAndFlush(
                List.of(e1, e2, e3)
        );
    }


    // 4. deleteAllInBatch()
    public void deleteAllInBatchExample() {

        repository.deleteAllInBatch();
    }


    // 5. deleteAllByIdInBatch()
    public void deleteAllByIdInBatchExample(
            List<Integer> ids) {

        repository.deleteAllByIdInBatch(ids);
    }


    // 6. getReferenceById()
    public Employee getReferenceByIdExample(
            Integer id) {

        Employee employee =
                repository.getReferenceById(id);

        return employee;
    }
}
Controller code
package com.example.demo.controller;

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

    // 1. flush()
    @PutMapping("/flush")
    public String flushExample() {

        service.flushExample();

        return "Employee salary updated and changes flushed";
    }


    // 2. saveAndFlush()
    @PostMapping("/save-and-flush")
    public Employee saveAndFlushExample() {

        return service.saveAndFlushExample();
    }


    // 3. saveAllAndFlush()
    @PostMapping("/save-all-and-flush")
    public List<Employee> saveAllAndFlushExample() {

        return service.saveAllAndFlushExample();
    }


    // 4. deleteAllInBatch()
    @DeleteMapping("/delete-all-batch")
    public String deleteAllInBatchExample() {

        service.deleteAllInBatchExample();

        return "All employees deleted in batch";
    }


    // 5. deleteAllByIdInBatch()
    @DeleteMapping("/delete-by-ids-batch")
    public String deleteAllByIdInBatchExample(
            @RequestBody List<Integer> ids) {

        service.deleteAllByIdInBatchExample(ids);

        return "Employees deleted successfully";
    }


    // 6. getReferenceById()
    @GetMapping("/{id}/reference")
    public Employee getReferenceByIdExample(
            @PathVariable Integer id) {

        return service.getReferenceByIdExample(id);
    }
}
  a) flush():
  Suppose database contains:

id | name | salary
-------------------
1  | Ravi | 50000
Step 1 — Find employee
repository.findById(1);

Hibernate executes something like:

SELECT *
FROM employee
WHERE emp_id = 1;

Hibernate creates an Employee object:

Employee
----------------
empId = 1
name = Ravi
salary = 50000

This entity is now managed by the persistence context.
  Now Java object becomes:

Employee
----------------
empId = 1
name = Ravi
salary = 80000
But we haven't explicitly written:
UPDATE
Hibernate uses dirty checking.
It knows:
Original:
salary = 50000
Current:
salary = 80000
Something changed!
  Step 3 — flush()
repository.flush();
Hibernate synchronizes the persistence context with the database.
Conceptually:
UPDATE employee
SET name = 'Ravi',
    salary = 80000
WHERE emp_id = 1;
flush() does not mean commit.
Think:
flush()
    =
"Send pending changes to the database"
while:
commit()
    =
"Finalize the transaction"

  b saveandflush()
  This means saveandflush()
  public Employee saveAndFlushExample() {

    Employee employee =
            new Employee("Anita", 55000);

    return repository.saveAndFlush(employee);
}
Because employee is new:
empId = null
  Saves the entity
Immediately flushes the persistence context to the database
  save() → save the entity in JPA/Hibernate's persistence context
saveAndFlush() → save + immediately synchronize with the database
  Think of Persistence Context as a temporary workspace/cache maintained by JPA/Hibernate for entities.

It keeps track of Java objects that are currently being managed by JPA.
  Example

Suppose the database contains:

employee
-------------------------
id   name       salary
-------------------------
1    Rahul      50000

You retrieve Rahul:

Employee emp = employeeRepository.findById(1)
        .orElseThrow();

Now Hibernate manages this object.

Conceptually:

Database
   ↓
Employee(id=1, salary=50000)
   ↓
Persistence Context

Now you change:

emp.setSalary(70000);

What happens?

You might think:

emp.setSalary(70000)
        ↓
UPDATE database

But that's not necessarily what happens immediately.

Instead:

emp.setSalary(70000)
        ↓
Persistence Context
        ↓
salary changed from 50000 → 70000

Hibernate is tracking the change.

4. This is called Dirty Checking
  Then what is Flushing?

Flushing means synchronizing the changes in the persistence context with the database.

For example:

Persistence Context


Employee
id = 1
name = Rahul
salary = 70000

After flushing:

Persistence Context
        ↓
      FLUSH
        ↓
Database
