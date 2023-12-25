package com.example.service;

import com.example.JDBC;
import com.example.repository.AuthRepository;
import com.example.repository.AuthRepositoryImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthServiceImpl implements com.example.AuthService {
    private final AuthRepository authRepository;

    public AuthServiceImpl() {
        this.authRepository = new AuthRepositoryImpl();
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return authRepository.getNicknameByLoginAndPassword(login, password);
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        return authRepository.saveUser(login, password, nickname);
    }

    @Override
    public boolean changeNick(String oldNick, String newNick) {
        return authRepository.changeNick(oldNick, newNick);
    }
}
