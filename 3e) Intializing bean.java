
//Method1 : afterPropertiesSet
//Student.java
package com.example2.demo2;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Student implements InitializingBean {
	public Student() {
		System.out.println("1. Student object created");
		}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("2. Bean Initialized")	;
		}
	
	
}

//Main.java
package com.example2.demo2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(Appconfig.class);
		Student t = context.getBean(Student.class);
//		t.callStudent();
	}
}


//Method 2: init method
//Student.java
package com.example2.demo2;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Student  {
	public Student() {
		System.out.println("1. Student object created");
		}

	
	public void myinit() {
		// TODO Auto-generated method stub
		System.out.println("2. Bean Initialized")	;
		}
	
	
}
//Appconfig.java
package com.example2.demo2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages="com.example2.demo2")
public class Appconfig {
	@Bean(initMethod = "myinit")
	public Student student() {
		return new Student();
	}
}
//Main.java
package com.example2.demo2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages="com.example2.demo2")
public class Appconfig {
	@Bean(initMethod = "myinit")
	public Student student() {
		return new Student();
	}
}
