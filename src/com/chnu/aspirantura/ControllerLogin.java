package com.chnu.aspirantura;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by fibs on 04.02.2018.
 */

public class ControllerLogin{

    @FXML  TextField textField_login;
    @FXML  TextField textField_password;
    @FXML  Button btn_login;
    @FXML  Label labelError;

    public static String login;
    private String password;

    public void initialize(){

    }


    public void login(ActionEvent event){
        login = textField_login.getText();
        password = textField_password.getText();
        int result = SqlCommander.login(login,password);
        if (result==1){
            password=null;
            ((Node) (event.getSource())).getScene().getWindow().hide();
            try {
                Main.loadMainView(this.getClass(), new Stage());
            } catch (IOException e) {
                e.printStackTrace();
                SqlCommander.writeDbLog(e,e.getMessage());
            }
        }
        else if (result==-1) {
            labelError.setText("Невірний пароль!");
        }else if(result==-2){
            labelError.setText("Неможливо з'єднатися з сервером!");
        }
        else{
            labelError.setText("Користувача не знайдено!");
        }
    }
}
