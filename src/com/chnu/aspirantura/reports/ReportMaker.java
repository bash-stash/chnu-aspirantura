package com.chnu.aspirantura.reports;

import com.chnu.aspirantura.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

public class ReportMaker {

    static private int year = 2018;

    @FXML

    private Label title;
    @FXML

    private Label resultLabel;

    @FXML
    private TextField yearField;



    public void initialize() {
        yearField.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    }

    public static void main(String[] args) throws IOException, ParseException {
        ReportMaker reportMaker = new ReportMaker();
        reportMaker.makeReport(year);
    }

    public void apply() {

        String s = "Сформовано!";

        try {
            year = Integer.parseInt(yearField.getText());
            makeReport(year);
        }catch (Exception e){
            s = "Виникла помилка";
            Utils.logger.error("Report formatting: "+e.getMessage());
        }

        resultLabel.setText(s);
        resultLabel.setVisible(true);

    }

    public void cancel() {
        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }

    private XlsxBuilder mergeCells(XlsxBuilder xlsxBuilder) {
        xlsxBuilder.
                startRow().
                addMergedColumn(0, 2, 0, 0).
                addMergedColumn(0, 2, 1, 1).
                addMergedColumn(0, 2, 2, 2).
                addMergedColumn(0, 0, 3, 4).
                addMergedColumn(1, 2, 3, 3).
                addMergedColumn(1, 2, 4, 4).
                addMergedColumn(0, 0, 5, 8).
                addMergedColumn(1, 1, 5, 6).
                addMergedColumn(1, 1, 7, 8).
                addMergedColumn(2, 2, 5, 5).
                addMergedColumn(2, 2, 6, 6).
                addMergedColumn(2, 2, 7, 7).
                addMergedColumn(2, 2, 8, 8).
                addMergedColumn(0, 0, 9, 10).
                addMergedColumn(1, 2, 9, 9).
                addMergedColumn(1, 2, 10, 10).
                addMergedColumn(0, 2, 11, 11).
                addMergedColumn(0, 2, 12, 12).
                addMergedColumn(0, 2, 13, 13).
                startRow().
                startRow().
                startRow();

        return xlsxBuilder;
    }


    private void makeReport(int year) {

        XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.startSheet("Форма № 1-нк");
        mergeCells(xlsxBuilder);
        xlsxBuilder.setBordersToMergedCells();
        setTitles(xlsxBuilder);
        manageRowsHeight(xlsxBuilder);

        addDataToReport(xlsxBuilder,year);

        writeReportToFile(xlsxBuilder);
    }


    private void addDataToReport(XlsxBuilder xlsxBuilder,int year){



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
}
