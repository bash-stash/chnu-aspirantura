package com.chnu.aspirantura.aspirant;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

/**
 * Created by fibs on 12.02.2018.
 */
public class ControllerPraktika {
    static int actionId=0;
    static ObjectAspirant aspirant;
    static ObservableList<ObjectAspirant> aspirantsData;
    @FXML
    private ComboBox<ObjectAspirant> comboBox;

    @FXML

    public void initialize(){

        comboBox.setItems(aspirantsData);
        comboBox.setPromptText("Оберіть аспіранта");
        if (aspirant!=null) {
            comboBox.setValue(aspirant);
            System.out.println(comboBox.getValue().getId());
        }
    }

}