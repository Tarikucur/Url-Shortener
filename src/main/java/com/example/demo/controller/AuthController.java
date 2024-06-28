package com.example.demo.controller;

import com.example.demo.controller.auth.AuthenticationRequestBody;
import com.example.demo.controller.auth.AuthenticationResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.AuthenticationFailedException;
import com.example.demo.exception.UserAlreadyExistsException;
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

    @PostMapping(value = "/login",produces = "application/json")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequestBody body) {
        AuthenticationResponse response = authService.authenticate(body);
        if (response == null){
            throw new AuthenticationFailedException("Authentication failed. Invalid credentials.");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registerUser")
    public ResponseEntity register(@RequestBody UserEntity userEntity) {
        userEntity.setId(null);
        if (authService.isEmailDuplicate(userEntity.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if(authService.register(userEntity)){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }
}