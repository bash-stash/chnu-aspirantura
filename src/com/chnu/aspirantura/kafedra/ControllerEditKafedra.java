package com.chnu.aspirantura.kafedra;
import com.chnu.aspirantura.SqlCommander;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerEditKafedra{

    static ObjectKafedra kafedra;
    static ObservableList<ObjectKafedra> kafedraData;



    @FXML
    private ComboBox<ObjectKafedra> comboBoxKafedra;


    @FXML
    private TextField nameKafedra;
    @FXML
    private TextField roomKafedra;


    public void initialize(){

        comboBoxKafedra.setItems(kafedraData);

        if (kafedra!=null) {
            comboBoxKafedra.setValue(kafedra);
            nameKafedra.setText(kafedra.getName());
            roomKafedra.setText(kafedra.getRoom());
        }

        comboBoxKafedra.valueProperty().addListener(new ChangeListener<ObjectKafedra>() {
            @Override
            public void changed(ObservableValue<? extends ObjectKafedra> observable, ObjectKafedra oldValue, ObjectKafedra newValue) {
                nameKafedra.setText(newValue.getName());
                roomKafedra.setText(newValue.getRoom());
            }
        });
        kafedra=null;
    }


    public void apply() {
        //TODO check input param

        int id = comboBoxKafedra.getValue().getId();
        String name = nameKafedra.getText();
        String room = roomKafedra.getText();


        SqlCommander.editKafedra(id,name,room);


        ControllerKafedra.addedNew = true;
        Stage stage = (Stage) roomKafedra.getScene().getWindow();
        stage.close();
    }


    public void cancel() {
        Stage stage = (Stage) roomKafedra.getScene().getWindow();
        stage.close();
    }
}

