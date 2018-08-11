package com.chnu.aspirantura.speciality;

import com.chnu.aspirantura.*;
import com.chnu.aspirantura.faculty.ObjectFaculty;
import com.chnu.aspirantura.kafedra.ObjectKafedra;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerEditSpeciality extends SpecialityData {

    static ObjectSpeciality speciality;


    @FXML
    private ComboBox<ObjectSpeciality> comboBoxSpeciality;
    @FXML
    private ComboBox<ObjectKafedra> comboBoxKafedra;
    @FXML
    private ComboBox<ObjectFaculty> comboBoxFaculty;
    @FXML
    private ComboBox<String> comboBoxType;


    @FXML
    private TextField nameSpeciality;
    @FXML
    private TextField codeSpeciality;


    public void initialize(){
        comboBoxSpeciality.setItems(specialityData);
        comboBoxKafedra.setItems(kafedraData);
        comboBoxFaculty.setItems(facultyData);
        if (speciality!=null) {
            comboBoxSpeciality.setValue(speciality);
            comboBoxType.setValue(speciality.getStatus());
            nameSpeciality.setText(speciality.getName());
            codeSpeciality.setText(speciality.getCode());
            if (speciality.getFaculty()!=null) comboBoxFaculty.setValue(speciality.getFaculty());
            if (speciality.getKafedra()!=null) comboBoxKafedra.setValue(speciality.getKafedra());
        }

        comboBoxSpeciality.valueProperty().addListener(new ChangeListener<ObjectSpeciality>() {
            @Override
            public void changed(ObservableValue<? extends ObjectSpeciality> observable, ObjectSpeciality oldValue, ObjectSpeciality newValue) {
                try {
                    comboBoxType.setValue(newValue.getStatus());
                    nameSpeciality.setText(newValue.getName());
                    codeSpeciality.setText(newValue.getCode());
                    comboBoxKafedra.setValue(null);
                    comboBoxFaculty.setValue(null);
                    if (newValue.getKafedra()!=null) comboBoxKafedra.setValue(newValue.getKafedra());
                    if (newValue.getFaculty()!=null) comboBoxFaculty.setValue(newValue.getFaculty());

                } catch (Exception e) {
                }
            }
        });
        speciality=null;
    }


    public void clearFaculty(){
        comboBoxFaculty.setValue(null);
    }
    public void clearKafedra(){
        comboBoxKafedra.setValue(null);
    }


    public void apply() {
        //TODO check input param

        int faculty_id=-1;
        int kafedra_id=-1;

        int id = comboBoxSpeciality.getValue().getId();
        String name = nameSpeciality.getText();
        String code = codeSpeciality.getText();
        String type = comboBoxType.getValue();

        try{
            faculty_id = comboBoxFaculty.getValue().getId();
        }catch (Exception e){

        }

        try{
            kafedra_id = comboBoxKafedra.getValue().getId();
        }catch (Exception e){

            kafedra_id=-2;
        }

        System.out.println(faculty_id);
        System.out.println(kafedra_id);
        SqlCommander.editSpeciality(id,name,code,type,faculty_id,kafedra_id);
        ControllerSpeciality.addedNew = true;


        Stage stage = (Stage) codeSpeciality.getScene().getWindow();
        stage.close();
    }


    public void cancel() {
        Stage stage = (Stage) codeSpeciality.getScene().getWindow();
        stage.close();
    }
}
