package com.example.demo.controller.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {
    @JsonProperty("token")
    public String token;
    @JsonProperty("name")
    public String name;
    @JsonProperty("email")
    public String email;
    @JsonProperty("userId")
    public Long userId;

    public AuthenticationResponse(String token, String name, String email, Long userId){
        this.userId = userId;
        this.token = token;
        this.name = name;
        this.email = email;
    }
}