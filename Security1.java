1a) Database
Create a MySQL database:
CREATE DATABASE security_demo;
Our table will eventually look like:
users
------------------------------------------------
id | username | password                    | role
------------------------------------------------
1  | john     | $2a$10$......                | USER
Notice:
john123
is NOT stored.

1b) Spring Boot dependencies
Create a Spring Boot project with:
Spring Web
Spring Security
Spring Data JPA
MySQL Driver
Lombok
For JWT, we'll also need a JWT library such as JJWT.

1c) application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/security_demo
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

......................................Registration flow..............................................
Frontend:
  import { useState } from "react";

function App() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const registerUser = async (e) => {

        e.preventDefault();

        const user = {
            username: username,
            password: password
        };

        try {

            const response = await fetch(
                "http://localhost:8080/api/auth/register",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(user)
                }
            );

            const message = await response.text();

            alert(message);

        } catch (error) {

            console.error(error);

            alert("Something went wrong");
        }
    };

    return (
        <div>

            <h1>Register</h1>

            <form onSubmit={registerUser}>

                <div>
                    <label>Username</label>

                    <input
                        type="text"
                        value={username}
                        onChange={(e) =>
                            setUsername(e.target.value)
                        }
                    />
                </div>

                <br />

                <div>
                    <label>Password</label>

                    <input
                        type="password"
                        value={password}
                        onChange={(e) =>
                            setPassword(e.target.value)
                        }
                    />
                </div>

                <br />

                <button type="submit">
                    Register
                </button>

            </form>

        </div>
    );
}

export default App;
1d) User entity
  @Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String role;

    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // getters and setters
}

1e) UserRepository
  @Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}


1f)......................SecurityConfig.java

For registration, we need a PasswordEncoder so the password is hashed before storing it in MySQL.

package com.example.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

1h) .....................AuthController
  package com.example.securitydemo.controller;

import com.example.securitydemo.entity.User;
import com.example.securitydemo.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {

        // 1. Check whether username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("Username already exists");
        }

        // 2. Convert plain password into BCrypt hash
        String encodedPassword =
                passwordEncoder.encode(user.getPassword());

        // 3. Replace plain password with encoded password
        user.setPassword(encodedPassword);

        // 4. Set default role
        user.setRole("USER");

        // 5. Save user into MySQL
        userRepository.save(user);

        return ResponseEntity.ok("Registration successful");
    }
}



....................................................Login FLOW...................................
  l1) LoginRequest.java
  package com.example.securitydemo.dto;

public class LoginRequest {

    private String username;

    private String password;

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

l2) UserRepository.java
  We already created this during registration.
  package com.example.securitydemo.repository;

import com.example.securitydemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}


1f).............. UserDetailsService
  Now lets connect our database user to Spring security
  @Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        )
                );

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
The important flow is:

DaoAuthenticationProvider
          ↓
CustomUserDetailsService
          ↓
UserRepository
          ↓
MySQL
          ↓
User
          ↓
UserDetails
Conceptually:
Your User entity
username = john
password = $2a$10$ABC...
role = USER

        ↓
Spring Security UserDetails
username = john
password = $2a$10$ABC...
authorities = ROLE_USER
  Spring Security gave me a username. I will search my database for that username. If I cannot find it, I will 
  throw UsernameNotFoundException. If I find it, I will take the username, stored password hash, and role from
  my database user and convert them into a Spring Security UserDetails object.

1g)..................Security config
  
  
spring.jpa.properties.hibernate.format_sql=true
