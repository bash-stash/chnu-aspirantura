package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.LoadWindow;
import com.chnu.aspirantura.Main;
import com.chnu.aspirantura.SqlCommander;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by fibs on 12.02.2018.
 */
public class ControllerShowScience{
    public  static int aspirantId;
    public  static String aspirantName;
    public  static  boolean addedNew =true;
    @FXML Label title;
    @FXML AnchorPane pane;


    public void addNewWork(){
        try {
            LoadWindow.loader.openWindow("/fxml/aspirant/form_add_edit_science_work.fxml","Наукова активність | " + aspirantName,540,205,null,2,0);
            if (addedNew){

                pane.getChildren().clear();
                initialize();
                addedNew=false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }



    public void initialize(){
        title.setText("Аспірант №"+aspirantId+"  "+aspirantName);
        DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");

        ControllerAddEditScienceWork.aspirantId=aspirantId;

        ArrayList<ObjectScienceWork> list = SqlCommander.getAspirantScienceWorks(aspirantId);
        ObjectScienceWork obj;

        for (int i = 0; i < ((list==null)?0:list.size()); i++) {
            obj= list.get(i);
            Pane p = new Pane();
            p.setId(String.valueOf(obj.id));
            p.setMinHeight(105);
            p.setMinWidth(600);
            p.setLayoutY(105*i);
            p.setLayoutX(20);
            p.setCursor(Cursor.HAND);
            p.setId(String.valueOf(obj.id));

            Label nameLabel = new Label();
            nameLabel.setStyle("-fx-font-size: 14px;");
            nameLabel.setLayoutX(10);
            nameLabel.setLayoutY(5);

            Label nameValue = new Label();
            nameValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            nameValue.setLayoutX(110);
            nameValue.setLayoutY(5);



            Label whereLabel = new Label();
            whereLabel.setStyle("-fx-font-size: 14px;");
            whereLabel.setLayoutX(10);
            whereLabel.setLayoutY(30);

            Label whereValue = new Label();
            whereValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            whereValue.setLayoutX(110);
            whereValue.setLayoutY(30);


            Label dateLabel = new Label();
            dateLabel.setStyle("-fx-font-size: 14px;");
            dateLabel.setLayoutX(10);
            dateLabel.setLayoutY(55);

            Label dateValue = new Label();
            dateValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            dateValue.setLayoutX(110);
            dateValue.setLayoutY(55);



            Label linkLabel = new Label();
            linkLabel.setStyle("-fx-font-size: 14px;");
            linkLabel.setLayoutX(10);
            linkLabel.setLayoutY(80);

            Hyperlink linkValue = new Hyperlink(obj.link);
            linkValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            linkValue.setLayoutX(110);
            linkValue.setLayoutY(80);
            linkValue.setBorder(Border.EMPTY);
            linkValue.setPadding(new Insets(4, 0, 4, 0));




            Separator separator = new Separator();
            separator.setLayoutY(105);
            separator.setMinWidth(570);


            nameLabel.setText("Назва роботи: ");
            nameValue.setText(obj.name);

            whereLabel.setText("Де видано: ");
            whereValue.setText(obj.where);

            dateLabel.setText("Дата: ");
            dateValue.setText(targetFormat.format(obj.date));

            linkLabel.setText("Посилання: ");
            linkValue.setText(obj.link);


            linkValue.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    HostServices services = Main.mInstance.getHostServices();
                    services.showDocument(linkValue.getText());
                }
            });
            p.getChildren().addAll(nameLabel,nameValue,whereLabel,whereValue,dateLabel,dateValue,linkLabel,linkValue,separator);

            int finalI = i;

            p.setOnMouseClicked((id)->{
                ControllerAddEditScienceWork.work=list.get(finalI);

                try {
                    LoadWindow.loader.openWindow("/fxml/aspirant/form_add_edit_science_work.fxml","Наукова активність | " + aspirantName,540,205,null,2,0);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(Integer.parseInt(p.getId()));
                ObjectScienceWork obj2  = SqlCommander.getAspirantScienceWork(Integer.parseInt(p.getId()));
                linkValue.setText(obj2.link);
                dateValue.setText(targetFormat.format(obj2.date));
                whereValue.setText(obj2.where);
                nameValue.setText(obj2.name);

                list.set(finalI,obj2);

            });

            pane.getChildren().add(p);
        }
    }


}