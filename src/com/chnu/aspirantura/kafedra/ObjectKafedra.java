package com.chnu.aspirantura.kafedra;

public class ObjectKafedra {

    private int id;
    private String room;
    private String name;
    private String speciality;


    public ObjectKafedra(int id, String name, String room) {
        this.id = id;
        this.room = room;
        this.name = name;
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

    public String getRoom() {
        return room;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
      return id +") " + name +" | " + room;
    }
}
