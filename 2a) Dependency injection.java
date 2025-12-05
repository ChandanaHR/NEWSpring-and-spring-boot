//Charger.java
package com.example1.demo1;

import org.springframework.stereotype.Component;

@Component
public class Charger {
	public void charge() {
		System.out.println("Charging");
		}
}

//Phone.java
package com.example1.demo1;

import org.springframework.stereotype.Component;

@Component
public class Phone {
	private Charger charger;


  //Constructor based injection
	public Phone(Charger charger) {
		this.charger = charger;
	}
	
	public void display() {
		charger.charge();
		System.out.println("Phone is ON 58%");
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

//        Orderservice orderService = context.getBean(Orderservice.class);
//
//        orderService.placeorder(499.99);
		
		Phone phone = context.getBean(Phone.class);
		phone.display();
    }
}

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

