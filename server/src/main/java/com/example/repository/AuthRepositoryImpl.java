package com.example.repository;

public class AuthRepositoryImpl implements AuthRepository{
    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return null;
    }

    @Override
    public boolean saveUser(String login, String password, String nickname) {
        return false;
    }
}
