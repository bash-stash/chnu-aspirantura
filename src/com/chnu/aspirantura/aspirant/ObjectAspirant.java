package com.chnu.aspirantura.aspirant;

import com.chnu.aspirantura.SqlCommander;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class ObjectAspirant {
    private int id;
    private String name;

    private String form;
    private String male;
    private String speciality;
    private String kafedraKabinet;
    private Date date;
    private String kerivnik;
    private String statusLabel;
    private String note;
    private int statusId;
    private int year;
    private ObjectContacts contacts;
    private ObjectPassport passport;



    InputStream imgStream;

    public void setPassport(ObjectPassport passport) {
        this.passport = passport;
    }

    public ObjectPassport getPassport() {

        return passport;
    }


    public void setForm(String form) {
        this.form = form;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getForm() {
        return form;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getKafedraKabinet() {
        return kafedraKabinet;
    }

    public void setKafedraKabinet(String kafedraKabinet) {
        this.kafedraKabinet = kafedraKabinet;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setImgStream(InputStream imgStream) {
        this.imgStream = imgStream;
    }

    public String getMale() {
        return male;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
        switch (SqlCommander.getTypeByNakaz(statusId)){
            case "Оформлення перерви":
                statusLabel=Status.orgStatuses[0];
                break;
            case "Поновлення після перерви":
                statusLabel=Status.orgStatuses[1];
                break;
            case "Закінчення аспірантури":
                statusLabel=Status.orgStatuses[2];
                break;
            case "Зарахування до аспірантури":
                statusLabel=Status.orgStatuses[1];
                break;
            case "Відрахування з аспірантури":
                statusLabel=Status.orgStatuses[3];
                break;
        }
    }

    public void setContacts(ObjectContacts contacts) {
        this.contacts = contacts;
    }

    public ObjectContacts getContacts() {

        return contacts;
    }

    public String getKerivnik() {
        return kerivnik;
    }

    public void setKerivnik(String kerivnik) {
        this.kerivnik = kerivnik;
    }

    public String getStatusLabel() {
        return statusLabel;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ObjectAspirant(int id, String name, String speciality, Date birth, String kerivnik, int statusId,int year) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.date = birth;
        setStatusId(statusId);
        this.kerivnik = kerivnik;
        this.year = year;
    }

    public String getDate() {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date);
    }

    public void setDate(Date birth) {
        this.date = birth;
    }

    public int getId() {
        return id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return id+") "+name;
    }
}
