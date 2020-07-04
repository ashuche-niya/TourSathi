package com.example.toursathi.ResponseModel;

public class singleGroupSplitwiseModel {
    String name;
    String mobileno;
    String paidmoney;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getPaidmoney() {
        return paidmoney;
    }

    public void setPaidmoney(String paidmoney) {
        this.paidmoney = paidmoney;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public singleGroupSplitwiseModel() {
    }

    public singleGroupSplitwiseModel(String name, String mobileno, String paidmoney, String description) {
        this.name = name;
        this.mobileno = mobileno;
        this.paidmoney = paidmoney;
        this.description = description;
    }
}
