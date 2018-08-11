package com.chnu.aspirantura.vykladach;

import com.chnu.aspirantura.LoadWindow;
import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.aspirant.ControllerAspirant;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;


public class ControllerVykladach {
    private ObservableList<ObjectVykladach> vykladachesData = FXCollections.observableArrayList();

    static boolean loaded = false;

    @FXML
    public TableView<ObjectVykladach> table;

    @FXML
    private ChoiceBox searchType;

    @FXML
    private Label searchResultCount;

    @FXML
    private TextField textToFind;


    @FXML
    public TableColumn<ObjectVykladach, Integer> tableVykladachId;

    @FXML
    public TableColumn<ObjectVykladach, String> tableVykladachName;


    @FXML
    public TableColumn<ObjectVykladach, String> tableVykladachIsKerivnik;



    static boolean addedNew = false;

    public void initialize() {
        Label l1 = new Label("Немає жодного викладача у базі даних. Додайте нового викладача!");
        l1.setStyle("-fx-font-size: 20px");

        table.setPlaceholder(l1);
        tableVykladachId.setCellValueFactory(new PropertyValueFactory<ObjectVykladach, Integer>("id"));
        tableVykladachId.setStyle("-fx-alignment: CENTER");

        tableVykladachName.setCellValueFactory(new PropertyValueFactory<ObjectVykladach, String>("name"));
        tableVykladachIsKerivnik.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isKerivnik()));
        initData();
        table.setItems(vykladachesData);


        searchType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                find();
            }
        });

        searchResultCount.setText("Всього результатів: " + vykladachesData.size());
        loaded = true;
    }


    public void initData() {
        vykladachesData = SqlCommander.getAllVykladachi();
    }


    public void loadFormAddVykladach() throws IOException {

        LoadWindow.loader.openWindow("/fxml/vykladach/form_add_vykladach.fxml","Відділ аспірантури | Додати викладача",485,180,null,2,0);

        if (addedNew) {
            addedNew = false;
            refreshData();
        }
    }


    public void loadFormEditVykladach() throws IOException {
        ControllerEditVykladach.vykladachesData = vykladachesData;


        ObjectVykladach o = table.getSelectionModel().getSelectedItem();

        if (o != null) {
            ControllerEditVykladach.vykladach = o;
        }


        LoadWindow.loader.openWindow("/fxml/vykladach/form_edit_vykladach.fxml","Відділ аспірантури |  Редагувати дані викладача",485,510,null,2,0);
        if (addedNew) {
            addedNew = false;
            refreshData();
        }
    }


    public void refreshData() {
        initData();
        table.setItems(vykladachesData);
        searchResultCount.setText("Всього результатів: " + vykladachesData.size());
    }

    public void loadFormSettings() {

    }

    public void clear(){
        textToFind.setText("");
        table.setItems(vykladachesData);
        searchResultCount.setText("Всього результатів: " + vykladachesData.size());
        Platform.runLater( () -> ControllerAspirant.root.requestFocus());
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
            for (ObjectVykladach vykladach: vykladachesData) {
                if (searchKind.equals("Ім'я викладача")) {
                    if (vykladach.getName().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(vykladach);
                        i++;
                    }
                }
            }
            searchResultCount.setText("Знайдено: " + i);
        }else{
            Label l1 = new Label("Немає наказів у базі даних. Створіть новий наказ!");
            l1.setStyle("-fx-font-size: 20px");
            table.setPlaceholder(l1);
            table.setItems(vykladachesData);
            searchResultCount.setText("Всього результатів: " + vykladachesData.size());
        }
    }
}