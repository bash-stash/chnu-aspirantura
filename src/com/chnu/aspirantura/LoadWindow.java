package com.chnu.aspirantura;

import com.chnu.aspirantura.aspirant.ControllerAddAspirant;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoadWindow {
    public static LoadWindow loader;

    private LoadWindow() {
    }


    public static LoadWindow getInstance() {
        if (loader == null) loader = new LoadWindow();
        return loader;
    }


    public void openWindow(String url, String title, int width, int height, String idPane, int param, int activePane) throws IOException {
        Stage primaryStage = new Stage();
        URL paneMainUrl = getClass().getResource(url);
        Pane paneMain = FXMLLoader.load(paneMainUrl);
        BorderPane root = new BorderPane();
        root.setCenter(paneMain);

        if (idPane != null) root.setId(idPane);
        Scene scene = new Scene(root, width, height);
        switch (activePane) {
            case 1:
                ControllerMenu.setActivePane(root);
                break;
            case 0:
                break;
            case 2:
                ControllerAddAspirant.scene = scene;
                break;

        }
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/img/icon.png")));
        scene.getStylesheets().add(0, "/css/style.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); //?
        if (param == 1) {
            primaryStage.show();
        }
        if (param == 2) {
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.showAndWait();

        }
    }
}
