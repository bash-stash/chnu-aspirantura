package com.chnu.aspirantura.aspirant;

import java.util.Date;

public class ObjectPractice {

    public String name;
    public String organization;
    public Date from;
    public Date till;

    public String markNational;
    public String markIectis;
    public int markPoints;
    public String credits;

    public String workDescription;
    public String commissionNames;

    public int id;

    public ObjectPractice(String name, String organization, Date from, Date till, String markNational, String markIectis, int markPoints, String credits, String workDescription, String commissionNamse, int id) {
        this.name = name;
        this.organization = organization;
        this.from = from;
        this.till = till;
        this.markNational = markNational;
        this.markIectis = markIectis;
        this.markPoints = markPoints;
        this.credits = credits;
        this.workDescription = workDescription;
        this.commissionNames = commissionNamse;
        this.id = id;
    }

}
