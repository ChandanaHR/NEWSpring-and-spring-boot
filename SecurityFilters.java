1a) SecurityContextHolderFilter
  Normal java code
  public class LoginDemo {
    public static void main(String[] args) {
        // Step 1: Check the credentials
        String username = "chandana";
        String password = "pass123";
        if (username.equals("chandana")
                && password.equals("pass123")) {
            // Step 2: Remember who logged in
            String loggedInUser = username;
            // Step 3: Use the identity later
            System.out.println(
                "Welcome " + loggedInUser
            );
        }
    }
}
Notice that the program checks the password only in Step 1.
After that, it uses loggedInUser to know who has logged in. It does not need to compare the password again just to display the welcome message.
In a real application, you wouldn't manage authentication this way. This is only to explain the concept.
  First, imagine this situation
You log in to a website.
- Username: chandana
- Password: 1234
The website checks your username and password. They are correct, so you are logged in.
Now you click My Profile.
The website needs to know: Who is viewing this profile?
It already knows that you logged in as chandana. It should not ask you to enter your password again just to display your name.
This is where SecurityContextHolder becomes useful. It lets your application access the identity of the currently authenticated user.
  SecurityConfig.java
  
package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // STEP 1: Configure security rules
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public").permitAll()
                .requestMatchers("/profile").authenticated()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

    // STEP 2: Create a user for learning
    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails user = User.builder()
            .username("student")
            .password(passwordEncoder.encode("pass123"))
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    // STEP 3: Encode passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


1b) UsernamePasswordAuthenticationFilter
  SecurityConfig.java
  
package com.example.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails user = User.builder()
                .username("chandana")
                .password(passwordEncoder.encode("Java@123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/dashboard").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
DashboardController.java
  
package com.example.securitydemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome! You have logged in successfully.";
    }
}
UsernamePasswordAuthenticationFilter	Processes submitted username and password for form login.
AuthenticationManager	Coordinates authentication.
DaoAuthenticationProvider	Checks the user details and submitted password.
SecurityContextHolderFilter	Establishes access to the security context for the current request, loading it through the configured security-context repository.
SecurityContextHolder	Provides access to the current thread's security context.
