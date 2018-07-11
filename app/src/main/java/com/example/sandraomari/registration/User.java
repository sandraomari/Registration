package com.example.sandraomari.registration;

public class User {
    private int id, admissionNo;
    private String email;

    public User(int id, int admissionNo, String email){
        this.id = id;
        this.admissionNo = admissionNo;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public int getAdmissionNo() {
        return admissionNo;
    }

    public String getEmail() {
        return email;
    }
}
