package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.discipline.ObjectStudyForm;
import com.chnu.aspirantura.nakaz.ObjectNakaz;
import com.chnu.aspirantura.speciality.ObjectSpeciality;
import com.chnu.aspirantura.vykladach.ObjectVykladach;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;


/*
 * Created by fibs on 04.02.2018.
 */

public class ControllerAddAspirant {
    public static Scene scene;
    @FXML
    TextField pib;
    @FXML
    TextField telephone;
    @FXML
    TextField email;
    @FXML
    TextField address;
    @FXML
    TextField seria;
    @FXML
    TextField number;
    @FXML
    TextField publishedBy;
    @FXML
    TextField identificator;
    @FXML
    DatePicker datePublished;
    @FXML
    DatePicker birthday;
    @FXML
    ImageView photo;
    @FXML
    ComboBox<ObjectSpeciality> comboBoxSpeciality;
    @FXML
    ComboBox<ObjectVykladach> comboBoxKerivnik;
    @FXML
    ComboBox<ObjectNakaz> comboBoxNakaz;

    File file;
    @FXML  ComboBox<ObjectStudyForm> comboBoxForma;
    @FXML  ComboBox<String> comboBoxMale;


    static ObservableList<ObjectSpeciality> dataSpecialities = FXCollections.observableArrayList();
    static ObservableList<ObjectVykladach> dataKerivnik= FXCollections.observableArrayList();
    static ObservableList<ObjectNakaz> dataNakazi = FXCollections.observableArrayList();
    static ObservableList<ObjectStudyForm> forms = FXCollections.observableArrayList();

    public void initialize() {
        dataSpecialities = SqlCommander.getAllSpeciality();
        comboBoxSpeciality.setItems(dataSpecialities);
        forms = SqlCommander.getAllForms();
        dataNakazi = SqlCommander.getNakazByType(4);
        comboBoxNakaz.setItems(dataNakazi);
        comboBoxForma.setItems(forms);

        comboBoxSpeciality.valueProperty().addListener(new ChangeListener<ObjectSpeciality>() {
            @Override
            public void changed(ObservableValue<? extends ObjectSpeciality> observable, ObjectSpeciality oldValue, ObjectSpeciality newValue) {
                try {
                    dataKerivnik = SqlCommander.getAllVykladachiBySpeciality(newValue.getId());
                    comboBoxKerivnik.setItems(dataKerivnik);
                } catch (Exception e) {

                }
            }
        });
    }


    public void cancel() {
        Stage stage = (Stage) birthday.getScene().getWindow();
        stage.close();
    }

    public void selectPhoto(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter( "*.images","*.jpg","*.png","*.jpeg","*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Open Resource File");
        file = fileChooser.showOpenDialog(null);

        if (file != null) {

            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                photo.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void apply() throws FileNotFoundException {
        String pibData = pib.getText();
        String telephoneData = telephone.getText();
        String emailData = email.getText();
        String addressData = address.getText();
        String seriaData = seria.getText();
        String numberData = number.getText();
        String publishedByData = publishedBy.getText();
        String identificatorData = identificator.getText();
        LocalDate datePublishedData = datePublished.getValue();
        LocalDate birthdayData = birthday.getValue();


        boolean isMale = (comboBoxMale.getValue().equals("Чоловіча"));
        short form=(short)comboBoxForma.getValue().getId();


        if (form==-1) return;
        FileInputStream fis=null;
        if (file!=null)  fis = new FileInputStream(file);

        int idSpecialilty = comboBoxSpeciality.getValue().getId();
        int idKerivnik = comboBoxKerivnik.getValue().getId();
        int idNakaz = comboBoxNakaz.getValue().getId();
        int year = Integer.parseInt(comboBoxNakaz.getValue().getDate().substring(comboBoxNakaz.getValue().getDate().length()-4,comboBoxNakaz.getValue().getDate().length()));



        int idPassport = SqlCommander.addNewPassportData(pibData,seriaData,numberData,publishedByData,datePublishedData,identificatorData);
        int idContacts = SqlCommander.addNewContactsData(telephoneData,emailData,addressData);
        int nextAspirant = SqlCommander.getLastApirantId();
        int idStatus = SqlCommander.addStatusData(LocalDate.now(),idNakaz,nextAspirant+1);
        SqlCommander.addNewAspirant(pibData,birthdayData,idKerivnik,idSpecialilty,idContacts,idPassport,idStatus,year,(isMale?1:0),form,fis,(file==null)?0:file.length());

        ControllerAspirant.addedNew=true;

        Stage stage = (Stage) birthday.getScene().getWindow();
        stage.close();
    }
}
