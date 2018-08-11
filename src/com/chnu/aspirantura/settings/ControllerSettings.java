package com.chnu.aspirantura.settings;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControllerSettings {
    @FXML
    public Label title;

    public void apply(){

        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }
}
