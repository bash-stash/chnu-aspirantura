package com.chnu.aspirantura;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class Utils {
    public static Logger logger = LogManager.getLogger("app");
    public static Logger dbLogger = LogManager.getLogger("db");

    public static void showErrorMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText("Помилка");
        alert.setContentText(msg);
        alert.showAndWait();
    }


    public static int askYesNo(String ask){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Увага!");
        alert.setContentText(ask);

        Optional<ButtonType> result = alert.showAndWait();

        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            return 1;
        }

        return 0;
    }
}
