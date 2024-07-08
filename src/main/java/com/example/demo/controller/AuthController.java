package com.example.demo.controller;

import com.example.demo.controller.auth.AuthenticationRequestBody;
import com.example.demo.controller.auth.AuthenticationResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.AuthenticationFailedException;
import com.example.demo.request.UserRegistrationRequest;
import com.example.demo.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestBody body) {
        try {
            AuthenticationResponse response = authService.authenticate(body);
            return ResponseEntity.ok(response);
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/registerUser")
    public ResponseEntity<Void> register(@RequestBody UserRegistrationRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setIdentifier(request.getIdentifier());
        userEntity.setPassword(request.getPassword());
        userEntity.setName(request.getName());
        userEntity.setAccountType(request.getAccountType());

        authService.register(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}