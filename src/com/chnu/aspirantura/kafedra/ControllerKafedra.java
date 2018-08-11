package com.chnu.aspirantura.kafedra;

import com.chnu.aspirantura.LoadWindow;
import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.aspirant.ControllerAspirant;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;


public class ControllerKafedra {
    private ObservableList<ObjectKafedra> kafedraData = FXCollections.observableArrayList();

    static boolean loaded = false;

    @FXML
    public TableView<ObjectKafedra> table;

    @FXML
    private ChoiceBox searchType;

    @FXML
    private Label searchResultCount;

    @FXML
    private TextField textToFind;


    @FXML
    public TableColumn<ObjectKafedra, Integer> tableKafedraId;

    @FXML
    public TableColumn<ObjectKafedra, String> tableKafedraName;

    @FXML
    public TableColumn<ObjectKafedra, String> tableKafedraSpeciality;

    @FXML
    public TableColumn<ObjectKafedra, String> tableKafedraRoom;

    static boolean addedNew = false;

    public void initialize() {
        Label l1 = new Label("Немає жодної кафедри у базі даних. Створіть нову кафедру!");
        l1.setStyle("-fx-font-size: 20px");
        table.setPlaceholder(l1);

        tableKafedraId.setCellValueFactory(new PropertyValueFactory<ObjectKafedra, Integer>("id"));
        tableKafedraId.setStyle("-fx-alignment: CENTER");

        tableKafedraName.setCellValueFactory(new PropertyValueFactory<ObjectKafedra, String>("name"));
        tableKafedraSpeciality.setCellValueFactory(new PropertyValueFactory<ObjectKafedra, String>("speciality"));
        tableKafedraRoom.setCellValueFactory(new PropertyValueFactory<ObjectKafedra, String>("room"));


        initData();

        table.setItems(kafedraData);
        searchResultCount.setText("Всього результатів: " + kafedraData.size());


        searchType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                find();
            }
        });

        loaded = true;
    }


    public void initData() {
        kafedraData = SqlCommander.getAllKafedras();
    }


    public void loadFormAddKafedra() throws IOException {
        LoadWindow.loader.openWindow("/fxml/kafedra/form_add_kafedra.fxml","Відділ аспірантури | Додати кафедру",485,230,null,2,0);

        if (addedNew) {
            addedNew = false;
            refreshData();
        }
    }


    public void loadFormEditKafedra() throws IOException {
        ControllerEditKafedra.kafedraData= kafedraData;

        ObjectKafedra o = table.getSelectionModel().getSelectedItem();

        if (o != null) {
            System.out.println(o.getName());
            ControllerEditKafedra.kafedra = o;
        }

        LoadWindow.loader.openWindow("/fxml/kafedra/form_edit_kafedra.fxml","Відділ аспірантури | Редагувати кафедру",485,230,null,2,0);

        if (addedNew) {
            addedNew = false;
            refreshData();
        }

    }


    public void refreshData() {
        initData();
        table.setItems(kafedraData);
        searchResultCount.setText("Всього результатів: " + kafedraData.size());
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
            for (ObjectKafedra kafedra : kafedraData) {
                if (searchKind.equals("Назва кафедри"))
                    if (kafedra.getName().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(kafedra);
                        i++;
                    }

                if (searchKind.equals("Назва спеціальності"))
                    if (kafedra.getSpeciality().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(kafedra);
                        i++;
                    }

                if (searchKind.equals("Кімната кафедри"))
                    if (kafedra.getRoom().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(kafedra);
                        i++;
                    }

            }
            searchResultCount.setText("Знайдено: " + i);
        } else {
            Label l1 = new Label("Немає жодної кафедри у базі даних. Створіть нову кафедру!");
            l1.setStyle("-fx-font-size: 20px");
            table.setPlaceholder(l1);

            table.setItems(kafedraData);
            searchResultCount.setText("Всього результатів: " + kafedraData.size());
        }
    }

    public void clear(){
        textToFind.setText("");
        table.setItems(kafedraData);
        searchResultCount.setText("Всього результатів: " + kafedraData.size());
        Platform.runLater( () -> ControllerAspirant.root.requestFocus());
    }
}