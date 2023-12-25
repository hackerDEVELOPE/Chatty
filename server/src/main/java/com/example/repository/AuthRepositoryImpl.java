package com.example.repository;

import com.example.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.JDBC.*;

public class AuthRepositoryImpl implements AuthRepository{
//    @Override
//    public String getNicknameByLoginAndPassword(String login, String password) {
//        try {
//            //JDBC.connect();
//            ResultSet rs = stmt.executeQuery("SELECT login, password, nickname FROM users");
//            while (rs.next()){
//                if (rs.getString("login").equals(login) &&
//                        rs.getString("password").equals(password)){
//                    return rs.getString("nickname");
//                }
//            }
//            return null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            //JDBC.disconnect();
//        }
//        return null;
//    }
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


//    @Override
//    public boolean saveUser(String login, String password, String nickname) {
//        try {
//            JDBC.connect();
//            ResultSet rs = rs = stmt.executeQuery("SELECT login, password, nickname FROM users");
//            while (rs.next()){
//                if (rs.getString("login").equals(login) ||
//                        rs.getString("nickname").equals(nickname)){
//                    return false;
//                }
//            }
//            JDBC.prepareStatement();
//            psRegistration.setString(1, login);
//            psRegistration.setString(2, password);
//            psRegistration.setString(3, nickname);
//            psRegistration.execute();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            JDBC.disconnect();
//        }
//        return false;
//
//    }

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
