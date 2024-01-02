package com.example;


import constants.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isAuthenticated;
    private String nickname;
    private String login;
    private ExecutorService service;

    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    LogManager manager = LogManager.getLogManager();


    public ClientHandler(Server server, Socket socket, ExecutorService service) {
        try {
            manager.readConfiguration(new FileInputStream("server/logging.properties"));

            this.server = server;
            this.socket = socket;
            this.service = service;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());


//            new Thread(() -> {
            service.execute(() -> {

//                System.out.println(Thread.currentThread().getName());

                try {
                    socket.setSoTimeout(5000);
                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {

                            if (str.equals(Command.END)) {
                                sendMsg(Command.END);
                                break;
                            }

                            if (str.startsWith(Command.AUTH)) {
                                String[] token = str.split(" ", 3);
                                if (token.length < 3) {
                                    continue;
                                }
                                String newNick = server.getAuthService()
                                        .getNicknameByLoginAndPassword(token[1], token[2]);
                                login = token[1];
                                if (newNick != null) {
                                    if (!server.isAccAuthenticated(login)) {
                                        nickname = newNick;
                                        isAuthenticated = true;
                                        sendMsg(Command.AUTH_OK + " " + nickname + " " + login);

                                        logger.log(Level.FINE, nickname + " have been authed");

                                        server.subscribe(this);
                                        socket.setSoTimeout(0);
                                        break;
                                    } else {
                                        sendMsg("Аккаунт уже авторизован");
                                    }

                                } else {
                                    sendMsg("invalid login or password");
                                }

                            }
                            if (str.startsWith(Command.REG)) {
                                String[] token = str.split(" ");
                                if (token.length < 4) {
                                    continue;
                                }
                                if (server.getAuthService()
                                        .registration(token[1], token[2], token[3])) {

                                    sendMsg(Command.REG_OK);

                                    logger.log(Level.FINE, token[3] + " have been registrated");
                                } else {
                                    sendMsg(Command.REG_FAIL);
                                }
                            }
                        }


                    }

                    //цикл работы
                    while (isAuthenticated) {
//                        socket.setSoTimeout(0);
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals(Command.END)) {
                                break;
                            }
                            if (str.startsWith(Command.W)) {
                                String[] token = str.split(" ", 3);
                                if (token.length < 3) {
                                    continue;
                                }
                                server.privateMsg(this, token[1], token[2]);
                            }
                            if (str.startsWith(Command.NICK)) {
                                String[] token = str.split("\\s+", 2);
                                if (token.length < 2) {
                                    continue;
                                }
                                if (token[1].contains(" ")) {
                                    sendMsg("Nickname can't have spaces");
                                    continue;
                                }
                                if (server.getAuthService().changeNick(this.nickname, token[1])) {
                                    sendMsg("/yournickis " + token[1]);
                                    sendMsg("Your nickname changed to " + token[1]);
                                    this.nickname = token[1];
                                    server.broadcastClientList();
                                    logger.log(Level.FINE, this.nickname + " changed nickname");
                                } else {
                                    sendMsg("Nickname " + token[1] + " already taken");
                                }
                            }
                        } else {
                            server.broadcastMsg(this, str);
                        }


                    }
                } catch (SocketTimeoutException e) {
                    sendMsg(Command.END);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(this);
                    logger.log(Level.FINE, this.nickname + " was disconnected");
//                    System.out.println("Client was disconnected");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Exception", e);
                        e.printStackTrace();
                    }
                }
//            }).start();
            });
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception", e);
            e.printStackTrace();
        }

    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }
}
