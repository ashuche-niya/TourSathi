package com.example.toursathi.ResponseModel;

public class singleGroupDetailModel {
    String name;
    String mobileno;
    String paidmoney;

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

    public singleGroupDetailModel() {
    }

    public singleGroupDetailModel(String name, String mobileno, String paidmoney) {
        this.name = name;
        this.mobileno = mobileno;
        this.paidmoney = paidmoney;
    }
}
