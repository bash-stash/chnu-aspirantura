package com.chnu.aspirantura.aspirant;

import java.util.Date;

public class ObjectScienceWork {

    public Date date;
    public String where;
    public String name;
    public String link;
    public int id;


    public ObjectScienceWork(int id,Date date, String where, String name, String link) {
        this.date = date;
        this.where = where;
        this.name = name;
        this.link = link;
        this.id = id;
    }

}
