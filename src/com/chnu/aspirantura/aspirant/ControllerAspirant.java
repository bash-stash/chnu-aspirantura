package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.LoadWindow;
import com.chnu.aspirantura.SqlCommander;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Date;

/**
 * Created by fibs on 30.01.2018.
 */
public class ControllerAspirant {

    public static final String CSS_DISMISSED = "cell-renderer-dismiss-student";
    public static final String CSS_ACTIVE = "cell-renderer-active-student";
    public static final String CSS_STOPPED= "cell-renderer-stopped-student";
    public static final String CSS_FINISHED = "cell-renderer-finished-student";


   ObservableList<ObjectAspirant> aspirantsData = FXCollections.observableArrayList();

    static boolean loaded=false;
    public static BorderPane root;
    static boolean addedNew = false;



    @FXML
    public ChoiceBox searchType;

    @FXML
    private Label searchResultCount;

    @FXML
    public TextField textToFind;

    @FXML
    public  TableView<ObjectAspirant> table;


    @FXML
    public  TableColumn<ObjectAspirant, Integer> tableAspirantId;

    @FXML
    public  TableColumn<ObjectAspirant, String> tableAspirantName;

    @FXML
    public TableColumn<ObjectAspirant, Date> tableAspirantDate;

    @FXML
    public TableColumn<ObjectAspirant, String> tableAspirantSpeciality;

    @FXML
    public TableColumn<ObjectAspirant, String> tableAspirantStatus;

    @FXML
    public TableColumn<ObjectAspirant, String> tableAspirantKerivnik;

    @FXML
    public TableColumn<ObjectAspirant, Integer> tableAspirantYear;

    @FXML
    public TableColumn<ObjectAspirant, String> tableAspirantNote;

    @FXML
    public TableColumn<ObjectAspirant, String> tableAspirantForm;



    public TableView<ObjectAspirant> getTable() {
        return table;
    }

