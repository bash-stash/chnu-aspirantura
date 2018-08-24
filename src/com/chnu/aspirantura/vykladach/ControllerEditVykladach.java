package com.chnu.aspirantura.vykladach;

import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.speciality.ObjectSpeciality;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ControllerEditVykladach {

    static ObjectVykladach vykladach;
    static ObservableList<ObjectVykladach> vykladachesData;

    @FXML
    private ComboBox<ObjectVykladach> comboBoxVykladach;
    @FXML
    private TextField nameVykladach;

    static ObservableList<ObjectSpeciality> specialitiesData;
    static ObservableList<ObjectSpeciality> specialitiesDataSelected;

    @FXML
    private ComboBox<ObjectSpeciality> comboSpecialities;
    @FXML
    private ListView<ObjectSpeciality> listSpecialities;


    public void addSpeciality() {
        ObjectSpeciality o = comboSpecialities.getValue();

        if (o != null) specialitiesDataSelected.add(o);
        listSpecialities.setItems(specialitiesDataSelected);
        if (o != null) specialitiesData.remove(o);
    }

    public void deleteSpeciality() {
        ObjectSpeciality o = listSpecialities.getFocusModel().getFocusedItem();
        if (o != null) specialitiesData.add(o);
        if (o != null) specialitiesDataSelected.remove(o);
    }


    public void initialize() {
        specialitiesDataSelected = FXCollections.observableArrayList();
        specialitiesData = SqlCommander.getAllSpeciality();
        comboSpecialities.setItems(specialitiesData);
        comboBoxVykladach.setItems(vykladachesData);
        comboBoxVykladach.setPromptText("Оберіть викладача");
        listSpecialities.setItems(specialitiesDataSelected);

        if (vykladach != null) {
            comboBoxVykladach.setValue(vykladach);
            nameVykladach.setText(vykladach.getName());
            specialitiesDataSelected.setAll(vykladach.getSpecialities());

            specialitiesDataSelected.forEach((y) -> {
                specialitiesData.removeIf((x) -> {
                    if (x.getCode().equals(y.getCode())) return true;
                    return false;
                });
            });

        }

        comboBoxVykladach.valueProperty().addListener(new ChangeListener<ObjectVykladach>() {
            @Override
            public void changed(ObservableValue<? extends ObjectVykladach> observable, ObjectVykladach oldValue, ObjectVykladach newValue) {
                nameVykladach.setText(newValue.getName());
                if (listSpecialities.getItems() != null) specialitiesData.addAll(listSpecialities.getItems());
                specialitiesDataSelected.setAll(newValue.getSpecialities());


                specialitiesDataSelected.forEach((y) -> {
                    specialitiesData.removeIf((x) -> {
                        if (x.getCode().equals(y.getCode())) return true;
                        return false;
                    });
                });

            }
        });
        vykladach = null;

    }

    public void apply() {

        int id = comboBoxVykladach.getValue().getId();
        String name = nameVykladach.getText();
        comboBoxVykladach.getValue().setSpecialities(specialitiesDataSelected);
        SqlCommander.editVykladach(id, name, specialitiesDataSelected);
        ControllerVykladach.addedNew = true;

        Stage stage = (Stage) comboBoxVykladach.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) comboBoxVykladach.getScene().getWindow();
        stage.close();
    }
}
