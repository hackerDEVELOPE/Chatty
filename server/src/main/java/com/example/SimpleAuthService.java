package com.example;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService{
    private class UserData{
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }



    private List<UserData> users;

    public SimpleAuthService() {

        users = new ArrayList<>();
        users.add(new UserData("qwe", "qwe", "qwe"));
        users.add(new UserData("asd", "asd", "asd"));


        for (int i = 0; i < 9; i++) {

            users.add(new UserData("user"+ i, "user"+ i, "user"+ i));
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if (user.login.equals(login) && user.password.equals(password)){
                return user.nickname;
            }
        }

        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        for (UserData user : users) {
            if (user.login.equals(login) || user.nickname.equals(nickname)){
                return false;
            }
        }
        users.add(new UserData(login, password, nickname));
        return true;
    }

    @Override
    public boolean changeNick(String oldNick, String newNick) {
        return false;
    }
}
