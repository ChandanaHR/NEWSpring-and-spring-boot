//Main class
@SpringBootApplication
@EnableAspectJAutoProxy
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private MyService myService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        myService.display();
        
        try {
            myService.errorMethod();
        } catch (Exception e) {}
    }
}

//Service class
import org.springframework.stereotype.Service;

@Service
public class MyService {

    public void display() {
        System.out.println("Inside display method");
    }

    public void errorMethod() {
        System.out.println("Inside error method");
        throw new RuntimeException("Exception occurred");
    }
}

//Aspect class
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {

    // ✅ Pointcut
    @Pointcut("execution(* MyService.*(..))")
    public void myPointcut() {}

    // ✅ Before Advice
    @Before("myPointcut()")
    public void beforeAdvice(JoinPoint jp) {
        System.out.println("Before method: " + jp.getSignature().getName());
    }

    // ✅ After Advice
    @After("myPointcut()")
    public void afterAdvice() {
        System.out.println("After method execution");
    }

    // ✅ After Returning
    @AfterReturning("myPointcut()")
    public void afterReturningAdvice() {
        System.out.println("Method executed successfully");
    }

    // ✅ After Throwing
    @AfterThrowing("myPointcut()")
    public void afterThrowingAdvice() {
        System.out.println("Exception occurred in method");
    }

    // ✅ Around Advice (MOST IMPORTANT)
    @Around("myPointcut()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Before (Around)");

        Object result = pjp.proceed(); // actual method call

        System.out.println("After (Around)");
        return result;
    }
}

