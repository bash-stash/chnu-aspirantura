package com.chnu.aspirantura.aspirant;

public class ObjectContacts {
    private String telephone;
    private String email;
    private String address;

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public ObjectContacts(String telephone, String email, String address) {

        this.telephone = telephone;
        this.email = email;
        this.address = address;
    }
}
