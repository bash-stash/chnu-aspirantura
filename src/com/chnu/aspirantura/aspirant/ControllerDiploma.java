package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ControllerDiploma {


    static int aspirantId;
    private ObjectDiploma diploma;


    @FXML
    private TextField titleTextField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TextArea descriptionTextArea;


    public void initialize() {
        diploma = SqlCommander.getDiplomaByAspirantId(aspirantId);
        if (diploma != null) {
            titleTextField.setText(diploma.getTitle());
            descriptionTextArea.setText(diploma.getDescription());
            statusComboBox.getSelectionModel().select(diploma.getStatus());
        }
    }


    public void apply() {
        String title = titleTextField.getText();
        String description = descriptionTextArea.getText();
        int statusCode = statusComboBox.getSelectionModel().getSelectedIndex();


        if (diploma == null) {
            SqlCommander.createDiploma(title, description, statusCode, aspirantId);
        } else {
            SqlCommander.editDiploma(diploma.getId(), title, description, statusCode);
        }
        cancel();
    }

    public void cancel() {
        Stage stage = (Stage) titleTextField.getScene().getWindow();
        stage.close();
    }
}
