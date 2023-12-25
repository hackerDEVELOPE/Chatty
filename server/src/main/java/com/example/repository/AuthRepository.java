package com.example.repository;

public interface AuthRepository {
    String getNicknameByLoginAndPassword(String login, String password);
    boolean saveUser (String login,String password,String nickname);

    boolean changeNick(String oldNickname, String newNickname);
}
