//Maincontroller.java
package com.example.mvc1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Maincontroller {
	@GetMapping("/home")
	public String home1() {
		return "home";
	}
}

// under src/main/resources
//home.html
<!DOCTYPE html>
<html>
<head>
    <title>Home Page</title>
</head>
<body>
    <h1>Welcome to Spring Boot</h1>
</body>
</html>

  In browser: http://localhost:8080/home

under src/main/resources/static/css folder/style.css
  body {
	background-color: black;
	color: white;
}
In html file: link css: <link rel="stylesheet" href="/css/style.css">

If using thymeleaf
  home.html
  <!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Home</title>

    <!-- Link CSS -->
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>

    <h1>Welcome to Spring Boot</h1>

</body>
</html>
