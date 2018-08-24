package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ControllerMark {

    static String mark;
    static int id;
    @FXML private TextField markTxt;


    public void initialize(){
        if (mark!=null){
            markTxt.setText(mark);
            markTxt.positionCaret(mark.length());
        }
    }

    public void apply(){
        mark = markTxt.getText();
        SqlCommander.editMark(id,mark);
        ControllerShowResults.changed= true;

        cancel();
    }

    public void cancel() {
        Stage stage = (Stage) markTxt.getScene().getWindow();
        stage.close();
    }
}
