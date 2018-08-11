package com.chnu.aspirantura;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

/**
 * Created by fibs on 30.01.2018.
 */

public class ControllerMenu {

    public static Pane activePane;
    public static Pane paneAspirant = null;
    public static Pane paneKerivniki = null;
    public static Pane paneSpeciality = null;
    public static Pane paneDiscipline = null;
    public static Pane paneFaculty = null;
    public static Pane paneNakazi = null;
    public static Pane paneKafedra = null;
    public static Pane paneVykladachi = null;
    boolean loaded = false;
    @FXML
    private RadioMenuItem itemBirthday;
    @FXML
    private RadioMenuItem itemName;
    //private RadioMenuItem itemContacts;
    @FXML
    private RadioMenuItem itemStatus;
    @FXML
    private RadioMenuItem itemKerivnikView;
    @FXML
    private RadioMenuItem itemSpecialityView;

    @FXML
    void switchToAspirant(ActionEvent event) {
        paneAspirant = switchActivity(paneAspirant, "paneAspirant", "/fxml/aspirant/view_aspirant.fxml", "Аспріанти");
    }

    @FXML
    void switchToVykladachi(ActionEvent event) {
        paneVykladachi = switchActivity(paneVykladachi,"paneVykladachi", "/fxml/vykladach/view_vykladachi.fxml","Викладачі");
    }

    @FXML
    void switchToDiscipline(ActionEvent event) {
        paneDiscipline = switchActivity(paneDiscipline,"paneDiscipline", "/fxml/discipline/view_discipline.fxml","Дисципліни");
    }

    @FXML
    void switchToKafedri(ActionEvent event) {
        paneKafedra = switchActivity(paneKafedra,"paneKafedra", "/fxml/kafedra/view_kafedra.fxml","Кафедра");
    }

    @FXML
    void switchToSpeciality(ActionEvent event) {
        paneSpeciality = switchActivity(paneSpeciality,"paneSpeciality", "/fxml/speciality/view_speciality.fxml","Спеціальності");
    }
    @FXML
    void switchToFaculty(ActionEvent event) {
        paneFaculty = switchActivity(paneFaculty,"paneFaculty", "/fxml/faculty/view_faculty.fxml","Факультети");
    }


    @FXML
    void switchToNakazi(ActionEvent event) {
        paneNakazi = switchActivity(paneNakazi,"paneNakazi", "/fxml/nakaz/view_nakazi.fxml","Накази");
    }

