package com.example.demo.controller.auth;

public class AuthenticationResponse {
    private String token;
    private String name;
    private String identifier;
    private Long userId;

    public AuthenticationResponse(String token, String name, String identifier, Long userId) {
        this.token = token;
        this.name = name;
        this.identifier = identifier;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}