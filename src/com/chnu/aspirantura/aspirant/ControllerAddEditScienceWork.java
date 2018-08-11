package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ControllerAddEditScienceWork {

    public  static ObjectScienceWork work;
    public  static int aspirantId;
    public int workId=0;

    @FXML TextField name;
    @FXML TextField where;
    @FXML TextField link;
    @FXML DatePicker datePicker;

    @FXML Button apply;

    public void initialize(){
        if (work!=null){
        name.setText(work.name);
        where.setText(work.where);
        link.setText(work.link);
        LocalDate ld = new java.sql.Date(work.date.getTime()).toLocalDate();
        datePicker.setValue(ld);
        }

        System.out.println(aspirantId+" aspit");

        if (work==null) apply.setText("Додати");

        if (work!=null){
            workId = work.id;
            work=null;
        }
    }


    public void apply(){

        String nameV=name.getText();
        String whereV=where.getText();
        String linkV=link.getText();
        LocalDate date=datePicker.getValue();


        if (workId!=0){
            SqlCommander.editScienceWork(nameV,whereV,linkV,date,workId);
        }
        else{
            SqlCommander.addScienceWork(nameV,whereV,linkV,date,aspirantId);
            ControllerShowScience.addedNew = true;
            aspirantId=0;
        }

        cancel();
    }

    public void cancel(){
        Stage stage = (Stage) where.getScene().getWindow();
        stage.close();
    }
}
