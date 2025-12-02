//Paymentgateway.java
package com.example1.demo1;

public class Paymentgateway {
	public String paymentprocess(double amount) {
		return "Payment process" +amount+ "processed successfully";
	}
}

//Paymentservice.java
package com.example1.demo1;

public class Paymentservice {
	private Paymentgateway paymentgateway;

	//Setter injection
	public void setPaymentgateway(Paymentgateway paymentgateway) {
		this.paymentgateway = paymentgateway;
	}
	
	
	
	public void pay(double amount) {
		String result = paymentgateway.paymentprocess(amount);
		System.out.println(result);
	}
}


//Main.java
package com.example1.demo1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("beans.xml");

        Paymentservice service = context.getBean("paymentService", Paymentservice.class);
        service.pay(999.00);
    }
}

// beans.xml
  <?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- Bean for PaymentGateway -->
    <bean id="paymentGateway" class="com.example1.demo1.Paymentgateway" />

    <!-- Bean for PaymentService with setter injection -->
    <bean id="paymentService" class="com.example1.demo1.Paymentservice">
        <property name="paymentgateway" ref="paymentGateway" />
    </bean>

</beans>

    //Demo1Application.java
    package com.example1.demo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}

}
