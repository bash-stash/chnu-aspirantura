package com.chnu.aspirantura.discipline;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerFormAddNewDiciplineType{

    @FXML
    TextField textName;

    public void initialize() {

    }

    public void apply() {
        //TODO check input param
        SqlCommander.addNewDisciplineType(textName.getText());
        ControllerSettingsDiscipline.addedNew = true;
        Stage stage = (Stage) textName.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) textName.getScene().getWindow();
        stage.close();
    }
}
