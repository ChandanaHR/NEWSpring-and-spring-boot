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
  
