package com.chnu.aspirantura.nakaz;

public class ObjectTypeNakazi {
    private int id;
    private String value;


    public ObjectTypeNakazi(int id, String value) {
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
