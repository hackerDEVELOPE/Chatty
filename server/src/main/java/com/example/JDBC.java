package com.example;

import java.sql.*;

public class JDBC {
    private static Connection connection;
    public static Statement stmt;
    public static PreparedStatement psRegistration;
    public static PreparedStatement psGetNickname;
    public static PreparedStatement psChangeNick;

    public static boolean regUser(String login, String password, String nickname) throws SQLException {
        ResultSet rs = select();
        while (rs.next()){
            if (rs.getString("login").equals(login) ||
                    rs.getString("nickname").equals(nickname)){
                return false;
            }
        }
        stmt.executeUpdate("INSERT INTO users (login, password, nickname) VALUES (login, password, nickname)");
        return true;
    }
    public  static String getNicknameByLoginAndPassword (String login, String password){
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
        return null;
    }
    public static ResultSet select() throws SQLException {
        ResultSet rs;
        return rs = stmt.executeQuery("SELECT login, password FROM users");
    }

    public static void prepareStatement() throws SQLException {

        psGetNickname = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ? ");
        psRegistration = connection.prepareStatement("INSERT INTO users (login, password, nickname) VALUES (?, ?, ?)");
        psChangeNick = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?");
    }

    public static boolean connect() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chatty.db");
            stmt = connection.createStatement();
            prepareStatement();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    public static void disconnect(){
        try {
            stmt.close();
            psRegistration.close();
            psGetNickname.close();
            psChangeNick.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
