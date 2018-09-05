package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ControllerMark {

    static String markPo;
    static String markNa;
    static String markIE;

    static int id;

    @FXML private TextField markPoints;
    @FXML private ComboBox<String> markNational;
    @FXML private ComboBox<String> markIECTIS;


    public void initialize(){
        if (markPo !=null) markPoints.setText(markPo);
        if (markNa !=null)  markNational.setValue(markNa);
        if (markIE !=null)  markIECTIS.setValue(markIE);
    }

    public void apply(){
        markPo = markPoints.getText();
        markIE = markIECTIS.getValue();
        markNa = markNational.getValue();

        int points  = Integer.parseInt(markPo);

        SqlCommander.editMark(id,points,markNa,markIE);
        ControllerShowResults.changed= true;
        cancel();
    }

    public void cancel() {
        Stage stage = (Stage) markPoints.getScene().getWindow();
        stage.close();
    }
}
