package com.chnu.aspirantura.nakaz;

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

/**
 * Created by fibs on 30.01.2018.
 */
public class ControllerSettingsNakazi {


    private ObservableList<ObjectTypeNakazi> typeNakaziData = FXCollections.observableArrayList();

    @FXML
    public   TableView<ObjectTypeNakazi> table;

    @FXML
    public  TableColumn<ObjectTypeNakazi, Integer> tableId;

    @FXML
    public  TableColumn<ObjectTypeNakazi, String> tableValue;



    static boolean addedNew = false;

    public void initialize(){
        Label l1 = new Label("Немає типів у базі даних. Створіть новий тип!");
        l1.setStyle("-fx-font-size: 15px;");
        l1.setContentDisplay(ContentDisplay.CENTER);
        table.setPlaceholder(l1);
        tableId.setStyle("-fx-alignment: CENTER");

        tableId.setCellValueFactory(new PropertyValueFactory<ObjectTypeNakazi, Integer>("id"));
        tableValue.setCellValueFactory(new PropertyValueFactory<ObjectTypeNakazi, String>("value"));

        initData( );
        table.setItems(typeNakaziData);
    }


    public void initData(){
        typeNakaziData= SqlCommander.getAllTypeNakazi();
    }


    public void apply() {
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }


    public void addNewType() throws IOException {
        LoadWindow.loader.openWindow("/fxml/nakaz/form_settings_add_nakaz_type.fxml","Відділ аспірантури | Створити новий тип",500,150,null,2,0);

        if (addedNew){
            addedNew=false;
            initData();
            table.setItems(typeNakaziData);
            }
        }

    }
