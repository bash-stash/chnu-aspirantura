package com.chnu.aspirantura.vykladach;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class ControllerFormAddVykladach {

    @FXML
    TextField textName;

    public void initialize() {

    }

    public void apply() {
        //TODO check input param
        SqlCommander.addNewVykladach(textName.getText());
        ControllerVykladach.addedNew = true;
        Stage stage = (Stage) textName.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) textName.getScene().getWindow();
        stage.close();
    }
}
