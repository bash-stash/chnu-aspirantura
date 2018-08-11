package com.chnu.aspirantura.faculty;

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
import java.util.Date;

/**
 * Created by fibs on 30.01.2018.
 */
public class ControllerFaculty {


    private ObservableList<ObjectFaculty> facultyData = FXCollections.observableArrayList();

    static boolean loaded = false;

    @FXML
    public TableView<ObjectFaculty> table;

    @FXML
    private ChoiceBox searchType;

    @FXML
    private Label searchResultCount;

    @FXML
    private TextField textToFind;

    @FXML
    public TableColumn<ObjectFaculty, Integer> tableFacultytId;

    @FXML
    public TableColumn<ObjectFaculty, String> tableFacultyName;

    @FXML
    public TableColumn<ObjectFaculty, Date> tableFacultyStatus;

    static boolean addedNew = false;

    public void initialize(){
        Label l1 = new Label("Немає факультетів у базі даних. Створіть новий факультет!");
        l1.setStyle("-fx-font-size: 20px");
        table.setPlaceholder(l1);



        tableFacultytId.setCellValueFactory(new PropertyValueFactory<ObjectFaculty, Integer>("id"));
        tableFacultytId.setStyle("-fx-alignment: CENTER");
        tableFacultyName.setCellValueFactory(new PropertyValueFactory<ObjectFaculty, String>("name"));
        tableFacultyStatus.setCellValueFactory(new PropertyValueFactory<ObjectFaculty, Date>("status"));
        initData( );
        table.setItems(facultyData);
        searchResultCount.setText("Всього результатів: " + facultyData.size());


        searchType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                find();
            }
        });

        loaded = true;
    }


    public void initData(){
        facultyData= SqlCommander.getAllFaculty();
    }


    public void loadFormAddFaculty() throws IOException {
        LoadWindow.loader.openWindow("/fxml/faculty/form_add_faculty.fxml","Відділ аспірантури | Додати факультет",500,150,null,2,0);
        if (addedNew){
                addedNew=false;
                refreshData();
            }
    }


    public void loadFormEditFaculty() throws IOException {
        ControllerEditFaculty.facultyData = facultyData;

        ObjectFaculty o = table.getSelectionModel().getSelectedItem();

        if(o!=null) {
            System.out.println(o.getName());
            ControllerEditFaculty.faculty = o;
        }

        LoadWindow.loader.openWindow("/fxml/faculty/form_edit_faculty.fxml","Відділ аспірантури | Редагувати факультет",500,250,null,2,0);
        if (addedNew){
            addedNew=false;
            refreshData();
        }

    }



    public void refreshData(){
        initData();
        table.setItems(facultyData);
        searchResultCount.setText("Всього результатів: " + facultyData.size());
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
            for (ObjectFaculty faculty : facultyData) {
                if (searchKind.equals("Назва факультету")) if (faculty.getName().toLowerCase().contains(toFind.toLowerCase())) {
                    table.getItems().add(faculty);
                    i++;
                }
            }
            searchResultCount.setText("Знайдено: " + i);
        }else{
            Label l1 = new Label("Немає факультетів у базі даних. Створіть новий факультет!");
            l1.setStyle("-fx-font-size: 20px");
            table.setPlaceholder(l1);

            table.setItems(facultyData);
            searchResultCount.setText("Всього результатів: " + facultyData.size());
        }
    }
    public void clear(){
        textToFind.setText("");
        table.setItems(facultyData);
        searchResultCount.setText("Всього результатів: " + facultyData.size());
        Platform.runLater( () -> ControllerAspirant.root.requestFocus());
    }
}