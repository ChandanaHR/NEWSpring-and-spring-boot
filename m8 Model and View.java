// m8g1) Model
//ModelController.java
package com.example.mvc1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

@Controller
public class ModelController {
	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("name","Chandana");
		model.addAttribute("course","Spring and spring boot");
		return "home";
	}
 }
//home.html
<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Home Page</title>
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>
    <h1 th:text ="${name}"></h1>
    <h2 th:text = "${course}"></h2>
</body>
</html>

 // Passing object using Model
  //Student.java
  package com.example.mvc1;

public class Student {
	private int id;
	private String name;
	public Student(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
//Modelcontroller.javaa
@GetMapping("/student")
	public String studentdetails(Model model) {
		Student s1 = new Student(101, "Ravi");
		model.addAttribute("student", s1);
		return "home";
	}

//home.html
<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Home Page</title>
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>
    <h1 th:text ="${name}"></h1>
    <h2 th:text = "${course}"></h2>
    
    <h1 th:text = "${student.id}"></h1>
    <h2 th:text = "${student.name}"></h2>
</body>
</html>

//Passing list using model
	@GetMapping("/students")
public String getStudents(Model model) {

    List<String> names = Arrays.asList("A", "B", "C");

    model.addAttribute("names", names);

    return "students";
}
//home.html
<ul>
  <li th:each="n : ${names}" th:text="${n}"></li>
</ul>
