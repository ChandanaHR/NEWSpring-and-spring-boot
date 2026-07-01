// URI Versioning
dto's
example : Userv1.java
  package com.example.versioning.dto;

public class UserV1 {

    private int id;
    private String name;

    public UserV1() {
    }

    public UserV1(int id, String name) {
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
Userv2.java
  package com.example.versioning.dto;

public class UserV2 {

    private int id;
    private String firstName;
    private String lastName;
    private String email;

    public UserV2() {
    }

    public UserV2(int id,
                  String firstName,
                  String lastName,
                  String email) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

Service Layer
  package com.example.versioning.service;

import org.springframework.stereotype.Service;

import com.example.versioning.dto.UserV1;
import com.example.versioning.dto.UserV2;

@Service
public class UserService {

    public UserV1 getUserV1() {

        return new UserV1(
                101,
                "Chandana");
    }

    public UserV2 getUserV2() {

        return new UserV2(
                101,
                "Chandana",
                "HR",
                "chandana@gmail.com");
    }

}
Controller class
  package com.example.versioning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.versioning.dto.UserV1;
import com.example.versioning.dto.UserV2;
import com.example.versioning.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // Version 1 API
    @GetMapping("/api/v1/users")
    public UserV1 getUserV1() {

        return userService.getUserV1();
    }

    // Version 2 API
    @GetMapping("/api/v2/users")
    public UserV2 getUserV2() {

        return userService.getUserV2();
    }

}

//Query parameter versioning
Only controller layer modification
  package com.example.versioning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.versioning.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public Object getUser(
            @RequestParam int version) {

        if (version == 1) {
            return userService.getUserV1();
        }

        else if (version == 2) {
            return userService.getUserV2();
        }

        else {
            return "Invalid API Version";
        }

    }

}

// 3. Header versioning
  Controller layer
  package com.example.versioning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.versioning.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public Object getUser(
            @RequestHeader("API-Version") int version) {

        if (version == 1) {
            return userService.getUserV1();
        }

        else if (version == 2) {
            return userService.getUserV2();
        }

        else {
            return "Invalid API Version";
        }
    }
}
// Another way
  package com.example.versioning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.versioning.dto.UserV1;
import com.example.versioning.dto.UserV2;
import com.example.versioning.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/api/users", headers = "API-Version=1")
    public UserV1 getUserV1() {
        return userService.getUserV1();
    }

    @GetMapping(value = "/api/users", headers = "API-Version=2")
    public UserV2 getUserV2() {
        return userService.getUserV2();
    }
}


// 4. Media Type Versioning
  package com.example.versioning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.versioning.dto.UserV1;
import com.example.versioning.dto.UserV2;
import com.example.versioning.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(
            value = "/api/users",
            produces = "application/vnd.company.v1+json")
    public UserV1 getUserV1() {

        return userService.getUserV1();
    }

    @GetMapping(
            value = "/api/users",
            produces = "application/vnd.company.v2+json")
    public UserV2 getUserV2() {

        return userService.getUserV2();
    }

}
