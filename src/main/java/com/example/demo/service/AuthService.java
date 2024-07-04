package com.example.demo.service;

import com.example.demo.controller.auth.AuthenticationRequestBody;
import com.example.demo.controller.auth.AuthenticationResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.AuthenticationFailedException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
public class AuthService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(TokenService tokenService,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse authenticate(AuthenticationRequestBody request) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(request.getEmail());

        if (userEntityOptional.isPresent() && passwordEncoder.matches(request.getPassword(), userEntityOptional.get().getPassword())) {
            UserEntity userEntity = userEntityOptional.get();
            return new AuthenticationResponse(
                    tokenService.generateToken(userEntity.getEmail()),
                    userEntity.getName(),
                    userEntity.getEmail(),
                    userEntity.getId()
            );
        } else {
            throw new AuthenticationFailedException("Authentication failed. Invalid credentials.");
        }
    }
    public void register(UserEntity userEntity) {
        if (isEmailDuplicate(userEntity.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
    }

    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}