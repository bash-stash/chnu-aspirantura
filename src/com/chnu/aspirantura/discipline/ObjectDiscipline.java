package com.chnu.aspirantura.discipline;

import java.util.Objects;

public class ObjectDiscipline{

    private int id;
    private String  name;
    private String  status;
    private String  typeKontrol;
    private String vykladach;
    private String speciality;
    private String formValue;
    private int form;
    private int semestr;
    private int course;


    public ObjectDiscipline(int id, String name, String status, int simestr,String typeKontrol,int course) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.semestr = simestr;
        this.typeKontrol = typeKontrol;
        this.course = course;
    }

    public void setFormValue(int  form){
        if (form==1) formValue="Денна";
        if (form==2) formValue="Заочна";
        if (form==3) formValue="Вечірня";
        this.form = form;
    }

    public int getForm() {
        return form;
    }

    public String getVykladach() {
        return vykladach;
    }

    public void setVykladach(String vykladach) {
        this.vykladach = vykladach;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getTypeKontrol() {
        return typeKontrol;
    }

    public int getSemestr(){
        return semestr;
    }

    public String getSpeciality() {
        return speciality;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return id+") "+name;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectDiscipline that = (ObjectDiscipline) o;
        return id == that.id &&
                semestr == that.semestr &&
                Objects.equals(name, that.name) &&
                Objects.equals(status, that.status) &&
                Objects.equals(typeKontrol, that.typeKontrol) &&
                Objects.equals(vykladach, that.vykladach) &&
                Objects.equals(course, that.course) &&
                Objects.equals(speciality, that.speciality);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, status, typeKontrol, vykladach, speciality, semestr,course);
    }
}
