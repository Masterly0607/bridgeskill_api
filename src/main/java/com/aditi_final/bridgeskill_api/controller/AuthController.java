package com.aditi_final.bridgeskill_api.controller;

import com.aditi_final.bridgeskill_api.dto.*;
import com.aditi_final.bridgeskill_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController // @RestController = this class handles REST API requests and returns JSON response
@RequestMapping("/api/auth")  // @RequestMapping = This sets the base URL for all methods inside this controller.
public class AuthController {

    @Autowired //  @Autowired = Spring injects AuthService into this controller.
    private AuthService authService;

    // @RequestBody RegisterRequest request = Means Spring takes JSON from frontend request body and converts it into a Java object.
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request)); // authService.register(request) = Controller does not do the business logic itself. It sends the work to AuthService.
        // .ok(...) = return 200 success with data as body
        //  ResponseEntity(built-in Spring class) = It helps you return a full HTTP response, including: status code, response body, headers if needed.
        // <RegisterResponse> = This is the generic type. It means the response body will contain a RegisterResponse object.
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(authService.me(email));
    }

}
// + Main goal of controller = handle HTTP requests and responses.(It is the layer that talks with the client/frontend.)
// + What controller usually does?
// receive request from frontend
// get data from @RequestBody, @PathVariable, @RequestParam
// call service
// return response
// + Main goal of AuthController = receive auth API requests from frontend, send them to AuthService, and return the result back to frontend