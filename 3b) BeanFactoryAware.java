//Student.java
package com.example2.demo2;

import org.springframework.stereotype.Component;

@Component
public class Student {
	public void study() {
		System.out.println("Student studying");
	}
}

//Teacher.java
package com.example2.demo2;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

@Component
public class Teacher implements BeanFactoryAware {
	BeanFactory beanfactory;
	public Teacher() {
		System.out.println("Teacher created");	
		}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		this.beanfactory = beanFactory;
		System.out.println("BeanFactory given to Teacher");
	}
	public void callStudent() {
		Student s = beanfactory.getBean(Student.class);
		s.study();
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
		Teacher t = context.getBean(Teacher.class);
		t.callStudent();
	}
}
