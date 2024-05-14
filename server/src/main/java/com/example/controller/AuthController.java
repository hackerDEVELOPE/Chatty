package com.example.controller;

import com.example.AuthService;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public boolean regUser(String login, String password, String nickname){
        return authService.registration(login, password, nickname);
    }
}
