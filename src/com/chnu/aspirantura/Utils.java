package com.chnu.aspirantura;

import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {
    public static Logger logger = LogManager.getLogger("app");
    public static Logger dbLogger = LogManager.getLogger("db");

    public static void showErrorMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
