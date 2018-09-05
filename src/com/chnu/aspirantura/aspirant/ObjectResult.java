package com.chnu.aspirantura.aspirant;

public class ObjectResult {
    private String disName;
    private String vyklName;
    private String type;

    private String markNational="asd";
    private String markIECTIS="asd";
    private String markPoints="asd";

    private int course;
    private int semestr;
    private int id;

    public ObjectResult(String disName, String vyklName, String type, String markNational, String markIECTIS, String markPoints, int course, int semestr, int id) {
        this.disName = disName;
        this.vyklName = vyklName;
        this.type = type;
        this.markNational = markNational;
        this.markIECTIS = markIECTIS;
        this.markPoints = markPoints;
        this.course = course;
        this.semestr = semestr;
        this.id = id;
    }


    public String getType() {
        return type;
    }

    public String getDisName() {
        return disName;
    }

    public String getVyklName() {
        return vyklName;
    }

    public String getMarkNational() {
        return markNational;
    }

    public int getCourse() {
        return course;
    }

    public int getSemestr() {
        return semestr;
    }

    public String getMarkIECTIS() {
        return markIECTIS;
    }

    public String getMarkPoints() {
        return markPoints;
    }

    public int getId() {
        return id;
    }


    public void setMarkNational(String markNational) {
        this.markNational = markNational;
    }

    public void setMarkIECTIS(String markIECTIS) {
        this.markIECTIS = markIECTIS;
    }

    public void setMarkPoints(String markPoints) {
        this.markPoints = markPoints;
    }
}
