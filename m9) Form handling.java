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

<h2>Form Submitted Successfully</h2>

</body>
</html>
