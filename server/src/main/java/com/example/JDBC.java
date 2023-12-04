package com.example;

import java.sql.*;

public class JDBC {
    private static Connection connection;
    public static Statement stmt;
    public static PreparedStatement psInsert;




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

    public static String getNicknameByLoginAndPassword (String login, String password) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT login, password FROM users");
        while (rs.next()){
            if (rs.getString("login").equals(login) &&
                    rs.getString("password").equals(password)){
                return rs.getString("nickname");
            }
        }
        return null;
    }
    public static ResultSet select() throws SQLException {
        ResultSet rs;
        return rs = stmt.executeQuery("SELECT login, password FROM users");
    }
    // rs close??

    public static void prepareStatement() throws SQLException {
        psInsert = connection.prepareStatement("INSERT INTO users (login, password, nickname) VALUES (?, ?, ?)");
    }

    public static void connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:whatsApp.db");
        stmt = connection.createStatement();
    }
    public static void disconnect(){
        try {
            stmt.close();
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
