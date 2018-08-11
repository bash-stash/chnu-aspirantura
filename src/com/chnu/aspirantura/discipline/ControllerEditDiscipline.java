package com.chnu.aspirantura.discipline;

import com.chnu.aspirantura.*;
import com.chnu.aspirantura.speciality.ObjectSpeciality;
import com.chnu.aspirantura.vykladach.ObjectVykladach;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerEditDiscipline {

    private static ObservableList<ObjectTypeDiscipl> typeDiscipline= FXCollections.observableArrayList();
    private static ObservableList<ObjectVykladach>   vykladadchData= FXCollections.observableArrayList();
    private static ObservableList<ObjectSpeciality>  specialityData= FXCollections.observableArrayList();
    public static ObservableList<ObjectDiscipline>   disciplinesData= FXCollections.observableArrayList();
    public static ObservableList<ObjectStudyForm>   forms= FXCollections.observableArrayList();

    static ObjectDiscipline discipline;

    @FXML
    private ComboBox<String> comboBoxNumberSym;
    @FXML
    private ComboBox<String> comboBoxNumberCourse;


    @FXML
    private ComboBox<ObjectStudyForm> comboBoxForm;

    @FXML
    private ComboBox<ObjectTypeDiscipl> comboBoxTypeControl;

    @FXML
    private ComboBox<ObjectSpeciality> comboBoxSpeciality;

    @FXML
    private ComboBox<ObjectVykladach> comboBoxVykladach;


    @FXML
    private ComboBox<ObjectDiscipline> comboBoxDisciplines;


    @FXML
    private ComboBox<String> comboBoxStatus;

    @FXML
    private TextField textFieldName;



    public void initialize(){
        forms = SqlCommander.getAllForms();
        comboBoxNumberSym.setValue("1");
        comboBoxNumberCourse.setValue("1");
        typeDiscipline =  SqlCommander.getAllTypeDiscipline();
        specialityData =  SqlCommander.getAllSpeciality();
        vykladadchData =  SqlCommander.getAllVykladachi();
        disciplinesData = SqlCommander.getAllDisciplines(0);
        comboBoxTypeControl.setItems(typeDiscipline);
        comboBoxSpeciality.setItems(specialityData);
        comboBoxVykladach.setItems(vykladadchData);
        comboBoxDisciplines.setItems(disciplinesData);
        comboBoxForm.setItems(forms);

        if (discipline!=null) {
            comboBoxNumberSym.setValue(discipline.getSemestr()+"");
            comboBoxNumberCourse.setValue(discipline.getCourse()+"");
            comboBoxDisciplines.setValue(discipline);
            textFieldName.setText(discipline.getName());
            comboBoxForm.setValue(forms.get(discipline.getForm()-1));

            for(ObjectTypeDiscipl s : typeDiscipline) {
                if (s.getValue().equals(discipline.getTypeKontrol())) {
                    comboBoxTypeControl.setValue(s);

                    break;
                }
            }
            textFieldName.setText(discipline.getName());
            comboBoxStatus.setValue(discipline.getStatus());
            for(ObjectSpeciality sp : specialityData) {
                if (sp.getName().equals(discipline.getSpeciality())) {
                    comboBoxSpeciality.setValue(sp);
                    break;
                }
            }

            for(ObjectVykladach vykl : vykladadchData) {
                if (vykl.getName().equals(discipline.getVykladach())) {
                    comboBoxVykladach.setValue(vykl);
                    break;
                }
            }


        }

        comboBoxDisciplines.valueProperty().addListener(new ChangeListener<ObjectDiscipline>() {
            @Override
            public void changed(ObservableValue<? extends ObjectDiscipline> observable, ObjectDiscipline oldValue, ObjectDiscipline newValue) {
                try {
                    for(ObjectTypeDiscipl s : typeDiscipline) {
                        if (s.getValue().equals(newValue.getTypeKontrol())) {
                            comboBoxTypeControl.setValue(s);
                            break;
                        }
                    }

                    for(ObjectSpeciality sp : specialityData) {
                        if (sp.getName().equals(newValue.getSpeciality())) {
                            comboBoxSpeciality.setValue(sp);
                            break;
                        }
                    }

                    for(ObjectVykladach vykl : vykladadchData) {
                        if (vykl.getName().equals(newValue.getVykladach())) {
                            comboBoxVykladach.setValue(vykl);
                            break;
                        }
                    }

                    comboBoxForm.setValue(forms.get(newValue.getForm()));
                    textFieldName.setText(newValue.getName());
                    comboBoxStatus.setValue(newValue.getStatus());
                    comboBoxNumberSym.setValue(newValue.getSemestr()+"");
                    comboBoxNumberCourse.setValue(newValue.getCourse()+"");

                } catch (Exception e) {
                }
            }
        });
        discipline=null;
    }






    public void apply() {
        //TODO CHECK PARAMS

        String nameDislc = textFieldName.getText();
        String status = comboBoxStatus.getValue();
        int id = comboBoxDisciplines.getValue().getId();
        int nomSym = Integer.parseInt(comboBoxNumberSym.getValue());
        int course = Integer.parseInt(comboBoxNumberCourse.getValue());
        int form = comboBoxForm.getValue().getId();


        int idVykl = comboBoxVykladach.getValue().getId();
        int idSpeciality = comboBoxSpeciality.getValue().getId();
        int idTypeControl = comboBoxTypeControl.getValue().getId();

        SqlCommander.editDiscipline(nameDislc,idVykl,idTypeControl,nomSym,idSpeciality,id,status,course,form);

        ControllerDiscipline.addedNew = true;
        Stage stage = (Stage) textFieldName.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) textFieldName.getScene().getWindow();
        stage.close();
    }
}
