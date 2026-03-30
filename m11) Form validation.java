//User.java
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class User {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Min(value = 18, message = "Minimum age is 18")
    @Max(value = 60, message = "Maximum age is 60")
    private int age;

    @Size(min = 6, max = 12, message = "Password must be 6-12 characters")
    private String password;

    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile must be 10 digits")
    private String mobile;

    @Positive(message = "Salary must be positive")
    private double salary;

    @Past(message = "DOB must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotEmpty(message = "Select at least one skill")
    private List<String> skills;

    // getters and setters
}

//Controller(Backend)
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class UserController {

    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result) {

        if (result.hasErrors()) {
            return "register";
        }

        return "success";
    }
}

//Thymeleaf (Frontend) register.html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<body>

<h2>Registration Form</h2>

<form th:action="@{/save}" th:object="${user}" method="post">

    Name:
    <input type="text" th:field="*{name}" />
    <p th:errors="*{name}"></p>

    Email:
    <input type="text" th:field="*{email}" />
    <p th:errors="*{email}"></p>

    Age:
    <input type="number" th:field="*{age}" />
    <p th:errors="*{age}"></p>

    Password:
    <input type="password" th:field="*{password}" />
    <p th:errors="*{password}"></p>

    Mobile:
    <input type="text" th:field="*{mobile}" />
    <p th:errors="*{mobile}"></p>

    Salary:
    <input type="number" th:field="*{salary}" />
    <p th:errors="*{salary}"></p>

    DOB:
    <input type="date" th:field="*{dob}" />
    <p th:errors="*{dob}"></p>

    Gender:
    <input type="radio" th:field="*{gender}" value="Male"/> Male
    <input type="radio" th:field="*{gender}" value="Female"/> Female
    <p th:errors="*{gender}"></p>

    Skills:
    <input type="checkbox" th:field="*{skills}" value="Java"/> Java
    <input type="checkbox" th:field="*{skills}" value="Spring"/> Spring
    <input type="checkbox" th:field="*{skills}" value="SQL"/> SQL
    <p th:errors="*{skills}"></p>

    <button type="submit">Submit</button>

</form>

</body>
</html>
