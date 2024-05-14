package com.example;

public class DatabaseAuthService implements AuthService{
    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        return false;
    }

    @Override
    public boolean changeNick(String oldNick, String newNick) {
        return false;
    }
}
