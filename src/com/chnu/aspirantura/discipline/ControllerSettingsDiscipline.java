package com.chnu.aspirantura.discipline;

import com.chnu.aspirantura.LoadWindow;
import com.chnu.aspirantura.SqlCommander;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerSettingsDiscipline{

    private ObservableList<ObjectTypeDiscipl> typeData = FXCollections.observableArrayList();

    @FXML
    public TableView<ObjectTypeDiscipl> table;

    @FXML
    public TableColumn<ObjectTypeDiscipl, Integer> tableType;

    @FXML
    public  TableColumn<ObjectTypeDiscipl, String> tableValue;



    static boolean addedNew = false;

    public void initialize(){
        Label l1 = new Label("Немає типів у базі даних. Створіть новий тип!");
        l1.setStyle("-fx-font-size: 15px;");
        l1.setContentDisplay(ContentDisplay.CENTER);
        table.setPlaceholder(l1);
        tableType.setStyle("-fx-alignment: CENTER");

        tableType.setCellValueFactory(new PropertyValueFactory<ObjectTypeDiscipl, Integer>("id"));
        tableValue.setCellValueFactory(new PropertyValueFactory<ObjectTypeDiscipl, String>("value"));

        initData( );
        table.setItems(typeData);
    }


    public void initData(){
        typeData= SqlCommander.getAllTypeDiscipline();
    }


    public void apply() {
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }


    public void addNewType() throws IOException {
        LoadWindow.loader.openWindow("/fxml/discipline/form_settings_add_discipline_type.fxml","Дисципіліни | Налаштування | Створити новий тип",570,150,null,2,0);


        if (addedNew){
            addedNew=false;
            initData();
            table.setItems(typeData);
        }
    }

}