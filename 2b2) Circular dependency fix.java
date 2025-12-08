//Charger.java
package com.example1.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Charger {
	@Autowired
	private Helperclass helper;
	
	public void commonlogic() {
		helper.common();
	}
}

//Phone.java
package com.example1.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Phone {
	@Autowired
	private Helperclass helper;
	
	public void commonlogic() {
		helper.common();
	}
}


//Helperclass.java
package com.example1.demo1;

import org.springframework.stereotype.Service;

@Service
public class Helperclass {
	public void common() {
		System.out.println("Both are interlinked");
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

//Main.java
package com.example1.demo1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		Charger charge1 = context.getBean(Charger.class);
		Phone phone1 = context.getBean(Phone.class);
		
		charge1.commonlogic();
		phone1.commonlogic();
		
    }
}
