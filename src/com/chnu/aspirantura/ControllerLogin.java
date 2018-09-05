package com.chnu.aspirantura;

import com.chnu.aspirantura.backup.BackupExecutor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ControllerLogin {

    @FXML
    TextField textField_login;
    @FXML
    TextField textField_password;
    @FXML
    Button btn_login;
    @FXML
    Label labelError;

    public static String login;
    private String password;


    public void initialize(){
        Settings.loadDatabaseProperties();
        Settings.loadProperties();
        int day = Integer.parseInt(Settings.getProperty("backup.day"));

        if (LocalDate.now().getDayOfMonth()!=day){
            try {
                BackupExecutor.makeBackup();
                Settings.setSettings("backup.day", String.valueOf(LocalDate.now().getDayOfMonth()));
            }catch (Exception e){
                Utils.logger.error(e.getMessage());
            }
        }
    }


    public void login(ActionEvent event) {
        login = textField_login.getText();
        password = textField_password.getText();
        int result = SqlCommander.login(login, password);
        if (result == 1) {
            password = null;
            ((Node) (event.getSource())).getScene().getWindow().hide();
            try {
                Main.loadMainView(this.getClass(), new Stage());
            } catch (IOException e) {
                Utils.logger.error("Error while logging in: " + e.getMessage());
            }
        } else if (result == -1) {
            labelError.setText("Невірний пароль!");
        } else if (result == -2) {
            labelError.setText("Неможливо з'єднатися з сервером!");
        } else {
            labelError.setText("Користувача не знайдено!");
        }
    }
}
