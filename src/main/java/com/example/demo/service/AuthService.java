package com.example.demo.service;

import com.example.demo.controller.auth.AuthenticationRequestBody;
import com.example.demo.controller.auth.AuthenticationResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.AccountType;
import com.example.demo.exception.AuthenticationFailedException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    @Autowired
    public AuthService(TokenService tokenService,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       NotificationService notificationService) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }

    public AuthenticationResponse authenticate(AuthenticationRequestBody request) {
        Optional<UserEntity> userEntityOptional = userRepository.findByIdentifier(request.getIdentifier());

        if (userEntityOptional.isPresent() && passwordEncoder.matches(request.getPassword(), userEntityOptional.get().getPassword())) {
            UserEntity userEntity = userEntityOptional.get();
            return new AuthenticationResponse(
                    tokenService.generateToken(userEntity.getIdentifier()),
                    userEntity.getName(),
                    userEntity.getIdentifier(),
                    userEntity.getId()
            );
        } else {
            throw new AuthenticationFailedException("Authentication failed. Invalid credentials.");
        }
    }
    public void register(UserEntity userEntity) {
        if (isIdentifierDuplicate(userEntity.getIdentifier(), userEntity.getAccountType())) {
            String message = userEntity.getAccountType() == AccountType.EMAIL ? "Email already exists" : "Phone number already exists";
            throw new UserAlreadyExistsException(message);
        }
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);


        notificationService.sendWelcomeNotification(userEntity.getIdentifier(), userEntity.getAccountType());
    }

    private boolean isIdentifierDuplicate(String identifier, AccountType accountType) {
        return userRepository.findByAccountTypeAndIdentifier(accountType, identifier).isPresent();
    }
}