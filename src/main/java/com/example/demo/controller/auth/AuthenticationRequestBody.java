package com.example.demo.controller.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationRequestBody {
    @JsonProperty("email")
    public String email;
    @JsonProperty("password")
    public String password;
}