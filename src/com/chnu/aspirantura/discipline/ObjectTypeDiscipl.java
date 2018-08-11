package com.chnu.aspirantura.discipline;

public class ObjectTypeDiscipl {
    private int id;
    private String value;


    public ObjectTypeDiscipl(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return id +") " + value;
    }

    public String getValue() {
        return value;
    }

}
