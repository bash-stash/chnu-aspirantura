package com.chnu.aspirantura.aspirant;

import java.util.Date;

public class ObjectHistoryAspirant {

    public Date date;
    public String madeBy;
    public String type;
    public String nakazNumber;
    public Date nakazDate;

    public ObjectHistoryAspirant(Date date, String madeBy, String type, String nakazNumber, Date nakazDate) {
        this.nakazDate = nakazDate;
        this.date = date;
        this.madeBy = madeBy;
        this.type = type;
        this.nakazNumber = nakazNumber;
    }
}
