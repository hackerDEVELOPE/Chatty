package com.example.client;

import constants.Command;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField;
    @FXML
    public MediaView mediaView;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public HBox authPanel;
    @FXML
    public HBox msgPanel;
    @FXML
    public ListView<String> clientList;

    private Socket socket;
    private final static int PORT = 8189;
    private final static String ADDRESS = "localhost";
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isAuthenticated;
    private String nickname;
    private Stage stage;
    private Stage regStage;
    private RegController regController;
    private RandomAccessFile raf;
    private String login;

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        msgPanel.setVisible(authenticated);
        msgPanel.setManaged(authenticated);
        clientList.setVisible(authenticated);
        clientList.setManaged(authenticated);


        if (!authenticated) {
            nickname = "";
        }
        setTitle(nickname);

        textArea.clear();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) textField.getScene().getWindow();
            stage.setOnCloseRequest(windowEvent -> {
                System.out.println("bye");
                if (socket != null && !socket.isClosed()) {
                    try {
                        out.writeUTF(Command.END);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        setAuthenticated(false);
    }

    private void connect() {
        try {
            socket = new Socket(ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {

                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {

                            if (str.equals(Command.END)) {
                                break;
                            }
                            if (str.startsWith(Command.AUTH_OK)) {
                                nickname = str.split(" ")[1];
                                //
                                login = str.split(" ")[2];
                                //
                                setAuthenticated(true);
                                break;
                            }
                            if (str.equals(Command.REG_OK) || str.equals(Command.REG_FAIL)) {
                                regController.result(str);
                            }

                        } else {
                            textArea.appendText(str + "\n");
                        }
                    }
                    raf = new RandomAccessFile("client/src/main/resources/history/history_"+login+".txt", "rw");
//                    raf = new RandomAccessFile("client/src/main/resources/test.txt", "rw");

                    //цикл работы
                    while (isAuthenticated) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals(Command.END)) {
                                break;
                            }
                            if (str.startsWith(Command.CLIENTLIST)) {
                                String[] token = str.split(" ");
                                Platform.runLater(() -> {
                                    clientList.getItems().clear();
                                    for (int i = 1; i < token.length; i++) {
                                        clientList.getItems().add(token[i]);
                                    }
                                });
                            }

                            if (str.startsWith("/yournickis ")) {
                                nickname = str.split(" ")[1];
                                setTitle(nickname);
                            }
                        } else {
                            textArea.appendText(str + "\n");
                            raf.writeBytes(str + "\n");
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    setAuthenticated(false);
                    try {
                        socket.close();
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enterMsg(ActionEvent actionEvent) {

        try {
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (mediaView.getMediaPlayer() == null) {
            try {
                String fileName = getClass().getResource("/icq.mp3").toURI().toString();
                Media media = new Media(fileName);
                MediaPlayer player = new MediaPlayer(media);
                mediaView.setMediaPlayer(player);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        mediaView.getMediaPlayer().seek(mediaView.getMediaPlayer().getStartTime());
        mediaView.getMediaPlayer().play();


    }

    public void tryToAuth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        String msg = String.format(Command.AUTH + " %s %s", loginField.getText().trim(), passwordField.getText().trim());
        passwordField.clear();

        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setTitle(String nickname) {
        String title;
        if (nickname.equals("")) {
            title = "WHATSAPP 2";
        } else {
            title = String.format("WHATSAPP 2 - [%s]", nickname);
        }
        Platform.runLater(() -> {
            stage.setTitle(title);
        });
    }

    public void clientListMouseAction(MouseEvent mouseEvent) {
        String receiver = clientList.getSelectionModel().getSelectedItem();
        textField.setText(String.format("/w %s ", receiver));
    }

    private void createRegStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/reg.fxml"));
            Parent root = fxmlLoader.load();

            regStage = new Stage();

            regStage.setTitle("WHATSAPP 2 REGISTRATION");
            regStage.setScene(new Scene(root, 600, 500));

            regController = fxmlLoader.getController();
            regController.setController(this);

            regStage.initStyle(StageStyle.UTILITY);
            regStage.initModality(Modality.APPLICATION_MODAL);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void tryToReg(ActionEvent actionEvent) {
        if (regStage == null) {
            createRegStage();
        }

        regStage.show();
    }

    public void registration(String login, String password, String nickname) {
        String msg = String.format(Command.REG + " %s %s %s", login, password, nickname);

        if (socket == null || socket.isClosed()) {
            connect();
        }

        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}