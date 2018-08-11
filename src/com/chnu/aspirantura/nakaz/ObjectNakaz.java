package com.chnu.aspirantura.nakaz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ObjectNakaz {
    private int id;
    private String number;
    private String about;
    private Date date;
    private String type;

    @Override
    public String toString() {
    return id+") " + number;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getAbout() {
        return about;
    }

    public String getDate(){
        return new SimpleDateFormat("dd.mm.yyyy", Locale.getDefault()).format(date);
    }

    public String getType() {
        return type;
    }

    public ObjectNakaz(int id, String number, Date date, String type) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.type = type;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}
