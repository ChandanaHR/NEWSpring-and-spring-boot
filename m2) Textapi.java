//Maincontroller.java
package com.example.mvc1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Maincontroller {
	@GetMapping("/home")
	public String home1() {
		return "home";
	}
}

// Returns JSON object
//Maincontroller.java
@RestController
public class UserController {

    @GetMapping("/user")
    public User getUser() {
        return new User(1, "Chandana");
    }
}
//User.java
public class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
