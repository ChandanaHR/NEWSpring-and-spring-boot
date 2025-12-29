//Student.java
package com.example1.demo1;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Student {
	public Student() {
		System.out.println("1. Student object created");
	}
	@PostConstruct
	public void init() {
		System.out.println("3. Student initialized");
	}
}

//BeanPostProcess.java
package com.example1.demo1;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class Beanpostprocess implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		if(beanName.equals("student")) {
			System.out.println("2. Student check before initialization");
		}
		return bean;
	}
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		if(beanName.equals("student")) {
			System.out.println("4. Student check after initialization");
		}
		return bean;
	}
}

//Main.java
package com.example1.demo1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		Student t = context.getBean(Student.class);
//		t.notifyuser("sms");
		
    }
}

//Appconfig.java
package com.example1.demo1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example1.demo1")
public class AppConfig {
	
}