    public Pane switchActivity(Pane pane, String paneId, String resource,String title){
        try {

            URL paneUrl = getClass().getResource(resource);
            if (pane == null) pane = FXMLLoader.load(paneUrl);
            System.out.println(pane);
            pane.setId(paneId);
            activePane = pane;
            BorderPane border = Main.getRoot();
            border.setCenter(pane);
            Scene scene = pane.getScene();
            scene.getStylesheets().add(0,"/css/style.css");
            Platform.runLater( () -> border.requestFocus());
            Main.setMainStageTitle("Відділ аспірантури | "+title);
            manageMenuViewItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pane;
    }


    @FXML
    void openReports(ActionEvent event) throws IOException {
           MenuItem menuItem = (MenuItem) event.getSource();
        if (menuItem.getId().equals("allTogether")){
            try {
                LoadWindow.loader.openWindow("/fxml/reports/report_aspirants.fxml","Відділ аспірантури | Звіти",400,200,null,2,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void changeMenuViewListener(ActionEvent event) throws IOException {
        RadioMenuItem radioMenuItem = (RadioMenuItem) event.getSource();
        System.out.println(radioMenuItem.getId());
        System.out.println("Selected:" + radioMenuItem.isSelected());
        boolean isSelected = !radioMenuItem.isSelected();




        switch (activePane.getId()){
            case "paneAspirant":
                System.out.println("Aspirant");

                if (radioMenuItem.getId().equals("itemName")) {
                    if(isSelected){
                        Settings.setSettings("menu_item_name_aspirantura", "unchecked");

                    }
                    else{
                        Settings.setSettings("menu_item_name_aspirantura", "checked");

                    }
                }

                if (radioMenuItem.getId().equals("itemStatus")) {
                    if (isSelected) Settings.setSettings("menu_item_status_aspirantura", "unchecked");
                    else Settings.setSettings("menu_item_status_aspirantura", "checked");
                }

                if (radioMenuItem.getId().equals("itemBirthday")) {
                    if (isSelected) Settings.setSettings("menu_item_birthday_aspirantura", "unchecked");
                    else Settings.setSettings("menu_item_birthday_aspirantura", "checked");
                }


                if (radioMenuItem.getId().equals("itemContacts")) {
                    if (isSelected) Settings.setSettings("menu_item_contacts_aspirantura", "unchecked");
                    else Settings.setSettings("menu_item_contacts_aspirantura", "checked");
                }


                if (radioMenuItem.getId().equals("itemSpecialityView")) {
                    if (isSelected) Settings.setSettings("menu_item_speciality_aspirantura", "unchecked");
                    else Settings.setSettings("menu_item_speciality_aspirantura", "checked");
                }

                if (radioMenuItem.getId().equals("itemKerivnikView")) {
                    if (isSelected) Settings.setSettings("menu_item_directir_aspirantura", "unchecked");
                    else Settings.setSettings("menu_item_directir_aspirantura", "checked");
                }


                break;

            case "paneKerivniki":
                System.out.println("Kerivniki");

                if (radioMenuItem.getId().equals("itemName")) {
                    if (isSelected) Settings.setSettings("menu_item_name_kerivniki", "unchecked");
                    else Settings.setSettings("menu_item_name_kerivniki", "checked");
                }

                if (radioMenuItem.getId().equals("itemStatus")) {
                    if (isSelected) Settings.setSettings("menu_item_status_kerivniki", "unchecked");
                    else Settings.setSettings("menu_item_status_kerivniki", "checked");
                }

                if (radioMenuItem.getId().equals("itemSpecialityView")) {
                    if (isSelected) Settings.setSettings("menu_item_speciality_kerivniki", "unchecked");
                    else Settings.setSettings("menu_item_speciality_kerivniki", "checked");
                }

                break;
            default:
                break;
        }
        manageMenuViewItems();
        Settings.save();
        System.out.println("_______________________________");
    }


    public void initialize() throws IOException {
        Settings.load();
        manageMenuViewItems();
        loaded=true;
    }


    public void manageMenuViewItems() throws IOException {
        switch (activePane.getId()) {
            case "paneAspirant":

                itemName.setVisible(true);
                itemBirthday.setVisible(true);
               // itemContacts.setVisible(true);
                itemKerivnikView.setVisible(true);
                itemStatus.setVisible(true);

                Main.controllerAspirant.removeTableColumns();



                if (Settings.getSettings("menu_item_name_aspirantura").equals("checked")){
                    itemName.setSelected(true);
                    Main.controllerAspirant.table.getColumns().add(Main.controllerAspirant.tableAspirantName);
                } else{
                    itemName.setSelected(false);
                    Main.controllerAspirant.table.getColumns().remove(Main.controllerAspirant.tableAspirantName);
                }


                if (Settings.getSettings("menu_item_birthday_aspirantura").equals("checked")){
                    itemBirthday.setSelected(true);
                    Main.controllerAspirant.table.getColumns().add(Main.controllerAspirant.tableAspirantDate);
                }
                else{
                    Main.controllerAspirant.table.getColumns().remove(Main.controllerAspirant.tableAspirantDate);
                    itemBirthday.setSelected(false);
                }



                if (Settings.getSettings("menu_item_directir_aspirantura").equals("checked")){
                    itemKerivnikView.setSelected(true);
                    Main.controllerAspirant.table.getColumns().add(Main.controllerAspirant.tableAspirantKerivnik);
                }
                else{
                    Main.controllerAspirant.table.getColumns().remove(Main.controllerAspirant.tableAspirantKerivnik);
                    itemKerivnikView.setSelected(false);
                }

//                if (Settings.getSettings("menu_item_contacts_aspirantura").equals("checked")) itemContacts.setSelected(true);
//                else itemContacts.setSelected(false);

                if (Settings.getSettings("menu_item_speciality_aspirantura").equals("checked")){
                    itemSpecialityView.setSelected(true);
                    Main.controllerAspirant.table.getColumns().add(Main.controllerAspirant.tableAspirantSpeciality);
                }
                else{
                    itemSpecialityView.setSelected(false);
                    Main.controllerAspirant.table.getColumns().remove(Main.controllerAspirant.tableAspirantSpeciality);
                }

                if (Settings.getSettings("menu_item_status_aspirantura").equals("checked")){
                    itemStatus.setSelected(true);
                    Main.controllerAspirant.table.getColumns().add(Main.controllerAspirant.tableAspirantStatus);
                }
                else{
                    Main.controllerAspirant.table.getColumns().remove(Main.controllerAspirant.tableAspirantStatus);
                    itemStatus.setSelected(false);
                }
                Main.controllerAspirant.table.getColumns().add(Main.controllerAspirant.tableAspirantYear);
                Main.controllerAspirant.table.getColumns().add(Main.controllerAspirant.tableAspirantForm);
                Main.controllerAspirant.table.getColumns().add(Main.controllerAspirant.tableAspirantNote);

                break;
            case "paneKerivniki":
                itemBirthday.setVisible(false);
//                itemContacts.setVisible(false);
                itemKerivnikView.setVisible(false);

                if (Settings.getSettings("menu_item_name_kerivniki").equals("checked")) itemName.setSelected(true);
                else itemName.setSelected(false);

                if (Settings.getSettings("menu_item_status_kerivniki").equals("checked")) itemStatus.setSelected(true);
                else itemStatus.setSelected(false);

                if (Settings.getSettings("menu_item_speciality_kerivniki").equals("checked")) itemSpecialityView.setSelected(true);
                else itemSpecialityView.setSelected(false);

                break;
            case "paneSpeciality":

                break;
            case "paneDisciplines":

                break;
            case "paneFaculty":

                break;
        }
    }


    public static void setActivePane(Pane activePane) {
        ControllerMenu.activePane = activePane;
    }

    public static void setPaneAspirant(Pane aspirantPane) {
        paneAspirant = aspirantPane;
    }

}