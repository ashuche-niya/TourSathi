package com.example.toursathi.ResponseModel;

public class singleGroupChatModel {
    String name;
    String mobileno;
    String message;
    String time;

    public singleGroupChatModel(String name, String mobileno, String message, String time) {
        this.name = name;
        this.mobileno = mobileno;
        this.message = message;
        this.time = time;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public singleGroupChatModel() {
    }

    public singleGroupChatModel(String name, String message, String time) {
        this.name = name;
        this.message = message;
        this.time = time;
    }
}
