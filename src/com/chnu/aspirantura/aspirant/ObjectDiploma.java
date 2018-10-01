package com.chnu.aspirantura.aspirant;

public class ObjectDiploma {
    private int id;
    private String title;
    private String description;
    private int status;

    public ObjectDiploma(int id, String title, String description, int status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }
}
