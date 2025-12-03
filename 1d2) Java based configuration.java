//Paymentgateway.java
package com.example1.demo1;

public interface Paymentgateway {
	void processPayment(double amount);
}

//RazorpayPaymentGateway
package com.example1.demo1;

public class RazorpayPaymentGateway implements Paymentgateway {
	@Override
	public void processPayment(double amount) {
		System.out.println("Payment of amount" +amount+ "through razorpay");
	}
}

//Paymentservice.java
package com.example1.demo1;

public class Paymentservice {
	private Paymentgateway paymentgateway;

public Paymentservice(Paymentgateway paymentgateway) {
	super();
	this.paymentgateway = paymentgateway;
}

public void makePayment(double amount) {
	paymentgateway.processPayment(amount);
}
}

//Orderservice.java
package com.example1.demo1;

public class Orderservice {
		private Paymentservice paymentservice;

		public Orderservice(Paymentservice paymentservice) {
			super();
			this.paymentservice = paymentservice;
		}

		public void placeorder(double amount) {
			System.out.println("Order placed.....");
			paymentservice.makePayment(amount);
			System.out.println("Order completed....");
		}
		
		
}

//Appconfig.java
package com.example1.demo1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	@Bean
	public Paymentgateway PG() {
		return new RazorpayPaymentGateway();
	}
	
	@Bean
	public Paymentservice PS() {
		return new Paymentservice(PG());
	}
	
	@Bean
	public Orderservice OS() {
		return new Orderservice(PS());
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

        Orderservice orderService = context.getBean(Orderservice.class);

        orderService.placeorder(499.99);
    }
}

//Demo1Application
package com.example1.demo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}

}

