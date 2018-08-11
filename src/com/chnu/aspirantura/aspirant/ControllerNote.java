package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Created by fibs on 07.02.2018.
 */
public class ControllerNote {

    static String note;
    static int aspirantId;
    @FXML private TextArea noteTxt;


    public void initialize(){
        if (note!=null){
            noteTxt.setText(note);
            noteTxt.positionCaret(note.length());
        }
    }

    public void apply(){
        note = noteTxt.getText();
        SqlCommander.editNote(aspirantId,note);
        ControllerAspirant.addedNew = true;

        cancel();
    }

    public void cancel() {
        Stage stage = (Stage) noteTxt.getScene().getWindow();
        stage.close();
    }
}
