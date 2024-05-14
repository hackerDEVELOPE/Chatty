package com.example.repository;


import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.JDBC.*;

public class AuthRepositoryImpl implements AuthRepository{
    public String getNicknameByLoginAndPassword(String login, String password) {
        String nick = null;
        try {
            psGetNickname.setString(1, login);
            psGetNickname.setString(2, password);
            ResultSet rs = psGetNickname.executeQuery();
            if (rs.next()){
                nick = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return nick;
    }

    public boolean saveUser(String login, String password, String nickname) {
        try {
            psRegistration.setString(1, login);
            psRegistration.setString(2, password);
            psRegistration.setString(3, nickname);
            psRegistration.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeNick(String oldNickname, String newNickname){
        try {
            psChangeNick.setString(1, newNickname);
            psChangeNick.setString(2, oldNickname);
            psChangeNick.executeUpdate();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
