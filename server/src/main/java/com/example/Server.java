package com.example;

import com.example.service.AuthServiceImpl;
import constants.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;


    private ExecutorService service;


    public Server() throws Exception {

        //
        service = Executors.newCachedThreadPool();
        //
        clients = new CopyOnWriteArrayList<>();
        if (!JDBC.connect()){
            throw new RuntimeException("DB was fallen apart");
        }
        authService = new AuthServiceImpl();
        try {
            server = new ServerSocket(PORT);
            System.out.println("server was started");


            while (true) {
                socket = server.accept();
                System.out.println("client was connected");
                new ClientHandler(this, socket, service);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            JDBC.disconnect();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(ClientHandler sender, String msg) {

        String message = String.format("[%s]: %s", sender.getNickname(), msg);
        for (ClientHandler client : clients) {
            client.sendMsg(message);
        }
    }

    public void privateMsg(ClientHandler sender, String receiver, String msg) {
        String message = String.format("#private# [%s]: %s", sender.getNickname(), msg);
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(receiver)) {
                client.sendMsg(message);
                if(!sender.getNickname().equals(receiver)){
                    sender.sendMsg(message);
                }

                return;
            }

        }
        sender.sendMsg("Пользователь с ником " + receiver + " не найден");
    }

    public boolean isAccAuthenticated(String login){
        for (ClientHandler client : clients) {
            if(client.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }
    public void broadcastClientList() {
        StringBuilder sb = new StringBuilder(Command.CLIENTLIST);
        for (ClientHandler client : clients) {
            sb.append(" ").append(client.getNickname());
        }

        String msg = sb.toString();

        for (ClientHandler client : clients) {
            client.sendMsg(msg);
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public AuthService getAuthService() {
        return authService;
    }
}
