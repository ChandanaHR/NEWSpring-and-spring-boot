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


// m82G) modelMap
@Controller
public class HomeController {

    @GetMapping("/about")
    public String aboutPage(ModelMap modelMap) {

        modelMap.addAttribute("name", "Chandana");
        modelMap.addAttribute("city", "Bangalore");

        return "about";
    }
}
//about.html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<body>

<h1 th:text="${name}"></h1>
<p th:text="${city}"></p>

</body>
</html>

	//Passing object example
	// Student.java
	public class Student {
    private int id;
    private String name;

    // constructor + getters
}
//Controller
@GetMapping("/student")
public String studentPage(ModelMap modelMap) {

    Student s = new Student(1, "Rahul");

    modelMap.addAttribute("student", s);

    return "student";
}
//student.html
<h2 th:text="${student.id}"></h2>
<h3 th:text="${student.name}"></h3>

	//Passing Lists example
	@GetMapping("/students")
public String students(ModelMap modelMap) {

    List<String> names = Arrays.asList("A", "B", "C");

    modelMap.addAttribute("names", names);

    return "students";
}
//students.html
<ul>
  <li th:each="n : ${names}" th:text="${n}"></li>
</ul>
//home.html
<ul>
  <li th:each="n : ${names}" th:text="${n}"></li>
</ul>

	m83G) ModelandView
//ModelController.java
	package com.example.mvc1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

@Controller
public class ModelController {
	 @GetMapping("/home")
	    public ModelAndView homePage() {

	        ModelAndView mv = new ModelAndView();

	        mv.addObject("name", "Chandana");
	        mv.addObject("course", "Spring MVC");

	        mv.setViewName("home");

	        return mv;
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

	//Passing Object Example
	//Student.java
public class Student {

    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
//StudentController.java
@GetMapping("/student")
public ModelAndView studentPage() {

    ModelAndView mv = new ModelAndView();

    Student s = new Student(1, "Rahul");

    mv.addObject("student", s);

    mv.setViewName("student");

    return mv;
}
//student.html
<h2 th:text="${student.id}"></h2>
<h3 th:text="${student.name}"></h3>

	//Passing List example
	@GetMapping("/students")
public ModelAndView studentsPage() {

    ModelAndView mv = new ModelAndView();

    List<String> list = Arrays.asList("A", "B", "C");

    mv.addObject("names", list);

    mv.setViewName("students");

    return mv;
}
students.html
	<ul>
<li th:each="n : ${names}" th:text="${n}"></li>
</ul>
