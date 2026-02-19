GET: 
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/users")
    public String getUsers() {
        return "User List";
    }
}
http://localhost:8080/api/users

POST: REQUEST
    Create User.java in eclipse
    package com.example.mvc1;

public class User {
	private String name;
	private String email;
	
	public User() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}

