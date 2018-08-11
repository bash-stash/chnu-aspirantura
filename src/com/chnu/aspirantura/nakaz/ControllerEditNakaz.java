package com.chnu.aspirantura.nakaz;

import com.chnu.aspirantura.SqlCommander;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class ControllerEditNakaz {

    static ObjectNakaz nakaz;
    static ObservableList<ObjectNakaz> nakaziData;
    static ObservableList<ObjectTypeNakazi> typeNakazi = FXCollections.observableArrayList();

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<ObjectTypeNakazi> comboBoxType;

    @FXML
    private ComboBox<ObjectNakaz> comboBoxNumber;

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
        comboBoxNumber.setItems(nakaziData);
        comboBoxType.setItems(typeNakazi);

        Date today = new Date();
        DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");
        setDate(targetFormat.format(today));

        if (nakaz!=null) {
            for(ObjectTypeNakazi s : typeNakazi) {
                if (s.getValue().equals(nakaz.getType())) {
                    comboBoxType.setValue(s);
                    break;
                }
            }
            comboBoxNumber.setValue(nakaz);
            setDate(nakaz.getDate());
            aboutText.setText(SqlCommander.getAboutNakaz(nakaz.getId()));
            number.setText(nakaz.getNumber());
        }

        comboBoxNumber.valueProperty().addListener(new ChangeListener<ObjectNakaz>() {
            @Override
            public void changed(ObservableValue<? extends ObjectNakaz> observable, ObjectNakaz oldValue, ObjectNakaz newValue) {
                try {
                    for(ObjectTypeNakazi s : typeNakazi) {
                        if (s.getValue().equals(newValue.getType())) {
                            comboBoxType.setValue(s);
                            break;
                        }
                    }
                    setDate(newValue.getDate());
                    aboutText.setText(SqlCommander.getAboutNakaz(newValue.getId()));
                    number.setText(newValue.getNumber());
                } catch (Exception e) {
                }
            }
        });
        nakaz=null;
    }


    public void apply() {
        //TODO check input params
        String about = aboutText.getText();
        int type = comboBoxType.getValue().getId();
        int id = comboBoxNumber.getValue().getId();
        LocalDate date = datePicker.getValue();
        String numberValue = number.getText();

        SqlCommander.editNakaz(numberValue,about,type,date,id);

        ControllerNakazi.addedNew = true;
        Stage stage = (Stage) aboutText.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) aboutText.getScene().getWindow();
        stage.close();
    }
}
