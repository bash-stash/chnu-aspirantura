package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ControllerDebt {

    static ObjectDebt debt;

    @FXML
    private ComboBox<Integer> startYearComboBox;

    @FXML
    private ComboBox<Integer> endYearComboBox;

    @FXML
    private ComboBox<Integer> startCourseComboBox;

    @FXML
    private ComboBox<Integer> endCourseComboBox;

    @FXML
    private ComboBox<Integer> startSemestrComboBox;

    @FXML
    private ComboBox<Integer> endSemestrComboBox;


    public void initialize() {

        ObservableList<Integer> listYears = FXCollections.observableArrayList();
        ObservableList<Integer> listCourse = FXCollections.observableArrayList();
        ObservableList<Integer> listSemestr = FXCollections.observableArrayList();

        for (int i = LocalDate.now().getYear(); i >= 2016; i--) {
            listYears.add(i);
        }

        listCourse.add(1);
        listCourse.add(2);
        listCourse.add(3);

        listSemestr.add(1);
        listSemestr.add(2);


        startCourseComboBox.setItems(listCourse);
        endCourseComboBox.setItems(listCourse);

        startSemestrComboBox.setItems(listSemestr);
        endSemestrComboBox.setItems(listSemestr);

        startYearComboBox.setItems(listYears);
        endYearComboBox.setItems(listYears);

        startYearComboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                endYearComboBox.setItems(listYears.filtered((x)->x>=newValue));
            }
        });

        startCourseComboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                endCourseComboBox.setItems(listCourse.filtered((x)->x>=newValue));
            }
        });

        startSemestrComboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                endSemestrComboBox.setItems(listSemestr.filtered((x)->x>=newValue));
            }
        });

        startCourseComboBox.getSelectionModel().select(0);
        endCourseComboBox.getSelectionModel().select(2);

        startSemestrComboBox.getSelectionModel().select(0);
        endSemestrComboBox.getSelectionModel().select(1);

        startYearComboBox.getSelectionModel().select(2);
        endYearComboBox.getSelectionModel().select(0);

    }


    public void apply() {
        debt = null;

        try {

            int startYear = startYearComboBox.getSelectionModel().getSelectedItem();
            int endYear = endYearComboBox.getSelectionModel().getSelectedItem();
            int startCourse = startCourseComboBox.getSelectionModel().getSelectedItem();
            int endCourse = endCourseComboBox.getSelectionModel().getSelectedItem();
            int startSemestr = startSemestrComboBox.getSelectionModel().getSelectedItem();
            int endSemestr = endSemestrComboBox.getSelectionModel().getSelectedItem();

            debt = new ObjectDebt(startYear, endYear, startCourse, endCourse, startSemestr, endSemestr);
            cancel();

        } catch (Exception e) {
            Utils.showErrorMessage("Не обраний параметр");
        }

    }

    public void cancel() {
        Stage stage = (Stage) startCourseComboBox.getScene().getWindow();
        stage.close();
    }
}
