package com.example;

import java.io.FileInputStream;
import java.util.logging.LogManager;

public class Main {
    public static void main(String[] args) throws Exception {
        LogManager manager = LogManager.getLogManager();
        manager.readConfiguration(new FileInputStream("server/logging.properties"));

        new Server();
    }

}