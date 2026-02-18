a) Request mapping at method level
  import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping("/home")
    @ResponseBody
    public String home() {
        return "Welcome Home";
    }
}
http://localhost:8080/home


b) Request mapping at class level
  @RestController
@RequestMapping("/api")
public class UserController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello User";
    }
}
http://localhost:8080/api/hello
