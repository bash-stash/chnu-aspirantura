package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;
import com.chnu.aspirantura.nakaz.ObjectNakaz;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class ControllerShowInfoAspirant {
    public static ObjectAspirant aspirant;
    @FXML Label title;
    @FXML Label showHideLabel;
    @FXML Pane passportPane;



    @FXML
    Label pib;
    @FXML
    Label telephone;
    @FXML
    Label email;
    @FXML
    Label address;
    @FXML
    Label seria;
    @FXML
    Label number;
    @FXML
    Label publishedBy;
    @FXML
    Label identificator;
    @FXML
    Label publishedDate;
    @FXML
    Label birthday;
    @FXML
    ImageView photo;
    @FXML
    Label speciality;
    @FXML
    Label male;
    @FXML
    Label form;
    @FXML
    Label nakazDate;
    @FXML
    Label kerivnik;
    @FXML
    Label nakazNumber;


    public void initialize() throws ParseException {

        aspirant.setContacts(SqlCommander.getContactsByAspirant(aspirant.getId()));
        aspirant.setPassport(SqlCommander.getPassportByAspirant(aspirant.getId()));
        InputStream s = SqlCommander.getAspirantPhoto(aspirant.getId());
        if (s!=null) {
            aspirant.setImgStream(s);
            photo.setImage(new Image(s));
        }
        pib.setText(aspirant.getName());

        birthday.setText(aspirant.getDate());
        telephone.setText(aspirant.getContacts().getTelephone());
        email.setText(aspirant.getContacts().getEmail());
        address.setText(aspirant.getContacts().getAddress());

        seria.setText(aspirant.getPassport().getSeria());
        number.setText(aspirant.getPassport().getNumber());
        publishedBy.setText(aspirant.getPassport().getPublishedBy());
        identificator.setText(aspirant.getPassport().getIdentificator());

        form.setText(aspirant.getForm());
        male.setText(aspirant.getMale());


        java.util.Date date;
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");

        date = originalFormat.parse((aspirant.getPassport().getPublishedDate().toString()));
        date = targetFormat.parse(targetFormat.format(date));
        publishedDate.setText(targetFormat.format(date).toString());

        speciality.setText(aspirant.getSpeciality());
        kerivnik.setText(aspirant.getKerivnik());

        ObjectNakaz o = SqlCommander.getNakazZarahByAspirantId(aspirant.getId());

        nakazNumber.setText(((o==(null))?"(відсутній)":o.getNumber()));
        nakazDate.setText((o==(null))?"(відсутній)":o.getDate());
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


    public void togglePassport() {
        if (showHideLabel.getText().equals("[Показати]")) {
            passportPane.setVisible(true);
            showHideLabel.setText(("[Приховати]"));
        } else {
            passportPane.setVisible(false);
            showHideLabel.setText(("[Показати]"));
        }
    }
}

