//BeanLifecycle.java
package com.example2.demo2;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

@Component
public class BeanLifecycle implements BeanNameAware {
	public BeanLifecycle() {
		System.out.println("Constructor called");
		}

	@Override
	public void setBeanName(String name) {
		// TODO Auto-generated method stub
		System.out.println("Bean name provided by Spring: " + name);
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
//		Orderservice orderService = context.getBean(Orderservice.class);
//		orderService.placeorder(2000);
//		Phone phone = context.getBean(Phone.class);
//		Charger charge = context.getBean(Charger.class);
//		phone.commonlogic();
//		charge.commonlogic();
		BeanLifecycle bean = context.getBean(BeanLifecycle.class);
	}
}

Custom Bean Name
  @Component("chandana")
public class BeanLifecycle implements BeanNameAware {
	public BeanLifecycle() {
		System.out.println("Constructor called");
		}

	@Override
	public void setBeanName(String name) {
		// TODO Auto-generated method stub
		System.out.println("Bean name provided by Spring: " + name);
	}
}
