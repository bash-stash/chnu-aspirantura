package com.chnu.aspirantura.discipline;

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


public class ControllerDiscipline {

    private ObservableList<ObjectDiscipline> disciplinesData = FXCollections.observableArrayList();

    static boolean addedNew = false;
    static boolean loaded = false;


    @FXML
    public RadioButton dailyForm;

    @FXML
    public RadioButton zaochnaForm;

    @FXML
    public RadioButton eveningForm;

    @FXML
    public ToggleGroup form;


    @FXML
    public TableView<ObjectDiscipline> table;
    @FXML
    private ChoiceBox searchType;
    @FXML
    private Label searchResultCount;
    @FXML
    private TextField textToFind;
    @FXML
    public TableColumn<ObjectDiscipline, Integer> tableDisciplineId;
    @FXML
    public TableColumn<ObjectDiscipline, String> tableDisciplineName;
    @FXML
    public TableColumn<ObjectDiscipline, String> tableDisciplineStatus;
    @FXML
    public TableColumn<ObjectDiscipline, String> tableDisciplineCourse;
    @FXML
    public TableColumn<ObjectDiscipline, String> tableDisciplineSemestr;
    @FXML
    public TableColumn<ObjectDiscipline, String> tableDisciplineVykladach;
    @FXML
    public TableColumn<ObjectDiscipline, String> tableDisciplineSpeciality;

    @FXML
    public TableColumn<ObjectDiscipline, String> tableDisciplineType;
    int formValue =1;

    public void initialize(){
        Label l1 = new Label("Немає дисциплін у базі даних. Додайте нову дисципіну!");
        l1.setStyle("-fx-font-size: 20px");
        table.setPlaceholder(l1);
        tableDisciplineId.setStyle("-fx-alignment: CENTER");


        tableDisciplineId.setCellValueFactory(new PropertyValueFactory<ObjectDiscipline, Integer>("id"));
        tableDisciplineName.setCellValueFactory(new PropertyValueFactory<ObjectDiscipline, String>("name"));
        tableDisciplineSpeciality.setCellValueFactory(new PropertyValueFactory<ObjectDiscipline, String>("speciality"));
        tableDisciplineCourse.setCellValueFactory(new PropertyValueFactory<ObjectDiscipline, String>("course"));
        tableDisciplineSemestr.setCellValueFactory(new PropertyValueFactory<ObjectDiscipline, String>("semestr"));
        tableDisciplineType.setCellValueFactory(new PropertyValueFactory<ObjectDiscipline, String>("typeKontrol"));
        tableDisciplineVykladach.setCellValueFactory(new PropertyValueFactory<ObjectDiscipline, String>("vykladach"));
        tableDisciplineStatus.setCellValueFactory(new PropertyValueFactory<ObjectDiscipline, String>("status"));

        initData(formValue);


        form  = dailyForm.getToggleGroup();
        form.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (form.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) form.getSelectedToggle();
                    switch (button.getText()){
                        case "Денна форма":
                            formValue =1;
                            break;
                        case "Заочна форма":
                            formValue =2;
                            break;
                        case "Вечірня форма":
                            formValue = 3;
                            break;
                    }

                }
                refreshData();
            }
        });


        table.setItems(disciplinesData);
        searchResultCount.setText("Всього результатів: " + disciplinesData.size());


        searchType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                find();
            }
        });
        loaded = true;

    }


    public void initData(int form){
        disciplinesData = SqlCommander.getAllDisciplines(form);
    }

    public void loadFormAddDiscipline() throws IOException {
        LoadWindow.loader.openWindow("/fxml/discipline/form_add_discipline.fxml","Відділ аспірантури | Додати нову дисципліну\"",500,370,null,2,0);
        if (addedNew){
            addedNew=false;
            refreshData();
        }
    }
    public void loadFormSettings() throws IOException {
        LoadWindow.loader.openWindow("/fxml/discipline/form_settings_discipline.fxml","Відділ аспірантури | Дисципліни | Налаштування",480,370,null,2,0);
    }



    public void loadFormEditDiscipl() throws IOException {
        ControllerEditDiscipline.disciplinesData = disciplinesData;

        ObjectDiscipline o = table.getSelectionModel().getSelectedItem();

        if(o!=null){
            ControllerEditDiscipline.discipline=o;
        }

        LoadWindow.loader.openWindow("/fxml/discipline/form_edit_discipline.fxml","Відділ аспірантури | Редагувати дисципліну",500,370,null,2,0);
        if (addedNew){
            addedNew=false;
            refreshData();
        }
    }


    public void refreshData(){
        initData(formValue);
        table.setItems(disciplinesData);
        searchResultCount.setText("Всього результатів: " + disciplinesData.size());
    }


    public void clear(){
        textToFind.setText("");
        table.setItems(disciplinesData);
        searchResultCount.setText("Всього результатів: " + disciplinesData.size());
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
            for (ObjectDiscipline discipline: disciplinesData) {
                if (searchKind.equals("Назва дисципліни")) {
                    if (discipline.getName().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(discipline);
                        i++;
                    }
                }

                if (searchKind.equals("Номер симестру")) {
                    if ((discipline.getSemestr()+"").contains(toFind.toLowerCase())) {
                        table.getItems().add(discipline);
                        i++;
                    }
                }

                if (searchKind.equals("Назва спеціальності")) {
                    if (discipline.getSpeciality().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(discipline);
                        i++;
                    }
                }

                if (searchKind.equals("Тип контролю")) {
                    if (discipline.getTypeKontrol().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(discipline);
                        i++;
                    }
                }

                if (searchKind.equals("Викладач")) {
                    if (discipline.getVykladach().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(discipline);
                        i++;
                    }
                }
            }
            searchResultCount.setText("Знайдено: " + i);
        }else{
            Label l1 = new Label("Немає наказів у базі даних. Створіть новий наказ!");
            l1.setStyle("-fx-font-size: 20px");
            table.setPlaceholder(l1);
            table.setItems(disciplinesData);
            searchResultCount.setText("Всього результатів: " + disciplinesData.size());
        }
    }
}