package com.aditi_final.bridgeskill_api.service;

import com.aditi_final.bridgeskill_api.dto.*;
import com.aditi_final.bridgeskill_api.entity.User;
import com.aditi_final.bridgeskill_api.repository.UserRepository;
import com.aditi_final.bridgeskill_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // @Service = this class is a service class. So Spring manages it as a bean, and other classes can use it.
public class AuthService {

    @Autowired
    private UserRepository userRepository; // UserRepository = Used to talk to database such as check if email exists, save user, and find user by email.

    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoder = Used to encrypt password before saving.

    @Autowired
    private JwtUtil jwtUtil; // JwtUtil = Used to generate JWT token.

    @Autowired
    private AuthenticationManager authenticationManager; // Used to authenticate login email/password.(Come from Spring Security)

    // public RegisterResponse register(RegisterRequest request) = this method is public, its name is register, it accepts a RegisterRequest, and it returns a RegisterResponse
    // RegisterResponse = is a return type.(return type = what kind of data this method will return(output))
    public RegisterResponse register(RegisterRequest request) {
        // Validate business rules = Before creating user, service checks business rule: email must be unique
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Validate selected role = public register can only choose CLIENT or STUDENT
        // Example role mapping:
        // 1L = ADMIN
        // 2L = CLIENT
        // 3L = STUDENT
        if (request.getRoleId() == null || (!request.getRoleId().equals(2L) && !request.getRoleId().equals(3L))) {
            throw new RuntimeException("Invalid role selected");
        }

        // + why do we need this step in register logic?
        // Main reason = request is only the input data from frontend. But userRepository.save(...) needs a real User entity object. So this step converts: request data → into a User entity. Without this, there is no User object to save.
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleId(request.getRoleId()) // Option 2 = role comes from request, but backend validates it first
                .build();

        userRepository.save(user); // Service sends processed user object to repository to save in database. This is calling repository.

        return new RegisterResponse("User registered successfully"); // After saving succeeds, service creates response object and returns it to controller.
        // + what is new keyword? create a new object from a class. create one new RegisterResponse object and put "User registered successfully" inside it
        // + return new RegisterResponse("User registered successfully") = create one new response object. send it back as the result of this method
        // 1. creates an object
        // 2. that object has: private String message;
        // 3. constructor puts the value into private String message
        // 4. Spring converts that object to JSON
    }

    public LoginResponse login(LoginRequest request) {
        // Validate / authenticate login = Service asks Spring Security to check: is email correct? and is password correct?
        //  Flow
        // 1. Spring tries to authenticate: authenticationManager.authenticate(...)
        // 2. If email is wrong or password is wrong, Spring throws error immediately: BadCredentialsException
        // 3. Method stops there. So this code never runs: userRepository.findByEmail(...)
        // Why? => For security.
        // Because if system says: "User not found" for wrong email or "Wrong password" for existing email. then attacker can test which emails exist in your system.
        // So standard Spring behavior is: wrong email or wrong password = Bad credentials
        // So we must add @ExceptionHandler(BadCredentialsException.class) for better message like "Invalid email or password"
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        // Call other service/tool = Service uses JwtUtil to create JWT token. This is calling another helper/service class.
        String token = jwtUtil.generateToken(user.getEmail());

        // Return result to controller = Service prepares login response and sends it back to controller.
        return new LoginResponse(
                token,
                user.getFullName(),
                user.getEmail(),
                user.getRoleId()
        );
    }
    public MeResponse me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new MeResponse(
                user.getFullName(),
                user.getEmail(),
                user.getRoleId()
        );
    }
}


// + Main goal of service = store business logic(This is where the real work of the application happens.)
// What service usually does
// validate business rules
// process data
// call repository
// call other services
// return result to controller