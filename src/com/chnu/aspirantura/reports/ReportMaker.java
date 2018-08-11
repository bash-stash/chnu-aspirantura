package com.chnu.aspirantura.reports;

import com.chnu.aspirantura.ControllerLogin;
import com.chnu.aspirantura.Main;
import com.chnu.aspirantura.aspirant.ObjectAspirant;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportMaker {

    @FXML
    private Label title;


    public void initialize(){

    }

    public void apply() throws ParseException, IOException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss ");

        String filter = (String) Main.controllerAspirant.searchType.getValue();
        String filterValue=Main.controllerAspirant.textToFind.getText();


        XlsxBuilder xlsxBuilder = new XlsxBuilder();

        xlsxBuilder.startSheet("Звіт аспіранти").                            // start with sheet
                startRow().                                          // then go row by row
                setRowTitleHeight().                                 // set row style, add borders and so on
                addTitleTextColumn("Звіт аспіранти").                    // add columns
                startRow().                                          // another row
                addBoldTextLeftAlignedColumn((filterValue.equals("")?"":"Фільтр - "+filter+" = "+filterValue)).
                startRow().                                          // another row
                setRowTitleHeight().                                 // row styling
                setRowThinBottomBorder().
                addBoldTextLeftAlignedColumn("Створив: "+ ControllerLogin.login +"     Дата:  " + dateFormat.format(new Date())).
                startRow().                                          // empty row
                startRow().                                          // header row
                setRowTitleHeight().
                setRowThickTopBorder().
                setRowThickBottomBorder().
                addBoldTextCenterAlignedColumn("№").
                addBoldTextCenterAlignedColumn("Ім'я").
                addBoldTextCenterAlignedColumn("Дата народження").
                addBoldTextCenterAlignedColumn("Науковий керівник").
                addBoldTextCenterAlignedColumn("Спеціальність").
                addBoldTextCenterAlignedColumn("Статус").
                addBoldTextCenterAlignedColumn("Рік зарахування").
                startRow();

                for(ObjectAspirant aspirant: Main.controllerAspirant.table.getItems() ){
                    xlsxBuilder.addBoldTextCenterAlignedColumn(String.valueOf(aspirant.getId())).
                            addTextLeftAlignedColumn(aspirant.getName()).
                            addTextLeftAlignedColumn(aspirant.getDate()).
                            addTextLeftAlignedColumn(aspirant.getKerivnik()).
                            addTextLeftAlignedColumn(aspirant.getSpeciality()).
                            addTextLeftAlignedColumn(aspirant.getStatusLabel()).
                            addTextLeftAlignedColumn(String.valueOf(aspirant.getYear())).
                            startRow();
                }

                xlsxBuilder.setRowThinTopBorder().
                startRow().
                startRow().                                          // footer row
                addTextLeftAlignedColumn("").
                setColumnSize(0, "XXXX".length()).          // setting up column sizes at the end of the sheet
                setAutoSizeColumn(1).
                setAutoSizeColumn(2).
                setAutoSizeColumn(3).
                setAutoSizeColumn(4).
                setAutoSizeColumn(5).
                setAutoSizeColumn(6).
                setAutoSizeColumn(7);

        byte[] report = xlsxBuilder.build();

        try (FileOutputStream fos = new FileOutputStream("report.xlsx")) {
            fos.write(report);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }
}
