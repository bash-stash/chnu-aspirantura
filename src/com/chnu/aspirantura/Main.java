package com.chnu.aspirantura;

import com.chnu.aspirantura.aspirant.ControllerAspirant;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class Main extends Application {

    private static BorderPane root = new BorderPane();
    public static Stage mainStage;
    public static Scene scene;
    public static Main mInstance;

    public static ControllerAspirant controllerAspirant;

    public static void setMainStageTitle(String title) {
        mainStage.setTitle(title);
    }

    public static BorderPane getRoot() {
        return root;
    }


    public static void loadMainView(Class cl, Stage primaryStage) throws IOException {

        URL url = cl.getClass().getResource("/fxml/aspirant/view_aspirant.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setLocation(url);
        Pane paneMain = fxmlLoader.load();
        paneMain.setId("paneAspirant");
        controllerAspirant = fxmlLoader.getController();

        URL menuBarUrl = cl.getClass().getResource("/fxml/menu.fxml");
        MenuBar menuBar = FXMLLoader.load(menuBarUrl);


        URL menuBarRightUrl = cl.getClass().getResource("/fxml/menu_right.fxml");
        MenuBar rightBar = FXMLLoader.load(menuBarRightUrl);
        rightBar.getMenus().get(0).setText(ControllerLogin.login);
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(menuBar, spacer, rightBar);


        ControllerAspirant.root = root;
        ControllerMenu.setActivePane(paneMain);
        ControllerMenu.setPaneAspirant(paneMain);
        root.setTop(menubars);
        root.setCenter(paneMain);

        if (scene == null) scene = new Scene(root, 1300, 710);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Відділ аспірантури | Аспіранти");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/img/icon.png")));
        scene.getStylesheets().add(0, "/css/style.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        mainStage = primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        mInstance = this;
        LoadWindow.loader = LoadWindow.getInstance();
        LoadWindow.loader.openWindow("/fxml/form_login.fxml", "Відділ аспірантури | Вхід", 300, 500, "paneAspirant", 1, 1);
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}