    @FXML
    private void initialize(){



        addedNew=true;
        loaded=false;

        Label l1 = new Label("Немає жодного аспіранта у базі даних. Додайте нового аспіранта!");
        l1.setStyle("-fx-font-size: 20px");

        table.setPlaceholder(l1);
//        tableVykladachId.setStyle("-fx-alignment: CENTER");

        Platform.runLater( () -> root.requestFocus());

        if (!loaded) {
            MenuItem mi1 = new MenuItem("Переглянути інформацію");
            mi1.setOnAction((ActionEvent event) -> {
                ObjectAspirant item = table.getSelectionModel().getSelectedItem();
                ControllerShowInfoAspirant.aspirant = item;
                try {
                    LoadWindow.loader.openWindow("/fxml/aspirant/form_show_info_aspirant.fxml","Відділ аспірантури | " + item.getName(),600,730,null,2,0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            MenuItem mi2 = new MenuItem("Редагувати інформацію");
            mi2.setOnAction((ActionEvent event) -> {


                ObjectAspirant item = table.getSelectionModel().getSelectedItem();
                ControllerEditInfoAspirant.aspirant = item;

                try {
                    LoadWindow.loader.openWindow("/fxml/aspirant/form_edit_info_aspirant.fxml","Відділ аспірантури | " + item.getName(),600,730,null,2,0);
                    if (addedNew){
                        addedNew=false;
                        loadData();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

            MenuItem mi3 = new MenuItem("Переглянути історію змін");
            mi3.setOnAction((ActionEvent event) -> {

                ObjectAspirant item = table.getSelectionModel().getSelectedItem();
                ControllerShowHistoryAspirant.aspirantId = item.getId();
                ControllerShowHistoryAspirant.aspirantName = item.getName();
                try {
                    LoadWindow.loader.openWindow("/fxml/aspirant/form_show_history_aspirant.fxml","Відділ аспірантури | Історія змін | " + item.getName(),600,500,null,2,0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

            MenuItem mi4 = new MenuItem("Переглянути наукову активність");
            mi4.setOnAction((ActionEvent event) -> {

                ObjectAspirant item = table.getSelectionModel().getSelectedItem();
                ControllerShowScience.aspirantId = item.getId();
                ControllerShowScience.aspirantName = item.getName();


                try {
                    LoadWindow.loader.openWindow("/fxml/aspirant/form_show_science_aspirant.fxml","Відділ аспірантури | Наукова активність | " + item.getName(),600,500,null,2,0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            MenuItem mi5 = new MenuItem("Переглянути результати навчання");
            mi5.setOnAction(mi4.getOnAction());
            mi5.setOnAction((ActionEvent event) -> {

                ObjectAspirant item = table.getSelectionModel().getSelectedItem();
                ControllerShowResults.aspirantId = item.getId();
                ControllerShowResults.aspirantName = item.getName();

                try {
                    LoadWindow.loader.openWindow("/fxml/aspirant/form_show_results_aspirant.fxml","Відділ аспірантури | Результати | " + item.getName(),600,500,null,2,0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });



            ContextMenu menu = new ContextMenu();
            menu.getItems().addAll(mi1, mi2,new SeparatorMenuItem(),mi5,mi4,new SeparatorMenuItem(),mi3);
            table.setContextMenu(menu);

            tableAspirantId.setCellValueFactory(new PropertyValueFactory<ObjectAspirant, Integer>("id"));
            tableAspirantId.setStyle("-fx-alignment: CENTER");
            tableAspirantName.setCellValueFactory(new PropertyValueFactory<ObjectAspirant, String>("name"));
            tableAspirantDate.setCellValueFactory(new PropertyValueFactory<ObjectAspirant, Date>("date"));
            tableAspirantSpeciality.setCellValueFactory(new PropertyValueFactory<ObjectAspirant, String>("speciality"));
            tableAspirantKerivnik.setCellValueFactory(new PropertyValueFactory<ObjectAspirant, String>("kerivnik"));
            tableAspirantStatus.setCellValueFactory(new PropertyValueFactory<ObjectAspirant, String>("statusLabel"));
            tableAspirantYear.setCellValueFactory(new PropertyValueFactory<ObjectAspirant, Integer>("year"));
            tableAspirantForm.setCellValueFactory(new PropertyValueFactory<ObjectAspirant, String>("form"));

            tableAspirantNote.setCellValueFactory(new PropertyValueFactory<ObjectAspirant, String>("note"));




            tableAspirantNote.setCellFactory(new Callback<TableColumn<ObjectAspirant, String>, TableCell<ObjectAspirant, String>>() {
                @Override public TableCell<ObjectAspirant, String> call(final TableColumn<ObjectAspirant, String> personStringTableColumn) {
                    TableCell cell = new TableCell<ObjectAspirant, String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? null : getString());
                            setGraphic(null);
                        }

                        private String getString() {
                            return getItem() == null ? "" : getItem();
                        }
                    };

                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() > 1) {
                                TableCell c = (TableCell) event.getSource();
                                ObjectAspirant o = (ObjectAspirant)cell.getTableRow().getItem();
                                ControllerNote.note =c.getText();
                                ControllerNote.aspirantId = o.getId();

                                try {
                                    LoadWindow.loader.openWindow("/fxml/aspirant/form_note.fxml",o.getName(),490,200,null,2,0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (addedNew){
                                    addedNew=false;
                                    o.setNote(ControllerNote.note);
                                    ControllerNote.note=null;
                                    ControllerNote.aspirantId=-1;
                                    cell.setText(o.getNote());
                                }
                            }
                        }
                    });
                    Text text = new Text();
                    cell.setGraphic(text);
                    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                    text.wrappingWidthProperty().bind(tableAspirantNote.widthProperty());
                    text.textProperty().bind(cell.itemProperty());
                    return cell;

                }
            });
            tableAspirantStatus.setCellFactory(new Callback<TableColumn<ObjectAspirant, String>, TableCell<ObjectAspirant, String>>() {
                @Override public TableCell<ObjectAspirant, String> call(final TableColumn<ObjectAspirant, String> personStringTableColumn) {
                    return new BackgroundTableCell();
                }
            });

            loadData();
            searchResultCount.setText("Всього результатів: " + aspirantsData.size());



            searchType.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    find();
                }
            });


            loaded = true;
        }
    }

    public void loadData() {
        aspirantsData = SqlCommander.getAllAspirants();
        table.setItems(aspirantsData);
    }



    @FXML public void loadFormPractica(Event event) throws IOException {

        ControllerPraktika.aspirantsData = aspirantsData;

        ObjectAspirant as = table.getSelectionModel().getSelectedItem();

        if(as!=null) {
            System.out.println(as.getName());
            ControllerPraktika.actionId = as.getId();
            ControllerPraktika.aspirant = as;
        }
        try {
            LoadWindow.loader.openWindow("/fxml/aspirant/form_praktica_aspirant.fxml","Відділ аспірантури | Оформлення практики",500,325,null,2,0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    @FXML public void loadFormActions(Event event) throws IOException {

        ControllerActions.aspirantsData = aspirantsData;

        ObjectAspirant as = table.getSelectionModel().getSelectedItem();

        if(as!=null) {
            System.out.println(as.getName());
            ControllerActions.actionId = as.getId();
            ControllerActions.aspirant = as;
        }

        AnchorPane pane = (AnchorPane) event.getSource();
        String title="";
        switch (pane.getId()){
            case "pauseAspirantPane":
                ControllerActions.actionId = 1;
                title = ("Відділ аспірантури | Оформлення перерви");
                break;
            case "recoverAspirantPane":
                ControllerActions.actionId = 2;
                title = ("Відділ аспірантури | Поновлення після перерви");
                break;
            case "finishAspirantPane":
                ControllerActions.actionId = 3;
                title = ("Відділ аспірантури | Закінчення аспірантури");
                break;
            case "dismissAspirantPane":
                ControllerActions.actionId = 5;
                title = ("Відділ аспірантури | Відрахування з аспірантури");
                break;
        }


        try {
            LoadWindow.loader.openWindow("/fxml/aspirant/form_actions_aspirant.fxml",title,515,200,null,2,0);
            if (addedNew){
                addedNew=false;
                loadData();
            }
            ControllerActions.actionId=0;
            ControllerActions.aspirant=null;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @FXML public void loadFormAddAspirant() throws IOException {
        try {
            LoadWindow.loader.openWindow("/fxml/aspirant/form_add_aspirant.fxml","Відділ аспірантури | Прийом в аспірантуру",600,730,"paneAspirant",2,2);
            if (addedNew){
                addedNew=false;
                loadData();
                searchResultCount.setText("Всього результатів: " + aspirantsData.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void removeTableColumns(){
        table.getColumns().remove(1, table.getColumns().size());
    }


    public void find() {
        String toFind = textToFind.getText();
        if (!toFind.equals("")) {
            String searchKind = (String) searchType.getValue();
            table.setItems(FXCollections.observableArrayList());
            int i = 0;
            for (ObjectAspirant aspirant : aspirantsData) {
                if (searchKind.equals("Ім'я аспіранта"))
                    if (aspirant.getName().toLowerCase().contains(toFind.toLowerCase())) {
                    table.getItems().add(aspirant);
                    i++;
                }

                if (searchKind.equals("Спеціальність"))
                    if (aspirant.getSpeciality().toLowerCase().contains(toFind.toLowerCase())) {
                        table.getItems().add(aspirant);
                        i++;
                    }

                if (searchKind.equals("Керівник аспіранта")) {
                    if (aspirant.getKerivnik().toLowerCase().contains(toFind.toLowerCase())) {

                        table.getItems().add(aspirant);
                        i++;
                    }
                }
                if (searchKind.equals("Кабінет кафедри")) {
                    if (aspirant.getKafedraKabinet().toLowerCase().contains(toFind.toLowerCase())) {

                        table.getItems().add(aspirant);
                        i++;
                    }
                }
            }
            searchResultCount.setText("Знайдено: " + i);
        }else{
            table.setItems(aspirantsData);
            searchResultCount.setText("Всього результатів: " + aspirantsData.size());
        }
    }

    public void clear(){
        table.setItems(aspirantsData);
        textToFind.setText("");
        searchResultCount.setText("Всього результатів: " + aspirantsData.size());
        Platform.runLater( () -> ControllerAspirant.root.requestFocus());
    }

    private static class BackgroundTableCell extends TableCell<ObjectAspirant, String> {
        @Override protected void updateItem(final String item, final boolean empty) {
            super.updateItem(item, empty);

            setText(empty ? "" : item);

            getStyleClass().removeAll(CSS_FINISHED, CSS_STOPPED, CSS_ACTIVE, CSS_DISMISSED);
            updateStyles(empty ? null : item);
        }

        private void updateStyles(String item) {
            if (item == null) {
                return;
            }

            if (item.equals("Перерва")) {
                getStyleClass().add(CSS_STOPPED);
            }
            else if (item.equals("Активно")) {
                getStyleClass().add(CSS_ACTIVE);
            }
            else if (item.equals("Закінчено")) {
                getStyleClass().add(CSS_FINISHED);
            }
            else if (item.equals("Відраховано")) {
                getStyleClass().add(CSS_DISMISSED);
            }
        }
    }

}

