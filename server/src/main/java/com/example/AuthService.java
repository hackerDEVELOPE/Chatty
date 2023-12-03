package com.example;

public interface AuthService {
    /*
    * Метод получения никнейма по логину и паролю
    * @return null если учетная запись не найдена
    * @return nickname если учетная запись найдена
    * */
    String getNicknameByLoginAndPassword(String login, String password);

    // метод для регистрации учетной записи
    // @return true при успешной регистрации
    // @return false если логин или никнейм заняты
    boolean registration (String login,String password,String nickname);
}
