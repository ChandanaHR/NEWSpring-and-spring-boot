//Constructor based
//Main.java
package com.example1.demo1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

//        Orderservice orderService = context.getBean(Orderservice.class);
//
//        orderService.placeorder(499.99);
		
		Phone phone = context.getBean(Phone.class);
		phone.display();
    }
}

//Charger.java
package com.example1.demo1;

import org.springframework.stereotype.Component;

@Component
public class Charger {
	private Phone phone;
	
	
	public Charger(Phone phone) {
		super();
		this.phone = phone;
	}


	//	public void charge() {
//		System.out.println("Charging");
//		}
	public String charge() {
		return "Charging..........";
	}
}

//Phone.java
package com.example1.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Phone {
	private Charger charger;
	
	public Phone(Charger charger) {
		this.charger = charger;
	}
	
//	@Autowired
//	public void setCharger(Charger charger) {
//		this.charger = charger;
//	}
	
	public void display() {
		charger.charge();
		System.out.println("Phone is ON 58%");
	}

}
//Error : Requested bean is currently in creation: Is there an unresolvable circular reference or an asynchronous initialization dependency?

//Setter based injection
//Main.java
package com.example1.demo1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

//        Orderservice orderService = context.getBean(Orderservice.class);
//
//        orderService.placeorder(499.99);
		
		Phone phone = context.getBean(Phone.class);
		phone.display();
    }
}

//Charger.java
package com.example1.demo1;

import org.springframework.stereotype.Component;

@Component
public class Charger {
	private Phone phone;
	
	
//	public Phone getPhone() {
//		return phone;
//	}


	public void setPhone(Phone phone) {
		this.phone = phone;
	}


	public void charge() {
		System.out.println("Charging........");
	}
}

//Phone.java
package com.example1.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Phone {
	private Charger charger;
	
//	public Phone(Charger charger) {
//		this.charger = charger;
//	}
	
	@Autowired
	public void setCharger(Charger charger) {
		this.charger = charger;
	}
	
	public void display() {
		charger.charge();
		System.out.println("Phone is ON 58%");
	}

}
//No error: Output we will get
