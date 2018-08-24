package com.chnu.aspirantura.faculty;

import com.chnu.aspirantura.SqlCommander;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class ControllerEditFaculty {

    static ObjectFaculty faculty;
    static ObservableList<ObjectFaculty> facultyData;
    @FXML private ComboBox<ObjectFaculty> comboBoxName;
    @FXML private ComboBox<String> comboBoxStatus;

    @FXML private TextField facultyName;

    public void initialize(){
        comboBoxName.setItems(facultyData);
        comboBoxName.setPromptText("Оберіть факультет");

        if (faculty!=null) {
            comboBoxName.setValue(faculty);
            comboBoxStatus.setValue(faculty.getStatus());
            facultyName.setText(faculty.getName());
        }

        comboBoxName.valueProperty().addListener(new ChangeListener<ObjectFaculty>() {
            @Override
            public void changed(ObservableValue<? extends ObjectFaculty> observable, ObjectFaculty oldValue, ObjectFaculty newValue) {
                try {
                    facultyName.setText(newValue.getName());
                    comboBoxStatus.setValue(newValue.getStatus());
                } catch (Exception e) {
                }
            }
        });
        faculty=null;

    }
    public void apply() {
        //TODO check input param
        String name = facultyName.getText();
        String status = comboBoxStatus.getValue();

        SqlCommander.editFaculty(comboBoxName.getValue().getId(),name,status);
        ControllerFaculty.addedNew = true;
        Stage stage = (Stage) facultyName.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) facultyName.getScene().getWindow();
        stage.close();
    }
}
