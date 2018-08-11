package com.chnu.aspirantura.speciality;

import com.chnu.aspirantura.kafedra.ObjectKafedra;
import com.chnu.aspirantura.faculty.ObjectFaculty;

import java.util.ArrayList;
import java.util.Objects;

public class ObjectSpeciality{
    private int id;
    private String name;
    private String code;
    private String status;
    private ObjectFaculty faculty;
    private ObjectKafedra kafedra;

    public ArrayList<Integer> getKerivniki_id() {
        return kerivniki_id;
    }

    public ObjectFaculty getFaculty() {
        return faculty;
    }


    public ObjectKafedra getKafedra() {
        return kafedra;
    }

    private ArrayList<Integer> kerivniki_id;


    public void setFaculty(ObjectFaculty faculty) {
        this.faculty = faculty;
    }

    public ObjectSpeciality(int id, String name, String status, String code) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.code = code;
    }

    public ObjectSpeciality(int id, String name, String code){
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }


    public void setKerivniki_id(ArrayList<Integer> kerivniki_id) {
        this.kerivniki_id = kerivniki_id;
    }

    public void setKafedra(ObjectKafedra kafedra) {
        this.kafedra = kafedra;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectSpeciality that = (ObjectSpeciality) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(faculty, that.faculty) &&
                Objects.equals(kafedra, that.kafedra) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code,faculty,kafedra,status);
    }

    @Override
    public String toString() {
        return id+") "+code+" | "+name;
    }
}
