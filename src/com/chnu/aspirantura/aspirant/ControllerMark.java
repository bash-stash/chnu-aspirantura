package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.Utils;
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
        if (markPo !=null && !markPo.equals("")) markPoints.setText(markPo);
        if (markNa !=null && !markNa.equals(""))  markNational.setValue(markNa);
        if (markIE !=null && !markIE.equals(""))  markIECTIS.setValue(markIE);
    }

    public void apply(){
        markPo = markPoints.getText();
        markIE = markIECTIS.getValue();
        markNa = markNational.getValue();


        if (markNa.equals("Не обрано"))markNa="";
        if (markIE.equals("Не обрано"))markIE="";


        try {
            if (!markPo.equals("")) Integer.parseInt(markPo);
            SqlCommander.editMark(id,markPo,markNa,markIE);
            ControllerShowResults.changed= true;
        }catch (Exception e){
            Utils.showErrorMessage("Невірне значення балів");
        }

        cancel();
    }

    public void cancel() {
        Stage stage = (Stage) markPoints.getScene().getWindow();
        stage.close();
    }
}
