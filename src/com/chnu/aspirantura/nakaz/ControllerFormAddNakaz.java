package com.chnu.aspirantura.nakaz;

import com.chnu.aspirantura.SqlCommander;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ControllerFormAddNakaz {

    static ObservableList<ObjectTypeNakazi> typeNakazi = FXCollections.observableArrayList();

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<ObjectTypeNakazi> comboBoxType;


    @FXML
    private TextArea aboutText;
    @FXML
    private TextField number;

    public void setDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        datePicker.setValue(localDate);
    }

    public void initialize(){
        typeNakazi = SqlCommander.getAllTypeNakazi();
        comboBoxType.setItems(typeNakazi);

        Date today = new Date();
        DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");
        System.out.println(targetFormat.format(today));
        setDate(targetFormat.format(today));
    }


    public void apply() {
        //TODO check input param

        String about = aboutText.getText();
        int type = comboBoxType.getValue().getId();
        LocalDate date = datePicker.getValue();
        String numberValue = number.getText();

        SqlCommander.addNewNakaz(numberValue,about,type,date);

        ControllerNakazi.addedNew = true;
        Stage stage = (Stage) aboutText.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) aboutText.getScene().getWindow();
        stage.close();
    }
}
