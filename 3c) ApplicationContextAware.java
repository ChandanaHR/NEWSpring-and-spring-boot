//Email.java
package com.example2.demo2;

import org.springframework.stereotype.Component;

@Component("email")
public class Email {
	public void send() {
		System.out.println("Email sent");
	}
}

//SMS.java
package com.example2.demo2;

import org.springframework.stereotype.Component;

@Component("sms")
public class SMS {
	public void send() {
		System.out.println("SMS sent");
	}
}

//NotificationService.java
package com.example2.demo2;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class NotificationService implements ApplicationContextAware {
	
	private ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.context = applicationContext;
	} 
	
	public void notifyuser(String type) {
		if(type.equals("email")) {
			Email emailservice = context.getBean("email", Email.class);
			emailservice.send();
		}
		else {
			SMS smsservice = context.getBean("sms", SMS.class);
			smsservice.send();
		}
	}
}
