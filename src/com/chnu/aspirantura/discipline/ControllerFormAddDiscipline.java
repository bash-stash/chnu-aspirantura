package com.chnu.aspirantura.discipline;

import com.chnu.aspirantura.speciality.ObjectSpeciality;
import com.chnu.aspirantura.vykladach.ObjectVykladach;
import com.chnu.aspirantura.SqlCommander;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerFormAddDiscipline {
    private static ObservableList<ObjectTypeDiscipl> typeDiscipline= FXCollections.observableArrayList();
    private static ObservableList<ObjectVykladach>   vykladadchData= FXCollections.observableArrayList();
    private static ObservableList<ObjectSpeciality>  specialityData= FXCollections.observableArrayList();

    private static ObservableList<ObjectStudyForm>  forms= FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> comboBoxNumberSym;


    @FXML
    private ComboBox<ObjectStudyForm> comboBoxForm;

    @FXML
    private ComboBox<String> comboBoxNumberCourse;

    @FXML
    private ComboBox<ObjectTypeDiscipl> comboBoxTypeControl;

    @FXML
    private ComboBox<ObjectSpeciality> comboBoxSpeciality;

    @FXML
    private ComboBox<ObjectVykladach> comboBoxVykladach;

    @FXML
    private TextField textFieldName;



    public void initialize(){
        comboBoxNumberSym.setValue("1");
        comboBoxNumberCourse.setValue("1");
        typeDiscipline = SqlCommander.getAllTypeDiscipline();
        specialityData = SqlCommander.getAllSpeciality();
        vykladadchData = SqlCommander.getAllVykladachi();
        forms = SqlCommander.getAllForms();

        comboBoxForm.setItems(forms);
        comboBoxTypeControl.setItems(typeDiscipline);
        comboBoxSpeciality.setItems(specialityData);
        comboBoxVykladach.setItems(vykladadchData);
    }


    public void apply() {


        String nameDislc = textFieldName.getText();
        int nomSym = Integer.parseInt(comboBoxNumberSym.getValue());
        int nomCourse = Integer.parseInt(comboBoxNumberCourse.getValue());

        int form = comboBoxForm.getValue().getId();
        int idVykl = comboBoxVykladach.getValue().getId();
        int idSpeciality = comboBoxSpeciality.getValue().getId();
        int idTypeControl = comboBoxTypeControl.getValue().getId();

       SqlCommander.addNewDiscipline(nameDislc,idVykl,idTypeControl,nomSym,idSpeciality,nomCourse,form);


        ControllerDiscipline.addedNew = true;
        Stage stage = (Stage) textFieldName.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) textFieldName.getScene().getWindow();
        stage.close();
    }
}
