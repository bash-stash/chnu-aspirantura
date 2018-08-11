package com.chnu.aspirantura.faculty;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Created by fibs on 04.02.2018.
 */

public class ControllerFormAddFaculty {

    @FXML
    TextField textName;

    public void initialize() {

    }

    public void apply() {
        //TODO check input param
        SqlCommander.addNewFaculty(textName.getText());
        ControllerFaculty.addedNew = true;
        Stage stage = (Stage) textName.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) textName.getScene().getWindow();
        stage.close();
    }
}
