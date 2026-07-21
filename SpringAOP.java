spring-aop-demo
│
├── controller
│     └── BankController.java
│
├── service
│     ├── BankService.java
│     └── BankServiceImpl.java
│
├── aspect
│     └── LoggingAspect.java
│
├── SpringAopApplication.java

  Dependencies
  <dependencies>

    <!-- Spring Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring AOP -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>

</dependencies>

  Service layer
  package com.example.service;

public interface BankService {

    void deposit(int amount);

    void withdraw(int amount);

    String checkBalance();

}

package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class BankServiceImpl implements BankService {

    @Override
    public void deposit(int amount) {

        System.out.println("Amount deposited : " + amount);

    }

    @Override
    public void withdraw(int amount) {

        if(amount > 10000){

            throw new RuntimeException("Insufficient Balance");

        }

        System.out.println("Withdraw successful : " + amount);

    }

    @Override
    public String checkBalance() {

        return "Current Balance : 50000";

    }

}

Controller
  package com.example.controller;

import com.example.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BankController {

    @Autowired
    private BankService service;

    @GetMapping("/deposit/{amount}")
    public String deposit(@PathVariable int amount){

        service.deposit(amount);

        return "Deposit Completed";

    }

    @GetMapping("/withdraw/{amount}")
    public String withdraw(@PathVariable int amount){

        service.withdraw(amount);

        return "Withdraw Completed";

    }

    @GetMapping("/balance")
    public String balance(){

        return service.checkBalance();

    }

}

Aspect class
  package com.example.aspect;

import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

}

Complete Aspect
  package com.example.aspect;

import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.example.service.*.*(..))")
    public void allMethods(){}

    @Before("allMethods()")
    public void before(JoinPoint jp){

        System.out.println("Before : " + jp.getSignature().getName());

    }

    @After("allMethods()")
    public void after(JoinPoint jp){

        System.out.println("After : " + jp.getSignature().getName());

    }

    @AfterReturning(pointcut="allMethods()", returning="result")
    public void afterReturning(Object result){

        System.out.println("Returned : " + result);

    }

    @AfterThrowing(pointcut="allMethods()", throwing="ex")
    public void afterThrowing(Exception ex){

        System.out.println("Exception : " + ex.getMessage());

    }

    @Around("allMethods()")
    public Object around(ProceedingJoinPoint jp) throws Throwable{

        long start = System.currentTimeMillis();

        System.out.println("Started");

        Object result = jp.proceed();

        long end = System.currentTimeMillis();

        System.out.println("Execution Time : " + (end-start));

        return result;

    }

}

Execution flow
  GET /deposit/1000

  Controller
      │
      ▼
@Before
      │
      ▼
@Around (Before)
      │
      ▼
deposit()
      │
      ▼
@AfterReturning
      │
      ▼
@After
      │
      ▼
@Around (After)
      │
      ▼
Response

  Exception case
  GET /withdraw/50000
  Controller
      │
      ▼
@Before
      │
      ▼
@Around (Before)
      │
      ▼
withdraw()
      │
      ▼
Exception
      │
      ├────────► @AfterThrowing
      │
      ├────────► @After
      │
      └────────► Exception propagates to caller
