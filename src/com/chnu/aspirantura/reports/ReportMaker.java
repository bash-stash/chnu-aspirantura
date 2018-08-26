package com.chnu.aspirantura.reports;

import com.chnu.aspirantura.ControllerLogin;
import com.chnu.aspirantura.Main;
import com.chnu.aspirantura.aspirant.ObjectAspirant;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportMaker {

    @FXML
    private Label title;


    public void initialize() {

    }

    public static void main(String[] args) throws IOException, ParseException {
        ReportMaker reportMaker = new ReportMaker();
        reportMaker.makeReport2();

    }

    public void apply() throws ParseException, IOException {

        makeReport2();

        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }

    public XlsxBuilder mergeCells(XlsxBuilder xlsxBuilder) {
        xlsxBuilder.
                startRow().
                addMergedColumn(0, 2, 0, 0, true).
                addMergedColumn(0, 2, 1, 1, true).
                addMergedColumn(0, 2, 2, 2, true).
                addMergedColumn(0, 0, 3, 4, true).
                addMergedColumn(1, 2, 3, 3, true).
                addMergedColumn(1, 2, 4, 4, true).
                addMergedColumn(0, 0, 5, 8, true).
                addMergedColumn(1, 1, 5, 6, true).
                addMergedColumn(1, 1, 7, 8, true).
                addMergedColumn(2, 2, 5, 5, true).
                addMergedColumn(2, 2, 6, 6, true).
                addMergedColumn(2, 2, 7, 7, true).
                addMergedColumn(2, 2, 8, 8, true).
                addMergedColumn(0, 0, 9, 10, true).
                addMergedColumn(1, 2, 9, 9, true).
                addMergedColumn(1, 2, 10, 10, true).
                addMergedColumn(0, 2, 11, 11, true).
                addMergedColumn(0, 2, 12, 12, true).
                addMergedColumn(0, 2, 13, 13, true).
                startRow().
                startRow().
                startRow();

        return xlsxBuilder;
    }


    public void makeReport2() {

        XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.startSheet("Форма № 1-нк");
        mergeCells(xlsxBuilder);
        xlsxBuilder.setBordersToMergedCells();
        setTitles(xlsxBuilder);
        manageRowsHeight(xlsxBuilder);

        writeReportToFile(xlsxBuilder);
    }

    private void manageRowsHeight(XlsxBuilder xlsxBuilder) {
        Sheet sheet = xlsxBuilder.getSheet();

        sheet.getRow(0).setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
        sheet.getRow(1).setHeightInPoints((sheet.getDefaultRowHeightInPoints()));
        sheet.getRow(2).setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));

        xlsxBuilder.setSheet(sheet);
    }


    private void setTitles(XlsxBuilder xlsxBuilder) {
        xlsxBuilder.addTitleToColumn(0, "Найменування\nспеціальностей");
        xlsxBuilder.addTitleToColumn(1, "Код\nспеціаль-\nності");
        xlsxBuilder.addTitleToColumn(2, "№\nрядка");
        xlsxBuilder.addTitleToColumn(3, "Кількість осіб, зарахованих до \nаспірантури у звітному році, за\n формами навчання ");
        xlsxBuilder.addTitleToColumn(4, "денною");
        xlsxBuilder.addTitleToColumn(5, "вечірньою та\n заочною ");
        xlsxBuilder.addTitleToColumn(6, "Кількість осіб, які закінчили аспірантуру\n у звітному році, за формами навчання");
        xlsxBuilder.addTitleToColumn(7, "денною");
        xlsxBuilder.addTitleToColumn(8, "вечірньою та заочною ");
        xlsxBuilder.addTitleToColumn(9, "усього");
        xlsxBuilder.addTitleToColumn(10, "у тому числі з захистом дисертації");
        xlsxBuilder.addTitleToColumn(11, "усього");
        xlsxBuilder.addTitleToColumn(12, "у тому числі з захистом дисертації");
        xlsxBuilder.addTitleToColumn(13, "Кількість аспірантів на кінець\n звітного року за формами\n навчання ");
        xlsxBuilder.addTitleToColumn(14, "денною");
        xlsxBuilder.addTitleToColumn(15, "вечірньою та заочною ");
        xlsxBuilder.addTitleToColumn(16, "Кількість\nаспірантів─\nжінок (з суми граф\n7 та 8)");
        xlsxBuilder.addTitleToColumn(17, "Кількість\nжінок, які\nзакінчили\nаспірантуру\n(з суми граф\n3 та 5)");
        xlsxBuilder.addTitleToColumn(18, "Кількість\n жінок,\nзарахованих до\nаспірантури\n(з суми граф\n1 та 2)");

        xlsxBuilder.
                addTextCenterAlignedColumn("А").
                addTextCenterAlignedColumn("Б").
                addTextCenterAlignedColumn("В").
                addTextCenterAlignedColumn("1").
                addTextCenterAlignedColumn("2").
                addTextCenterAlignedColumn("3").
                addTextCenterAlignedColumn("4").
                addTextCenterAlignedColumn("5").
                addTextCenterAlignedColumn("6").
                addTextCenterAlignedColumn("7").
                addTextCenterAlignedColumn("8").
                addTextCenterAlignedColumn("9").
                addTextCenterAlignedColumn("10").
                addTextCenterAlignedColumn("11");


        xlsxBuilder.setBordersToMergedCells();

    }


    private void writeReportToFile(XlsxBuilder xlsxBuilder) {

        byte[] report = xlsxBuilder.build();
        try (FileOutputStream fos = new FileOutputStream("report.xlsx")) {
            fos.write(report);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void makeReport() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss ");

        String filter = (String) Main.controllerAspirant.searchType.getValue();
        String filterValue = Main.controllerAspirant.textToFind.getText();


        XlsxBuilder xlsxBuilder = new XlsxBuilder();

        xlsxBuilder.startSheet("Звіт аспіранти").                            // start with sheet
                startRow().                                          // then go row by row
                setRowTitleHeight().                                 // set row style, add borders and so on
                addTitleTextColumn("Звіт аспіранти").                    // add columns
                startRow().                                          // another row
                addBoldTextLeftAlignedColumn((filterValue.equals("") ? "" : "Фільтр - " + filter + " = " + filterValue)).
                startRow().                                          // another row
                setRowTitleHeight().                                 // row styling
                setRowThinBottomBorder().
                addBoldTextLeftAlignedColumn("Створив: " + ControllerLogin.login + "     Дата:  " + dateFormat.format(new Date())).
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

        for (ObjectAspirant aspirant : Main.controllerAspirant.table.getItems()) {
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

    }
}
