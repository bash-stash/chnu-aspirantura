package com.chnu.aspirantura.nakaz;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Created by fibs on 12.02.2018.
 */

public class ControllerShowInfoNakaz {
    public static int nakazId;
    public static ObjectNakaz nakaz;

    @FXML Label header;
    @FXML Label dateLabel;
    @FXML TextArea textArea;

    public void initialize(){
        nakaz = SqlCommander.getNakaz(nakazId);
        header.setText("Вміст наказу "+nakaz.getNumber() +" про " + nakaz.getType().toLowerCase());
        dateLabel.setText(nakaz.getDate());
        textArea.setText(nakaz.getAbout());
    }


    public void close() {
        Stage stage = (Stage) header.getScene().getWindow();
        stage.close();
    }
}
