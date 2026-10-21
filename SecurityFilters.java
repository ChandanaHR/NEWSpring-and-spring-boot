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


1c) AuthorizationFilter
  SecurityConfig.java
  package com.example.Secondproject;

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
            PasswordEncoder encoder) {

        UserDetails customer = User.builder()
                .username("chandana")
                .password(encoder.encode("User@123"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("Admin@123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(customer, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/home").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/dashboard").authenticated()
                .anyRequest().denyAll()
            )
            .formLogin(Customizer.withDefaults());

        return http.build();
    }
}


Controller.java
  
package com.example.securitydemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/home")
    public String home() {
        return "Welcome! Anyone can access this page.";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome to your user dashboard.";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Welcome to the admin dashboard.";
    }
}


Final example ----------------------Final example--------------------------
	a) User.java
	package com.example.authdemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "ROLE_USER";

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

b) Userrepository.java
	package com.example.authdemo.repository;

import com.example.authdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

c) Registrationservice.java
	package com.example.authdemo.service;

import com.example.authdemo.entity.User;
import com.example.authdemo.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String rawPassword) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(rawPassword));
        user.setRole("ROLE_USER");

        return userRepository.save(user);
    }
}

d)AuthController.java
	package com.example.authdemo.controller;

import com.example.authdemo.entity.User;
import com.example.authdemo.service.RegistrationService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(
            RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        try {
            User user = registrationService.register(
                    request.email(),
                    request.password());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Registration successful",
                            "id", user.getId(),
                            "email", user.getEmail()
                    ));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", ex.getMessage()
                    ));
        }
    }

    @GetMapping("/user/profile")
    public Map<String, String> profile(
            Authentication authentication) {

        return Map.of(
                "message", "You are authenticated",
                "email", authentication.getName()
        );
    }

    @GetMapping("/public")
    public Map<String, String> publicEndpoint() {
        return Map.of("message", "Public endpoint");
    }

    public record RegisterRequest(
            String email,
            String password) {
    }
}

e) SecurityConfig.java
	package com.example.authdemo.config;

import com.example.authdemo.service.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CsrfTokenRepository csrfTokenRepository,
            CustomUserDetailsService userDetailsService)
            throws Exception {

        http
            // 1. CSRF protection
            .csrf(csrf -> csrf
                .csrfTokenRepository(csrfTokenRepository)
                .csrfTokenRequestHandler(
                    new CsrfTokenRequestAttributeHandler())
            )

            // 2. Load user details from our database
            .userDetailsService(userDetailsService)

            // 3. Authentication configuration
            .formLogin(form -> form
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")

                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(
                        "{\"message\":\"Login successful\"}");
                })

                .failureHandler((request, response, exception) -> {
                    response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(
                        "{\"error\":\"Invalid email or password\"}");
                })
            )

            // 4. Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/csrf",
                    "/register",
                    "/login",
                    "/public"
                ).permitAll()

                .requestMatchers("/admin/**")
                    .hasRole("ADMIN")

                .requestMatchers("/user/**")
                    .authenticated()

                .anyRequest()
                    .authenticated()
            )

            // 5. Handle unauthenticated and forbidden requests
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(
                    (request, response, exception) -> {
                        response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            "Please log in first");
                    }
                )
                .accessDeniedHandler(
                    (request, response, exception) -> {
                        response.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "You do not have permission");
                    }
                )
            );

        return http.build();
    }
}

d) CustomUserDetailsService.java
	package com.example.authdemo.service;

import com.example.authdemo.entity.User;
import com.example.authdemo.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
    }
}




