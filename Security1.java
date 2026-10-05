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
