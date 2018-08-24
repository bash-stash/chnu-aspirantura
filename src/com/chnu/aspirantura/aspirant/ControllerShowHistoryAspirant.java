package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



public class ControllerShowHistoryAspirant {
    public static int aspirantId;
    public static String aspirantName;
    @FXML Label title;
    @FXML AnchorPane pane;

    public void initialize(){
        title.setText("Аспірант №"+aspirantId+"  "+aspirantName);
        DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");

        ArrayList<ObjectHistoryAspirant> list = SqlCommander.getAspirantHistory(aspirantId);
        ObjectHistoryAspirant obj;

        for (int i = 0; i < ((list==null)?0:list.size()); i++) {
            obj= list.get(i);
            Pane p = new Pane();
            p.setMinHeight(100);
            p.setMinWidth(600);
            p.setLayoutY(50*i);
            p.setLayoutX(20);

            Label madeByLabel = new Label();
            madeByLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: rgba(61,61,61,0.99);");
            madeByLabel.setLayoutX(10);
            madeByLabel.setLayoutY(25);

            Label typeLabel = new Label();
            typeLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold");
            typeLabel.setLayoutX(100);
            typeLabel.setLayoutY(5);

            Label dateLabel = new Label();
            dateLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #255dff;");
            dateLabel.setLayoutX(10);
            dateLabel.setLayoutY(5);

            Separator separator = new Separator();
            separator.setLayoutY(50);
            separator.setMinWidth(570);

            madeByLabel.setText("Дата: "+obj.madeBy);
            dateLabel.setText(targetFormat.format(obj.date));
            typeLabel.setText("Наказ "+obj.nakazNumber+" від "+targetFormat.format(obj.nakazDate)+" | "+obj.type);

            p.getChildren().addAll(madeByLabel,dateLabel,typeLabel,separator);

            pane.getChildren().add(p);
        }
    }
}
