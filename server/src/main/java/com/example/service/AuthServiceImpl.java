package com.example.service;

import com.example.JDBC;
import com.example.repository.AuthRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthServiceImpl implements com.example.AuthService {




    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            return JDBC.getNicknameByLoginAndPassword(login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        try {
            return JDBC.regUser(login, password, nickname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
