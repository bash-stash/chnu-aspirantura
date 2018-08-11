package com.chnu.aspirantura.aspirant;

public class ObjectResult {
    private String disName;
    private String vyklName;
    private String type;
    private String mark;
    private int course;
    private int semestr;
    private int id;

    public String getType() {
        return type;
    }

    public String getDisName() {
        return disName;
    }

    public String getVyklName() {
        return vyklName;
    }

    public String getMark() {
        return mark;
    }

    public int getCourse() {
        return course;
    }

    public int getSemestr() {
        return semestr;
    }


    public ObjectResult(String disName, String vyklName, String mark,String type, int course, int semestr,int id) {
        this.disName = disName;
        this.vyklName = vyklName;
        this.mark = mark;
        this.type = type;
        this.course = course;
        this.semestr = semestr;
        this.id = id;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getId() {

        return id;
    }
}
