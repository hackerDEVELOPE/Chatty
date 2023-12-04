package com.example.repository;

import com.example.JDBC;

import java.sql.ResultSet;

import static com.example.JDBC.*;

public class AuthRepositoryImpl implements AuthRepository{
    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            JDBC.connect();
            ResultSet rs = stmt.executeQuery("SELECT login, password, nickname FROM users");
            while (rs.next()){
                if (rs.getString("login").equals(login) &&
                        rs.getString("password").equals(password)){
                    return rs.getString("nickname");
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.disconnect();
        }
        return null;
    }

    @Override
    public boolean saveUser(String login, String password, String nickname) {
        try {
            JDBC.connect();
            ResultSet rs = rs = stmt.executeQuery("SELECT login, password, nickname FROM users");
            while (rs.next()){
                if (rs.getString("login").equals(login) ||
                        rs.getString("nickname").equals(nickname)){
                    return false;
                }
            }
            JDBC.prepareStatement();
            psInsert.setString(1, login);
            psInsert.setString(2, password);
            psInsert.setString(3, nickname);
            psInsert.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.disconnect();
        }
        return false;

    }
}
