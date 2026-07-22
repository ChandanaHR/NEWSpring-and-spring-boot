//BankService.java
package spring_springboot.Myfirstproject;

public interface BankService {
	void deposit(int amount);
	void withdraw(int amount);
	String checkBalance();
}
//Bankserviceclass.java
package spring_springboot.Myfirstproject;

import org.springframework.stereotype.Service;

@Service
public class Bankserviceclass implements BankService {

	@Override
	public void deposit(int amount) {
		// TODO Auto-generated method stub
		System.out.println("Amount deposited : " +amount);	
		}

	@Override
	public void withdraw(int amount) {
		// TODO Auto-generated method stub
		if(amount > 10000) {
			throw new RuntimeException("Insufficient balance");
		}
		System.out.println("Withdraw successful ");
	}

	@Override
	public String checkBalance() {
		// TODO Auto-generated method stub
		return "Current balance: 50000";
	}

}
//Bankcontroller.java
package spring_springboot.Myfirstproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Bankcontroller {
	@Autowired
	private Bankserviceclass service;
	
	@GetMapping("/deposit/{amount}")
	public String deposit(@PathVariable int amount) {
		service.deposit(amount);
		return "Deposit completed";
	}
	
	@GetMapping("/withdraw/{amount}")
	public String withdraw(@PathVariable int amount) {
		service.withdraw(amount);
		return "Withdrawal completed";
	}
	
	@GetMapping("/balance")
	public String balance() {
		return  service.checkBalance();
	}
}
//Aspectclass.java
package spring_springboot.Myfirstproject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Aspectclass {
	@Pointcut("execution(* spring_springboot.Myfirstproject.Bankserviceclass*.*(..))")
	public void allMethods() {}
	
	@Before("allMethods()")
	public void before(JoinPoint jp) {
		System.out.println("Before: " +jp.getSignature().getName());
	}
	@After("allMethods()")
	public void after(JoinPoint jp) {
		System.out.println("After: " +jp.getSignature().getName());
	}
	@AfterReturning(pointcut ="allMethods()",returning="result")
	public void afterReturning(Object result) {
		System.out.println("Returned: " +result);
	}
	@AfterThrowing(pointcut ="allMethods()",throwing="ex")
	public void afterThrowing(Exception ex) {
		System.out.println("Exception:" +ex.getMessage());
	}
	@Around("allMethods()")
	public Object around(ProceedingJoinPoint jp) throws Throwable {
		long start = System.currentTimeMillis();
		System.out.println("Started");
		Object result = jp.proceed();
		long end = System.currentTimeMillis();
		System.out.println("Execution Time: " + (end-start));
		return result;
	}
}
//pom.xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.5.3</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>spring_springboot</groupId>
	<artifactId>Myfirstproject</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name/>
	<description/>
	<url/>
	<licenses>
		<license/>
	</licenses>
	<developers>
		<developer/>
	</developers>
	<scm>
		<connection/>
		<developerConnection/>
		<tag/>
		<url/>
	</scm>
	<properties>
		<java.version>21</java.version>
	</properties>
	<dependencies>
		<!--<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>-->
		<!--<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>-->
		<!--<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc</artifactId>
		</dependency>-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-aop</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<!--<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>-->
		<!--<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>-->
		<!--<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa-test</artifactId>
			<scope>test</scope>
		</dependency>-->
		<!--<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security-test</artifactId>
			<scope>test</scope>
		</dependency>-->
		<!--<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc-test</artifactId>
			<scope>test</scope>
		</dependency>-->
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<executions>
					<execution>
						<id>default-compile</id>
						<phase>compile</phase>
						<goals>
							<goal>compile</goal>
						</goals>
						<configuration>
							<annotationProcessorPaths>
								<path>
									<groupId>org.projectlombok</groupId>
									<artifactId>lombok</artifactId>
								</path>
							</annotationProcessorPaths>
						</configuration>
					</execution>
					<execution>
						<id>default-testCompile</id>
						<phase>test-compile</phase>
						<goals>
							<goal>testCompile</goal>
						</goals>
						<configuration>
							<annotationProcessorPaths>
								<path>
									<groupId>org.projectlombok</groupId>
									<artifactId>lombok</artifactId>
								</path>
							</annotationProcessorPaths>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>

</project>
