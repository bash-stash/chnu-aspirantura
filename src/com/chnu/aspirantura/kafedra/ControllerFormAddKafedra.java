package com.chnu.aspirantura.kafedra;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerFormAddKafedra {

    @FXML
    TextField nameKafedra;
    @FXML
    TextField roomKafedra;
    public void initialize() {

    }

    public void apply() {

        //TODO check input param
        SqlCommander.addNewKafedra(nameKafedra.getText(),roomKafedra.getText());
        ControllerKafedra.addedNew = true;
        Stage stage = (Stage) nameKafedra.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) nameKafedra.getScene().getWindow();
        stage.close();
    }
}
