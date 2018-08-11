package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.nakaz.ObjectNakaz;
import com.chnu.aspirantura.speciality.ObjectSpeciality;
import com.chnu.aspirantura.vykladach.ObjectVykladach;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by fibs on 12.02.2018.
 */

public class ControllerEditInfoAspirant {
    public static ObjectAspirant aspirant;
    @FXML Label showHideLabel;
    @FXML Pane passportPane;

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
    @FXML  ComboBox<String> comboBoxForma;
    @FXML  ComboBox<String> comboBoxMale;


    static ObservableList<ObjectSpeciality> dataSpecialities = FXCollections.observableArrayList();
    static ObservableList<ObjectVykladach> dataKerivnik= FXCollections.observableArrayList();
    static ObservableList<ObjectNakaz> dataNakazi = FXCollections.observableArrayList();


    public void initialize() {
        dataSpecialities = SqlCommander.getAllSpeciality();
        comboBoxSpeciality.setItems(dataSpecialities);

        dataNakazi = SqlCommander.getNakazByType(4);
        comboBoxNakaz.setItems(dataNakazi);

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


        aspirant.setContacts(SqlCommander.getContactsByAspirant(aspirant.getId()));
        aspirant.setPassport(SqlCommander.getPassportByAspirant(aspirant.getId()));
        comboBoxForma.setValue(aspirant.getForm());
        comboBoxMale.setValue(aspirant.getMale());
        InputStream s = SqlCommander.getAspirantPhoto(aspirant.getId());
        if (s!=null){
            aspirant.setImgStream(s);
            photo.setImage(new Image(s));
        }



        pib.setText(aspirant.getName());

        birthday.setValue(setDate(aspirant.getDate()));
        telephone.setText(aspirant.getContacts().getTelephone());
        email.setText(aspirant.getContacts().getEmail());
        address.setText(aspirant.getContacts().getAddress());

        seria.setText(aspirant.getPassport().getSeria());
        number.setText(aspirant.getPassport().getNumber());
        publishedBy.setText(aspirant.getPassport().getPublishedBy());
        identificator.setText(aspirant.getPassport().getIdentificator());
        datePublished.setValue(aspirant.getPassport().getPublishedDate());


        dataSpecialities.forEach((tab) -> {
          if (tab.getName().equals(aspirant.getSpeciality()))  comboBoxSpeciality.setValue(tab);
        });

        dataKerivnik.forEach((tab) -> {
            if (tab.getName().equals(aspirant.getKerivnik()))  comboBoxKerivnik.setValue(tab);
        });


        comboBoxNakaz.setValue(SqlCommander.getNakazZarahByAspirantId(aspirant.getId()));

    }

    public LocalDate setDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
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
        int identificatorData = Integer.parseInt(identificator.getText());
        LocalDate datePublishedData = datePublished.getValue();
        LocalDate birthdayData = birthday.getValue();




        boolean isMale = (comboBoxMale.getValue().equals("Чоловіча"));
        short form=-1;
        switch (comboBoxForma.getValue()){
            case "Денна":
                form = 1;
                break;
            case "Заочна":
                form = 2;
                break;
            case "Вечірня":
                form = 3;
                break;
        }

        if (form==-1) return;
        FileInputStream fis=null;
       if (file!=null)  fis = new FileInputStream(file);



        int idSpecialilty = comboBoxSpeciality.getValue().getId();
        int idKerivnik = comboBoxKerivnik.getValue().getId();
        int idNakaz = comboBoxNakaz.getValue().getId();
        int year = Integer.parseInt(comboBoxNakaz.getValue().getDate().substring(comboBoxNakaz.getValue().getDate().length()-4,comboBoxNakaz.getValue().getDate().length()));


        SqlCommander.editPassportData(aspirant.getId(),aspirant.getName(),seriaData,numberData,publishedByData,datePublishedData,identificatorData);
        SqlCommander.editContacts(aspirant.getId(),telephoneData,emailData,addressData);
        SqlCommander.editAspirant(aspirant.getId(),pibData,birthdayData,idKerivnik,idSpecialilty,idNakaz,year,(isMale?1:0),form,fis,(file==null)?0:file.length());

        ControllerAspirant.addedNew=true;
        Stage stage = (Stage) birthday.getScene().getWindow();
        stage.close();
    }


    public void togglePassport() {
        if (showHideLabel.getText().equals("[Показати]")) {
            passportPane.setVisible(false);
            showHideLabel.setText(("[Приховати]"));
        } else {
            passportPane.setVisible(true);
            showHideLabel.setText(("[Показати]"));
        }
    }
}
