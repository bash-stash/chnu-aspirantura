package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.LoadWindow;
import com.chnu.aspirantura.SqlCommander;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;



public class ControllerShowResults {
    public  static int aspirantId;
    public  static String aspirantName;
    ObservableList<ObjectResult> result = FXCollections.observableArrayList();
    ObservableList<ObjectResult> resultFiltered = FXCollections.observableArrayList();

    public static boolean changed=false;

    @FXML Label title;

    @FXML
    public TableView<ObjectResult> table;

    @FXML
    public TableColumn<ObjectResult, String> tableDisciplineName;

    @FXML
    public  TableColumn<ObjectResult, String> tableDisciplineType;

    @FXML
    public TableColumn<ObjectResult, String> tableDisciplineMark;

    @FXML
    public TableColumn<ObjectResult, String> tableDisciplineVykladach;


    @FXML
    public TabPane course;

    @FXML
    public TabPane semestr;

    int courseValue = 1;
    int semestrValue = 1;

    public void initialize(){
        title.setText("Аспірант №"+aspirantId+"  "+aspirantName);
        Platform.runLater( () -> title.requestFocus());

        tableDisciplineName.setCellValueFactory(new PropertyValueFactory<ObjectResult, String>("disName"));
        tableDisciplineType.setCellValueFactory(new PropertyValueFactory<ObjectResult, String>("type"));
        tableDisciplineMark.setCellValueFactory(new PropertyValueFactory<ObjectResult, String>("mark"));
        tableDisciplineVykladach.setCellValueFactory(new PropertyValueFactory<ObjectResult, String>("vyklName"));


        result = SqlCommander.getAllResultsByApirantId(aspirantId);

        filterData();


        course.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            courseValue = Integer.parseInt(newTab.getId());
            filterData();
        });

        semestr.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            semestrValue = Integer.parseInt(newTab.getId())-3;
            filterData();
        });




        tableDisciplineMark.setCellFactory(new Callback<TableColumn<ObjectResult, String>, TableCell<ObjectResult, String>>() {
            @Override public TableCell<ObjectResult, String> call(final TableColumn<ObjectResult, String> personStringTableColumn) {
                TableCell cell = new TableCell<ObjectResult, String>() {
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

                            ObjectResult o = (ObjectResult) cell.getTableRow().getItem();
                            ControllerMark.mark = c.getText();
                            ControllerMark.id = o.getId();

                            try {
                                LoadWindow.loader.openWindow("/fxml/aspirant/form_mark.fxml",aspirantName+" | "+o.getDisName(),315,100,null,2,0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (changed){
                                changed=false;
                                o.setMark(ControllerMark.mark);
                                ControllerMark.mark=null;
                                ControllerMark.id=-1;
                                cell.setText(o.getMark());
                            }
                        }
                    }
                });

                return cell;

            }
        });
    }


    public void filterData() {
        resultFiltered = result.filtered((x)->{
            return (x.getCourse()==courseValue && x.getSemestr()==semestrValue);
        });
        table.setItems(resultFiltered);
    }


    public void apply(){
        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }
}
