// Form backing object
//User.java
public class User {
    private String name;
    private String email;
    private int age;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
// Controller class
@Controller
public class UserController {

    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("user", new User()); // form backing object
        return "register";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getAge());
        return "success";
    }
}
//HTML Form(JSP or Thymeleaf)
<form th:action="@{/save}" th:object="${user}" method="post">
    
    Name: <input type="text" th:field="*{name}" /><br>
    Email: <input type="text" th:field="*{email}" /><br>
    Age: <input type="number" th:field="*{age}" /><br>

    <button type="submit">Submit</button>
</form>
