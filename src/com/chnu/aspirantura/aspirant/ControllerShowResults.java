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
    public static int aspirantId;
    public static String aspirantName;

    ObservableList<ObjectResult> result = FXCollections.observableArrayList();
    ObservableList<ObjectResult> resultFiltered = FXCollections.observableArrayList();

    public static boolean changed = false;

    @FXML
    Label title;

    @FXML
    public TableView<ObjectResult> table;

    @FXML
    public TableColumn<ObjectResult, String> tableDisciplineName;

    @FXML
    public TableColumn<ObjectResult, String> tableDisciplineType;

    @FXML
    public TableColumn<ObjectResult, String> tableDisciplineMarkPoints;
    public TableColumn<ObjectResult, String> tableDisciplineMarkNational;
    public TableColumn<ObjectResult, String> tableDisciplineMarkIECTIS;

    @FXML
    public TableColumn<ObjectResult, String> tableDisciplineVykladach;


    @FXML
    public TabPane course;

    @FXML
    public TabPane semestr;

    private int courseValue = 1;
    private int semestrValue = 1;

    public void initialize() {
        title.setText("Аспірант №" + aspirantId + "  " + aspirantName);
        Platform.runLater(() -> title.requestFocus());

        tableDisciplineName.setCellValueFactory(new PropertyValueFactory<ObjectResult, String>("disName"));
        tableDisciplineType.setCellValueFactory(new PropertyValueFactory<ObjectResult, String>("type"));
        tableDisciplineVykladach.setCellValueFactory(new PropertyValueFactory<ObjectResult, String>("vyklName"));

        tableDisciplineMarkPoints.setCellValueFactory(new PropertyValueFactory<ObjectResult, String>("markPoints"));
        tableDisciplineMarkNational.setCellValueFactory(new PropertyValueFactory<ObjectResult, String>("markNational"));
        tableDisciplineMarkIECTIS.setCellValueFactory(new PropertyValueFactory<ObjectResult, String>("markIECTIS"));


        result = SqlCommander.getAllResultsByApirantId(aspirantId);

        filterData();


        course.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {

            if (newTab.getId().equals("diploma")){
                try {
                    ControllerDiploma.aspirantId = aspirantId;
                    LoadWindow.loader.openWindow("/fxml/aspirant/form_diploma.fxml", aspirantName + " | Дисертація ", 475, 435, null, 2, 0);
                    ControllerDiploma.aspirantId = -1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Platform.runLater(()->course.getSelectionModel().select(Integer.parseInt(oldTab.getId())-1));

            }else{

                    courseValue = Integer.parseInt(newTab.getId());
                    filterData();
            }
        });

        semestr.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            semestrValue = Integer.parseInt(newTab.getId()) - 3;
            filterData();
        });

        tableDisciplineName.setCellFactory(new MyCallBackCell());
        tableDisciplineType.setCellFactory(new MyCallBackCell());
        tableDisciplineVykladach.setCellFactory(new MyCallBackCell());
        tableDisciplineMarkPoints.setCellFactory(new MyCallBackCell());
        tableDisciplineMarkNational.setCellFactory(new MyCallBackCell());
        tableDisciplineMarkIECTIS.setCellFactory(new MyCallBackCell());

    }


    public void filterData() {
        resultFiltered = result.filtered((x) -> {
            return (x.getCourse() == courseValue && x.getSemestr() == semestrValue);
        });
        table.setItems(resultFiltered);
    }


    public void apply() {
        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }

    public class MyCallBackCell implements Callback<TableColumn<ObjectResult, String>, TableCell<ObjectResult, String>> {
        @Override
        public TableCell<ObjectResult, String> call(final TableColumn<ObjectResult, String> personStringTableColumn) {
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
                        ObjectResult o = (ObjectResult) cell.getTableRow().getItem();

                        ControllerMark.markPo = String.valueOf(o.getMarkPoints());
                        ControllerMark.markIE = o.getMarkIECTIS();
                        ControllerMark.markNa = o.getMarkNational();

                        ControllerMark.id = o.getId();

                        try {
                            LoadWindow.loader.openWindow("/fxml/aspirant/form_mark.fxml", aspirantName + " | " + o.getDisName(), 315, 180, null, 2, 0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (changed) {
                            changed = false;
                            o.setMarkPoints(ControllerMark.markPo);
                            o.setMarkIECTIS(ControllerMark.markIE);
                            o.setMarkNational(ControllerMark.markNa);

                            ControllerMark.markPo = null;
                            ControllerMark.markNa = null;
                            ControllerMark.markIE = null;
                            ControllerMark.id = -1;
                            TableRow row = cell.getTableRow();
                            row.setItem(o);
                            table.refresh();
                        }
                    }
                }
            });

            return cell;

        }
    }
}
