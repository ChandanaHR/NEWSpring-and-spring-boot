package springcore1.Core1;

import java.util.Scanner;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class Food implements InitializingBean {
	public Food() {
		System.out.println("1: Constructor for Food Called.");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("3: Food initialized after/in Constructor");
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("4: Food Bean initialized");
	}
//	public void destroy() {
//		System.out.println("4. Student destroyed");
//	}
	public void foodMenu() {
		System.out.println("FOOD LIST");
		Scanner scanner = new Scanner(System.in);
		int choice;
		System.out.println("Food 1: COFFEE:");
		System.out.println("----------------");
		System.out.println("1: Cold coffee");
		System.out.println("2: Hot coffee");
		System.out.println("Food 2: TEA:");
		System.out.println("----------------");
		System.out.println("3: Cold tea");
		System.out.println("4: Hot Tea");
		System.out.println("Food 3: VEG Biriyani:");
		System.out.println("------------------------");
		System.out.println("5: Paneer Biriyani");
		System.out.println("6: Palak Biriyani");
		System.out.println("Food 4: CHICKEN Biriyani:");
		System.out.println("---------------------------");
		System.out.println("7: Dum Biriyani");
		System.out.println("8: Hyderabadi Biriyani");
		System.out.println("9: EXIT");
		
		do {
			choice = scanner.nextInt();
			switch (choice) {
				case 1: System.out.println("Added Cold Coffee to order list"); break;
				case 2: System.out.println("Added Hot Coffee to order list"); break;
				case 3: System.out.println("Added Cold Tea to order list"); break;
				case 4: System.out.println("Added Hot Tea to order list"); break;
				case 5: System.out.println("Added Paneer Biriyani to order list"); break;
				case 6: System.out.println("Added Palak Biriyani to order list"); break;
				case 7: System.out.println("Added Dum Biriyani to order list"); break;
				case 8: System.out.println("Added Hyderabadi Biriyani to order list"); break;
				case 9: System.out.println("Exiting from order list"); break;
				default: System.out.println("Invalide choice!");
			}
		} while (choice != 9);
		scanner.close();
	}
	@PreDestroy
	public void destroy() {
		System.out.println("6: Food Bean Destroyed");
	}

}
package springcore1.Core1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  ApplicationContext context =
	                new AnnotationConfigApplicationContext(Appconfig.class);
		  
//		  private paymentplatform pf = new Reazorpaypaymentplatform();
//		  Paymentgateway, Paymentservice

//	        Paymentservice service = context.getBean("paymentService", Paymentservice.class);
//	        service.pay(999.00);
		 
//		   Orderservice order = context.getBean(Orderservice.class);
//		   order.processorder(1000);
//		  
//		  Phone phone = context.getBean(Phone.class);
//		  phone.phonecharge();
		  
//		  Beannameaware beanname = context.getBean(Beannameaware.class);
//		  Teacher beannameaware = context.getBean(Teacher.class);
//		  beannameaware.teacherdetails();
//		  Notificationservice notify = context.getBean(Notificationservice.class);
//		  notify.notifyuser("email");
//		  Laptop laptop = context.getBean(Laptop.class);
//		  laptop.laptopcharge();
//		  Beanpostprocess beanpost = context.getBean(Beanpostprocess.class);
//		  beanpost.postProcessBeforeInitialization();
//		  Student s1 = context.getBean(Student.class);
//		  s1.init();
//		  s1.study();
		  
		  Food f1 = context.getBean(Food.class);
//		  f1.init();
		  f1.foodMenu();
		  ((AbstractApplicationContext) context).close();
//		  f1.destroy();
	}

}
