//Student.jsvs
package com.example1.demo1;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class Student implements DisposableBean {
	public Student() {
		System.out.println("1. Student object created");
	}
	@PostConstruct
	public void init() {
		System.out.println("3. Student initialized");
	}
	@PreDestroy
	public void destroy1() {
		System.out.println("5. Student destroyed");
	}
	@Override
	public void destroy() {
		System.out.println("6. Student cleanup using disposablebean");
	}
	
}
//Beanpostprocess.java
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
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		Student t = context.getBean(Student.class);
//		t.notifyuser("sms");
		((AbstractApplicationContext) context).close();
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
