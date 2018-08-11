package com.chnu.aspirantura.aspirant;

import java.time.LocalDate;

public class ObjectPassport {

        private String seria;
        private String number;
        private String publishedBy;
        private LocalDate publishedDate;
        private String identificator;

    public String getSeria() {
        return seria;
    }

    public String getNumber() {
        return number;
    }

    public String getPublishedBy() {
        return publishedBy;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public String getIdentificator() {
        return identificator;
    }

    public ObjectPassport(String seria, String number, String publishedBy, LocalDate publishedDate, String identificator) {

        this.seria = seria;
        this.number = number;
        this.publishedBy = publishedBy;
        this.publishedDate = publishedDate;
        this.identificator = identificator;
    }
}
