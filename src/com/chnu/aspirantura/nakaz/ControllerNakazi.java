package com.chnu.aspirantura.nakaz;

import com.chnu.aspirantura.LoadWindow;
import com.chnu.aspirantura.Main;
import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.aspirant.ControllerAspirant;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class ControllerNakazi {


    private ObservableList<ObjectNakaz> nakaziData = FXCollections.observableArrayList();
    static boolean loaded=false;

    @FXML
    public   TableView<ObjectNakaz> table;

    @FXML
    private ChoiceBox searchType;

    @FXML
    private Label searchResultCount;

    @FXML
    private TextField textToFind;



    @FXML
    public  TableColumn<ObjectNakaz, Integer> tableNakazId;

    @FXML
    public  TableColumn<ObjectNakaz, String> tableNakazNumber;

    @FXML
    public TableColumn<ObjectNakaz, String> tableNakazType;

    @FXML
    public TableColumn<ObjectNakaz, String> tableNakazDate;

     @FXML
    public TableColumn<ObjectNakaz, String> tableDisciplineVykladach;

    @FXML
    public TableColumn<ObjectNakaz, String> tableDisciplineSpeciality;




    static boolean addedNew = false;

    public void initialize(){
        Label l1 = new Label("Немає наказів у базі даних. Створіть новий наказ!");
        l1.setStyle("-fx-font-size: 20px");
        table.setPlaceholder(l1);
        tableNakazId.setStyle("-fx-alignment: CENTER");
        tableNakazDate.setStyle("-fx-alignment: CENTER");

        tableNakazId.setCellValueFactory(new PropertyValueFactory<ObjectNakaz, Integer>("id"));
        tableNakazNumber.setCellValueFactory(new PropertyValueFactory<ObjectNakaz, String>("number"));
        tableNakazType.setCellValueFactory(new PropertyValueFactory<ObjectNakaz, String>("type"));
        tableNakazDate.setCellValueFactory(new PropertyValueFactory<ObjectNakaz, String>("date"));

        initData();

        table.setItems(nakaziData);
        searchResultCount.setText("Всього результатів: " + nakaziData.size());


        if (!loaded){
            MenuItem mi1 = new MenuItem("Переглянути вміст наказу");
            mi1.setOnAction((ActionEvent event) -> {
            ObjectNakaz item = table.getSelectionModel().getSelectedItem();
            ControllerShowInfoNakaz.nakazId = item.getId();
            Stage primaryStage = new Stage();
            URL paneMainUrl = getClass().getResource("/fxml/nakaz/form_show_nakaz.fxml");
            Pane paneMain = null;
            try {
                paneMain = FXMLLoader.load(paneMainUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BorderPane root = new BorderPane();
            root.setCenter(paneMain);
            Scene scene = new Scene(root, 830, 590);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Відділ аспірантури | Наказ " + item.getNumber());
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/img/icon.png")));
            scene.getStylesheets().add(0,"/css/style.css");
            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.setResizable(false); //?
            primaryStage.showAndWait();
        });

            ContextMenu menu = new ContextMenu();
            menu.getItems().addAll(mi1);
            table.setContextMenu(menu);


    }


        searchType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                find();
            }
        });
        loaded = true;

    }


    public void initData(){
        nakaziData= SqlCommander.getAllNakazi();
    }


    public void loadFormAddNakaz() throws IOException {
        LoadWindow.loader.openWindow("/fxml/nakaz/form_add_nakaz.fxml","Відділ аспірантури | Додати новий наказ",500,370,null,2,0);

        if (addedNew){
            addedNew=false;
            refreshData();
        }
    }
    public void loadFormSettings() throws IOException {
        LoadWindow.loader.openWindow("/fxml/nakaz/form_settings_nakazi.fxml","Відділ аспірантури | Накази | Налаштування",480,390,null,2,0);

    }



    public void loadFormEditNakaz() throws IOException {
        ControllerEditNakaz.nakaziData = nakaziData;

        ObjectNakaz o = table.getSelectionModel().getSelectedItem();

        if(o!=null){
            ControllerEditNakaz.nakaz = o;
        }

        LoadWindow.loader.openWindow("/fxml/nakaz/form_edit_nakaz.fxml","Відділ аспірантури | Редагувати наказ",500,380,null,2,0);
        
        if (addedNew){
            addedNew=false;
            refreshData();
        }

    }



    public void refreshData(){
        initData();
        table.setItems(nakaziData);
        searchResultCount.setText("Всього результатів: " + nakaziData.size());
    }


    public void find() {
        String toFind = textToFind.getText();
        if (!toFind.equals("")) {
            Label l1 = new Label("Не знайдено!");
            l1.setStyle("-fx-font-size: 20px");
            table.setPlaceholder(l1);
            String searchKind = (String) searchType.getValue();
            table.setItems(FXCollections.observableArrayList());
            int i = 0;
            for (ObjectNakaz nakaz: nakaziData) {
                if (searchKind.equals("Номер наказу")) {
                    if (nakaz.getNumber().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(nakaz);
                        i++;
                    }
                }

                else if (searchKind.equals("Дата наказу")) {
                    if (nakaz.getDate().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(nakaz);
                        i++;
                    }
                }

                    else if (searchKind.equals("Тип наказу")){
                    if (nakaz.getType().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(nakaz);
                        i++;
                    }
                }
            }
            searchResultCount.setText("Знайдено: " + i);
        }else{
            Label l1 = new Label("Немає наказів у базі даних. Створіть новий наказ!");
            l1.setStyle("-fx-font-size: 20px");
            table.setPlaceholder(l1);
            table.setItems(nakaziData);
            searchResultCount.setText("Всього результатів: " + nakaziData.size());
        }
    }

    public void clear(){
        textToFind.setText("");
        table.setItems(nakaziData);
        searchResultCount.setText("Всього результатів: " + nakaziData.size());
        Platform.runLater( () -> ControllerAspirant.root.requestFocus());
    }
}
