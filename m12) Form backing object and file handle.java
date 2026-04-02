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



    //File upload from html form
    <form action="/upload" method="post" enctype="multipart/form-data">
    
    Name: <input type="text" name="name"><br><br>
    
    Select File: <input type="file" name="file"><br><br>
    
    <button type="submit">Upload</button>

</form>

    //Controller
    import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("name") String name,
                             @RequestParam("file") MultipartFile file) {

        System.out.println("Name: " + name);
        System.out.println("File Name: " + file.getOriginalFilename());
        System.out.println("File Size: " + file.getSize());

        return "success";
    }
}
//Save file to local system
@PostMapping("/upload")
public String uploadFile(@RequestParam("file") MultipartFile file) {

    try {
        String path = "C:/uploads/" + file.getOriginalFilename();
        file.transferTo(new File(path));
    } catch (Exception e) {
        e.printStackTrace();
    }

    return "success";
}


//Using Form backing object
//Model class
public class UploadForm {

    private String name;
    private MultipartFile file;

    // getters & setters
}

//Controller
@PostMapping("/upload")
public String upload(@ModelAttribute UploadForm form) {

    MultipartFile file = form.getFile();

    System.out.println(form.getName());
    System.out.println(file.getOriginalFilename());

    return "success";
}

//Thymeleaf form
<form th:action="@{/upload}" th:object="${uploadForm}" 
      method="post" enctype="multipart/form-data">

    Name: <input type="text" th:field="*{name}" /><br>

    File: <input type="file" th:field="*{file}" /><br>

    <button type="submit">Upload</button>

</form>

//In application.properties
    spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=10MB
