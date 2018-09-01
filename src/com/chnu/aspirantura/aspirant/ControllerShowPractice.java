package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.LoadWindow;
import com.chnu.aspirantura.SqlCommander;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;


public class ControllerShowPractice {
    public  static int aspirantId;
    public  static String aspirantName;
    public  static  boolean addedNew =true;
    @FXML Label title;
    @FXML AnchorPane pane;


    public void addNewPractice(){
        try {
            LoadWindow.loader.openWindow("/fxml/aspirant/form_add_edit_practice_work.fxml","Практика | " + aspirantName,700,380,null,2,0);
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


        ControllerAddEditPractice.aspirantId=aspirantId;

        ArrayList<ObjectPractice> list = SqlCommander.getAspirantPractice(aspirantId);
        ObjectPractice obj;

        for (int i = 0; i < ((list==null)?0:list.size()); i++) {
            obj= list.get(i);
            Pane p = new Pane();
            p.setId(String.valueOf(obj.id));
            p.setMinHeight(40);
            p.setMinWidth(600);
            p.setLayoutY(40*i);
            p.setLayoutX(20);
            p.setCursor(Cursor.HAND);
            p.setId(String.valueOf(obj.id));

            Label nameLabel = new Label();
            nameLabel.setStyle("-fx-font-size: 14px;");
            nameLabel.setLayoutX(10);
            nameLabel.setLayoutY(13);

            Label nameValue = new Label();
            nameValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            nameValue.setLayoutX(120);
            nameValue.setLayoutY(13);

            Separator separator = new Separator();
            separator.setLayoutY(45);
            separator.setMinWidth(570);


            nameLabel.setText("Назва практики: ");
            nameValue.setText(obj.name);


            p.getChildren().addAll(nameLabel,nameValue,separator);

            int finalI = i;

            p.setOnMouseClicked((id)->{
                ControllerAddEditPractice.practice=list.get(finalI);

                try {
                    LoadWindow.loader.openWindow("/fxml/aspirant/form_add_edit_practice_work.fxml","Наукова активність | " + aspirantName,700,380,null,2,0);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(Integer.parseInt(p.getId()));
                ObjectPractice obj2  = SqlCommander.getAspirantPracticeById(Integer.parseInt(p.getId()));
                nameValue.setText(obj2.name);

                list.set(finalI,obj2);

            });

            pane.getChildren().add(p);
        }
    }


}