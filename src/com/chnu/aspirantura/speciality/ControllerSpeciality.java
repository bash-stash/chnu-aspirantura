package com.chnu.aspirantura.speciality;

import com.chnu.aspirantura.LoadWindow;
import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.aspirant.ControllerAspirant;
import com.chnu.aspirantura.faculty.ObjectFaculty;
import com.chnu.aspirantura.kafedra.ObjectKafedra;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;

/**
 * Created by fibs on 30.01.2018.
 */
public class ControllerSpeciality extends SpecialityData {

    static boolean loaded = false;

    @FXML
    public TableView<ObjectSpeciality> table;

    @FXML
    private ChoiceBox searchType;

    @FXML
    private Label searchResultCount;

    @FXML
    private TextField textToFind;

    @FXML
    public TableColumn<ObjectSpeciality, Integer> tableSpecialityId;

    @FXML
    public TableColumn<ObjectSpeciality, String> tableSpecialityCode;

    @FXML
    public TableColumn<ObjectSpeciality, String> tableSpecialityName;

    @FXML
    public TableColumn<ObjectSpeciality, String> tableSpecialityStatus;

    @FXML
    public TableColumn<ObjectSpeciality, String> tableSpecialityFaculty;

    @FXML
    public TableColumn<ObjectSpeciality, String> tableSpecialityKafedra;

    static boolean addedNew = false;

    public void initialize(){
        Label l1 = new Label("Немає спеціальностей у базі даних. Створіть нову спеціальність!");
        l1.setStyle("-fx-font-size: 20px");
        table.setPlaceholder(l1);
        tableSpecialityId.setStyle("-fx-alignment: CENTER");
        tableSpecialityId.setCellValueFactory(new PropertyValueFactory<ObjectSpeciality, Integer>("id"));
        tableSpecialityCode.setCellValueFactory(new PropertyValueFactory<ObjectSpeciality, String>("code"));
        tableSpecialityName.setCellValueFactory(new PropertyValueFactory<ObjectSpeciality, String>("name"));


        tableSpecialityFaculty.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObjectSpeciality, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObjectSpeciality, String> cd) {
                ObjectFaculty o = cd.getValue().getFaculty();
                return  (o!=null) ? new SimpleStringProperty(o.getName()) : new SimpleStringProperty("");
            }

        });


        tableSpecialityKafedra.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObjectSpeciality, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObjectSpeciality, String> cd) {
                ObjectKafedra o = cd.getValue().getKafedra();
                return  (o!=null) ? new SimpleStringProperty(o.getRoom()+" | " + o.getName()) : new SimpleStringProperty("");
            }

        });


        searchType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                find();
            }
        });

        tableSpecialityStatus.setCellValueFactory(new PropertyValueFactory<ObjectSpeciality, String>("status"));

        refreshData();
        loaded = true;
    }


    public void initData(){
        specialityData = null;
        kafedraData=null;
        facultyData = null;

        specialityData = SqlCommander.getAllSpeciality();
        kafedraData = SqlCommander.getAllKafedras();
        facultyData = SqlCommander.getAllFaculty();
    }



    public void loadFormAddSpeciality() throws IOException {
        LoadWindow.loader.openWindow("/fxml/speciality/form_add_speciality.fxml","Відділ аспірантури | Додати спеціальність",500,380,null,2,0);

        if (addedNew){
            addedNew=false;
            refreshData();
        }
    }


    public void loadFormEditSpeciality() throws IOException {
        ObjectSpeciality o = table.getSelectionModel().getSelectedItem();

        if(o!=null) {

            System.out.println(o.getName());
            ControllerEditSpeciality.speciality = o;
        }

        LoadWindow.loader.openWindow("/fxml/speciality/form_edit_speciality.fxml","Відділ аспірантури | Редагувати спеціальність",500,380,null,2,0);

        if (addedNew){
            addedNew=false;
            refreshData();
        }

    }



    public void refreshData(){
        table.setItems(null);
        initData();
        table.setItems(SpecialityData.specialityData);
        searchResultCount.setText("Всього результатів: " + specialityData.size());
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
            for (ObjectSpeciality speciality : specialityData) {
                if (searchKind.equals("Назва спеціальності"))
                    if (speciality.getName().toLowerCase().contains(toFind.toLowerCase())) {
                    table.getItems().add(speciality);
                    i++;
                }

                if (searchKind.equals("Назва факультету"))
                    if (speciality.getFaculty().getName().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(speciality);
                        i++;
                    }


                if (searchKind.equals("Код спеціальності"))
                    if (speciality.getCode().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(speciality);
                        i++;
                    }


                if (searchKind.equals("Кафедра"))
                    if (speciality.getKafedra().getName().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(speciality);
                        i++;
                    }


            }
            searchResultCount.setText("Знайдено: " + i);
        }else{
            Label l1 = new Label("Немає спеціальностей у базі даних. Створіть нову спеціальність!");
            l1.setStyle("-fx-font-size: 20px");
            table.setPlaceholder(l1);

            table.setItems(specialityData);
            searchResultCount.setText("Всього результатів: " + specialityData.size());
        }
    }
    public void clear(){
        textToFind.setText("");
        table.setItems(specialityData);
        searchResultCount.setText("Всього результатів: " + specialityData.size());
        Platform.runLater( () -> ControllerAspirant.root.requestFocus());
    }

}
