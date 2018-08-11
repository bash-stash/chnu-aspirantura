package com.chnu.aspirantura;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by fibs on 30.01.2018.
 */

public class ControllerMenuRight {
@FXML MenuBar menu;


    @FXML
    void openSettings(ActionEvent event) throws IOException {
        LoadWindow.loader = LoadWindow.getInstance();
        LoadWindow.loader.openWindow("/fxml/settings/main.fxml",ControllerLogin.login+ " | Налаштування",490,390,"",2,0);
    }


    @FXML
    void logout(ActionEvent event) throws IOException {
        ControllerMenu.activePane = null;
      ControllerMenu.paneAspirant = null;
        ControllerMenu.paneKerivniki = null;
        ControllerMenu.paneSpeciality = null;
        ControllerMenu.paneDiscipline = null;
        ControllerMenu.paneFaculty = null;
        ControllerMenu.paneNakazi = null;
        ControllerMenu.paneKafedra = null;
        ControllerMenu.paneVykladachi = null;

        Stage stage =  (Stage) menu.getScene().getWindow();
        stage.close();

        LoadWindow.loader = LoadWindow.getInstance();
        LoadWindow.loader.openWindow("/fxml/form_login.fxml","Відділ аспірантури | Вхід",300,500,"paneAspirant",1,1);
    }
}
