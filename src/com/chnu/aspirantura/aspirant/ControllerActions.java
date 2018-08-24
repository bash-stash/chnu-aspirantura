package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.nakaz.ObjectNakaz;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ControllerActions{
    static int actionId=0;
    static ObjectAspirant aspirant;
    static ObservableList<ObjectAspirant> aspirantsData;
    static ObservableList<ObjectNakaz> nakazData;
    @FXML private ComboBox<ObjectAspirant> comboBox;
    @FXML private ComboBox<ObjectNakaz> comboBoxNakaz;

    @FXML private Label title;

    public void initialize(){
        comboBox.setItems(aspirantsData);

        comboBox.valueProperty().addListener(new ChangeListener<ObjectAspirant>() {
            @Override
            public void changed(ObservableValue<? extends ObjectAspirant> ObjectAspirant, ObjectAspirant oldValue, ObjectAspirant newValue) {
              aspirant = newValue;
            }
        });

        if (aspirant!=null) {
            comboBox.setValue(aspirant);
            System.out.println(comboBox.getValue().getId());
        }

        switch (actionId){
            case 1:
                title.setText("Оформлення перерви");
                break;
            case 2:
                title.setText("Поновлення після перерви");
                break;
            case 3:
                title.setText("Закінчення аспірантури");
                break;
            case 5:
                title.setText("Відрахування з аспірантури");
                break;
        }

        nakazData = SqlCommander.getNakazByType(actionId);
        comboBoxNakaz.setItems(nakazData);
    }

    public void apply(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.now();
        SqlCommander.addStatus(aspirant,localDate,comboBox.getValue().getId(),comboBoxNakaz.getValue().getId());
        ControllerAspirant.addedNew=true;
        Stage stage = (Stage) comboBoxNakaz.getScene().getWindow();
        stage.close();
    }

    public void cancel() {


        Stage stage = (Stage) comboBoxNakaz.getScene().getWindow();
        stage.close();
    }
}
