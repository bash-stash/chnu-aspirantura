package com.chnu.aspirantura.vykladach;

import com.chnu.aspirantura.speciality.ObjectSpeciality;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ObjectVykladach {
    private int id;
    private String name;
    private ObservableList<ObjectSpeciality> specialities = FXCollections.observableArrayList();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ObjectVykladach(int id, String name) {

        this.id = id;
        this.name = name;
    }


    public ObservableList<ObjectSpeciality> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(ObservableList<ObjectSpeciality> specialities) {
        this.specialities = specialities;
    }

    public String isKerivnik(){
        return specialities.size()!=0?"Так": "Ні";
    }
    @Override
    public String toString() {
        return id+") " + name;
    }
}
