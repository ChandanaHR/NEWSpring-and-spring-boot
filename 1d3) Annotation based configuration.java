//Paymentgateway.java
package com.example1.demo1;

public interface Paymentgateway {
	void pay(double amount);
}


//RazorpayPaymentGateway
package com.example1.demo1;

import org.springframework.stereotype.Component;

@Component
public class RazorpayPaymentGateway implements Paymentgateway {
	@Override
	public void pay(double amount) {
		System.out.println("Payment of amount" +amount+ "through razorpay");
	}
}

//Paymentservice.java
package com.example1.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Paymentservice {
	@Autowired
	private Paymentgateway paymentgateway;

public void makePayment(double amount) {
	paymentgateway.pay(amount);
}
}

//Orderrepository.java
package com.example1.demo1;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class Orderrepository {
	public void save(double amount) {
		System.out.println("Order placed with the amount" +amount);
		}
}

//Orderservice.java
package com.example1.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Orderservice {
	@Autowired
	private Paymentservice paymentservice;
	
	@Autowired
	private Orderrepository orderrepo;
	
	public void placeorder(double amount) {
		System.out.println("Placing the order......");
		paymentservice.makePayment(amount);
		orderrepo.save(amount);
		System.out.println("Successfully placed the order");
	}
}

//AppConfig.java
package com.example1.demo1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example1.demo1")
public class AppConfig {
	
}


//Main.java
package com.example1.demo1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Orderservice orderService = context.getBean(Orderservice.class);

        orderService.placeorder(499.99);
    }
}
