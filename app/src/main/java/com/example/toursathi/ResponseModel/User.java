package com.example.toursathi.ResponseModel;

public class User {
    String mobileno;
    String name;
    String email;
    String existence;

    public String getExistence() {
        return existence;
    }

    public void setExistence(String existence) {
        this.existence = existence;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }

    public User(String mobileno, String name, String email) {
        this.mobileno = mobileno;
        this.name = name;
        this.email = email;
    }

    public User(String mobileno, String name, String email, String existence) {
        this.mobileno = mobileno;
        this.name = name;
        this.email = email;
        this.existence = existence;
    }
}
