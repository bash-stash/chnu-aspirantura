package com.chnu.aspirantura.discipline;

import java.util.Objects;

public class ObjectStudyForm {
    private int id;
    private String form;

    public ObjectStudyForm(int id, String form) {
        this.id = id;
        this.form = form;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, form);
    }

    @Override
    public String toString() {
        return id+") "+form;
    }
}

