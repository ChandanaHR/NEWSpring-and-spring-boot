// m91) Creating a simple form
//Formcontroller.java
package com.example.mvc1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Formcontroller {
	@GetMapping("/getform")
	public String getform() {
		return "form";
	}
	@PostMapping("/submitForm")
	public String handleform(@RequestParam("name") String name, @RequestParam("email") String email) {
		System.out.println("Name is" +name);
		System.out.println("Email is" +email);
		return "success";
		}
}
//form.html
<!DOCTYPE html>
<html>
<head>
<title>User Form</title>
</head>

<body>

<h2>User Registration Form</h2>

<form action="/submitForm" method="post">

Name:
<input type="text" name="name"><br><br>

Email:
<input type="email" name="email"><br><br>

<button type="submit">Submit</button>

</form>

</body>
</html>
  //success.html
  <!DOCTYPE html>
<html>
<body>


	// m92g) Data Binding
	// Binding Form Data to Java Objects
	// Create student model class
	public class Student {

    private String name;
    private int age;

    // getters and setters
}
//HTML Form(Thymeleaf)
	<form action="/saveStudent" method="post">
    Name: <input type="text" name="name"><br>
    Age: <input type="text" name="age"><br>
    <button type="submit">Submit</button>
</form>
	//Controller class
	@PostMapping("/saveStudent")
public String saveStudent(Student student) {

    System.out.println(student.getName());
    System.out.println(student.getAge());

    return "result";
}

//Binding request parameters
// URL: We can bind individual parameters using @RequestParam.
	http://localhost:8080/login?username=admin&password=123
	@GetMapping("/login")
public String login(
        @RequestParam String username,
        @RequestParam String password) {

    System.out.println(username);
    System.out.println(password);

    return "home";
}
// This is parameter level binding

// Binding nested objects
//Address class
	public class Address {

    private String city;
    private String state;

    // getters setters
}
//Student class
public class Student {

    private String name;
    private Address address;

    // getters setters
}
html code
	<form action="/save" method="post">
Name: <input type="text" name="name"><br>
City: <input type="text" name="address.city"><br>
State: <input type="text" name="address.state"><br>
<button>Submit</button>
</form>
//Controller
	@PostMapping("/save")
public String save(Student student) {

    System.out.println(student.getName());
    System.out.println(student.getAddress().getCity());
    System.out.println(student.getAddress().getState());

    return "result";
}

//Binding collections(List/Array)
public class Student {

    private String name;
    private List<String> skills;

    // getters setters
}
<form action="/saveStudent" method="post">

Name: <input type="text" name="name"><br>

Skills:
<input type="checkbox" name="skills" value="Java">Java
<input type="checkbox" name="skills" value="Python">Python
<input type="checkbox" name="skills" value="React">React

<button>Submit</button>

</form>
@PostMapping("/saveStudent")
public String saveStudent(Student student) {

    System.out.println(student.getName());
    System.out.println(student.getSkills());

    return "result";
}
