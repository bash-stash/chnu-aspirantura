package com.chnu.aspirantura.speciality;

import com.chnu.aspirantura.kafedra.ObjectKafedra;
import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.faculty.ObjectFaculty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerFormAddSpeciality extends SpecialityData {


    @FXML
    private ComboBox<ObjectKafedra> comboBoxKafedra;

    @FXML
    private ComboBox<ObjectFaculty> comboBoxFaculty;



    @FXML
    private TextField nameSpeciality;

    @FXML
    private TextField codeSpeciality;


    public void initialize(){
        comboBoxFaculty.setItems(facultyData);
        comboBoxKafedra.setItems(kafedraData);
    }


    public void apply() {
        int facultyId=-1;
        try {
            facultyId = comboBoxFaculty.getValue().getId();
        }catch (Exception e){

        }


        int kafedraId=-1;
        try {
            kafedraId = comboBoxKafedra.getValue().getId();
        }catch (Exception e){

        }



        String name = nameSpeciality.getText();
        String code = codeSpeciality.getText();


        System.out.println(name+"\n"+code+"\n"+facultyId);

        SqlCommander.addNewSpeciality(name,code,facultyId,kafedraId);

        ControllerSpeciality.addedNew = true;
        Stage stage = (Stage) codeSpeciality.getScene().getWindow();
        stage.close();
    }


    public void cancel() {
        Stage stage = (Stage) codeSpeciality.getScene().getWindow();
        stage.close();
    }
}
