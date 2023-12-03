package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class JDBC {
    private static Connection connection;
    public static Statement stmt;
    private static PreparedStatement psInsert;

    public static void main(String[] args) {

    }

    public static void connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:whatsApp.db");
        stmt = connection.createStatement();
    }
}
