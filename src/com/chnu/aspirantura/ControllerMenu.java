package com.chnu.aspirantura;

import com.chnu.aspirantura.aspirant.ControllerAspirant;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

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

    @FXML private RadioMenuItem itemBirthday;
    @FXML private RadioMenuItem itemName;
    @FXML private RadioMenuItem itemStatus;
    @FXML private RadioMenuItem itemKerivnikView;
    @FXML private RadioMenuItem itemSpecialityView;
    @FXML private RadioMenuItem itemNote;
    @FXML private RadioMenuItem itemForm;
    @FXML private RadioMenuItem itemYear;

    @FXML private Menu viewItems;

    private ArrayList<RadioMenuItem> radioMenuItemsAspirantura = new ArrayList<>();


    @FXML
    void switchLayout(ActionEvent event) {

        RadioMenuItem node = (RadioMenuItem) event.getSource();
        String id = node.getId();

        viewItems.setVisible(false);

        if (id.equals("itemKafedri")) paneKafedra = switchActivity(paneKafedra, "paneKafedra", "/fxml/kafedra/view_kafedra.fxml", "Кафедра");
        else if (id.equals("itemDiscipline")) paneDiscipline = switchActivity(paneDiscipline, "paneDiscipline", "/fxml/discipline/view_discipline.fxml", "Дисципліни");
        else if (id.equals("itemVykladachi")) paneVykladachi = switchActivity(paneVykladachi, "paneVykladachi", "/fxml/vykladach/view_vykladachi.fxml", "Викладачі");
        else if (id.equals("itemSpeciality")) paneSpeciality = switchActivity(paneSpeciality, "paneSpeciality", "/fxml/speciality/view_speciality.fxml", "Спеціальності");
        else if (id.equals("itemFaculty")) paneFaculty = switchActivity(paneFaculty, "paneFaculty", "/fxml/faculty/view_faculty.fxml", "Факультети");
        else if (id.equals("itemNakazi")) paneNakazi = switchActivity(paneNakazi, "paneNakazi", "/fxml/nakaz/view_nakazi.fxml", "Накази");
        else if (id.equals("itemAspirants")){
            paneAspirant = switchActivity(paneAspirant, "paneAspirant", "/fxml/aspirant/view_aspirant.fxml", "Аспріанти");
            viewItems.setVisible(true);
        }
    }

    public Pane switchActivity(Pane pane, String paneId, String resource, String title) {
        try {
            URL paneUrl = getClass().getResource(resource);
            if (pane == null) pane = FXMLLoader.load(paneUrl);
            pane.setId(paneId);
            activePane = pane;
            BorderPane border = Main.getRoot();
            border.setCenter(pane);
            Scene scene = pane.getScene();
            scene.getStylesheets().add(0, "/css/style.css");
            Platform.runLater(() -> border.requestFocus());
            Main.setMainStageTitle("Відділ аспірантури | " + title);
            manageMenuViewItems();
        } catch (IOException e) {
            Utils.logger.error("Error switching activity: "+e.getMessage());
        }
        return pane;
    }


    @FXML
    void openReports(ActionEvent event) throws IOException {
        MenuItem menuItem = (MenuItem) event.getSource();
        if (menuItem.getId().equals("allTogether")) {
            try {
                LoadWindow.loader.openWindow("/fxml/reports/report_aspirants.fxml", "Відділ аспірантури | Звіти", 400, 200, null, 2, 0);
            } catch (IOException e) {
                Utils.logger.error("Error opening reports: "+e.getMessage());
            }
        }
    }


    @FXML
    void changeMenuViewListener(ActionEvent event) throws IOException {
        RadioMenuItem radioMenuItem = (RadioMenuItem) event.getSource();

        boolean isSelected = radioMenuItem.isSelected();

        if (activePane.getId().equals("paneAspirant")) {
                if (radioMenuItem.getId().equals("itemName")) Settings.setSettings("item.name.aspirantura.checked", String.valueOf(isSelected));
                else if (radioMenuItem.getId().equals("itemBirthday"))  Settings.setSettings("item.birthday.aspirantura.checked", String.valueOf(isSelected));
                else if (radioMenuItem.getId().equals("itemSpecialityView"))  Settings.setSettings("item.speciality.aspirantura.checked", String.valueOf(isSelected));
                else if (radioMenuItem.getId().equals("itemKerivnikView"))  Settings.setSettings("item.kerivnik.aspirantura.checked", String.valueOf(isSelected));
                else if (radioMenuItem.getId().equals("itemNote"))  Settings.setSettings("item.note.aspirantura.checked", String.valueOf(isSelected));
                else if (radioMenuItem.getId().equals("itemYear"))  Settings.setSettings("item.year.aspirantura.checked", String.valueOf(isSelected));
                else if (radioMenuItem.getId().equals("itemStatus")) Settings.setSettings("item.status.aspirantura.checked", String.valueOf(isSelected));
                else if (radioMenuItem.getId().equals("itemForm"))  Settings.setSettings("item.form.aspirantura.checked", String.valueOf(isSelected));
        }
        manageMenuViewItems();
        Settings.savePropertiesToFile();
    }


    public void initialize() throws IOException {
        Settings.loadProperties();

        radioMenuItemsAspirantura.add(itemName);
        radioMenuItemsAspirantura.add(itemBirthday);
        radioMenuItemsAspirantura.add(itemKerivnikView);
        radioMenuItemsAspirantura.add(itemSpecialityView);
        radioMenuItemsAspirantura.add(itemForm);
        radioMenuItemsAspirantura.add(itemStatus);
        radioMenuItemsAspirantura.add(itemYear);
        radioMenuItemsAspirantura.add(itemNote);

        manageMenuViewItems();

    }

    private  void manageItemView(TableView tableView, String parametr, RadioMenuItem radio, TableColumn column){

        boolean value = Settings.getProperty(parametr).equals("true");
        radio.setSelected(value);

        if (value) tableView.getColumns().add(column);
        else tableView.getColumns().remove(column);
    }



    public void manageMenuViewItems() throws IOException {
        TableView tableView;

        radioMenuItemsAspirantura.forEach((x)->x.setVisible(false));
        if(activePane.getId().equals("paneAspirant")) {
            radioMenuItemsAspirantura.forEach((x)->x.setVisible(true));
            ControllerAspirant controllerAspirant = Main.controllerAspirant;
            controllerAspirant.removeTableColumns();
            tableView = controllerAspirant.table;

            manageItemView(tableView, "item.name.aspirantura.checked", itemName, controllerAspirant.tableAspirantName);
            manageItemView(tableView, "item.birthday.aspirantura.checked", itemBirthday, controllerAspirant.tableAspirantDate);
            manageItemView(tableView, "item.kerivnik.aspirantura.checked", itemKerivnikView, controllerAspirant.tableAspirantKerivnik);
            manageItemView(tableView, "item.speciality.aspirantura.checked", itemSpecialityView, controllerAspirant.tableAspirantSpeciality);
            manageItemView(tableView, "item.form.aspirantura.checked", itemForm, controllerAspirant.tableAspirantForm);
            manageItemView(tableView, "item.status.aspirantura.checked", itemStatus, controllerAspirant.tableAspirantStatus);
            manageItemView(tableView, "item.year.aspirantura.checked", itemYear, controllerAspirant.tableAspirantYear);
            manageItemView(tableView, "item.note.aspirantura.checked", itemNote, controllerAspirant.tableAspirantNote);

        }
    }


    public static void setActivePane(Pane activePane_) {
        activePane = activePane_;
    }

    public static void setPaneAspirant(Pane aspirantPane) {
        paneAspirant = aspirantPane;
    }

}