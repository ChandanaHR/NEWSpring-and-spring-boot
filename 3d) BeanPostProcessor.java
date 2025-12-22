//Student.java
package com.example2.demo2;

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

//Beanpostprocess.java
package com.example2.demo2;

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
