package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ControllerAddEditPractice {

    public static ObjectPractice practice;
    public static int aspirantId;

    public int workId = 0;

    @FXML
    TextField name;
    @FXML
    TextField where;

    @FXML
    TextField markNational;

    @FXML
    TextField markPoints;

    @FXML
    TextField markECTIS;

    @FXML
    TextField credits;


    @FXML
    TextArea workDescription;

    @FXML
    TextArea namesCommission;


    @FXML
    DatePicker dateFrom;

    @FXML
    DatePicker dateTill;


    @FXML
    Button apply;

    public void initialize() {
        if (practice!=null){
        name.setText(practice.name);
        where.setText(practice.organization);

        markECTIS.setText(practice.markIectis);
        markNational.setText(practice.markNational);
        markPoints.setText(String.valueOf(practice.markPoints));
        credits.setText(practice.credits);

        workDescription.setText(practice.workDescription);
        namesCommission.setText(practice.commissionNames);

        LocalDate from = new java.sql.Date(practice.from.getTime()).toLocalDate();
        LocalDate till = new java.sql.Date(practice.till.getTime()).toLocalDate();

        dateFrom.setValue(from);
        dateTill.setValue(till);

        }


        if (practice==null) apply.setText("Додати");

        if (practice!=null){
            workId = practice.id;
            practice=null;
        }
    }


    public void apply() {

        String name_=name.getText();
        String organization_=where.getText();

        String markNational_=markNational.getText();
        String markECTIS_=markECTIS.getText();
        int markPoints_= Integer.parseInt(markPoints.getText());
        String credits_=credits.getText();

        LocalDate dateFrom_=dateFrom.getValue();
        LocalDate dateTill_=dateTill.getValue();


        String workDescription_=workDescription.getText();
        String namesCommission_=namesCommission.getText();


        if (workId!=0){
            SqlCommander.editPractice(name_,organization_,markNational_,markECTIS_,markPoints_,credits_,dateFrom_,dateTill_,workDescription_,namesCommission_,workId);
        }
        else{
            SqlCommander.addPractice(name_,organization_,markNational_,markECTIS_,markPoints_,credits_,dateFrom_,dateTill_,workDescription_,namesCommission_,aspirantId);
            ControllerShowPractice.addedNew = true;
        }

        aspirantId=0;
        cancel();
    }

    public void cancel() {
        Stage stage = (Stage) where.getScene().getWindow();
        stage.close();
    }
}
