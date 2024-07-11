package com.example.demo;

import com.example.demo.controller.auth.AuthenticationRequestBody;
import com.example.demo.controller.auth.AuthenticationResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.AuthenticationFailedException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AuthServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_withValidCredentials_shouldReturnAuthenticationResponse() {
        String identifier = "user@example.com";
        String password = "password";
        String encodedPassword = "$2a$10$mockedEncodedPassword"; // Mocked encoded password
        Long userId = 1L;

        AuthenticationRequestBody request = new AuthenticationRequestBody();
        request.setIdentifier(identifier);
        request.setPassword(password);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName("User");
        userEntity.setIdentifier(identifier);
        userEntity.setPassword(encodedPassword);

        Mockito.when(userRepository.findByIdentifier(identifier)).thenReturn(Optional.of(userEntity));
        Mockito.when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        Mockito.when(tokenService.generateToken(identifier)).thenReturn("mockedToken");
        AuthenticationResponse response = authService.authenticate(request);
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mockedToken");
        assertThat(response.getName()).isEqualTo("User");
        assertThat(response.getIdentifier()).isEqualTo(identifier);
        assertThat(response.getUserId()).isEqualTo(userId);
    }

    @Test
    void authenticate_withInvalidCredentials_shouldThrowAuthenticationFailedException() {
        String identifier = "user@example.com";
        String password = "wrongpassword";

        AuthenticationRequestBody request = new AuthenticationRequestBody();
        request.setIdentifier(identifier);
        request.setPassword(password);
        Mockito.when(userRepository.findByIdentifier(identifier)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessageContaining("Authentication failed");
    }
}
