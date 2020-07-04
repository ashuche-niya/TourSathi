package com.example.toursathi.ResponseModel;

public class singleGroupModel {
    String groupcode;
    String groupname;

    public String getGroupcode() {
        return groupcode;
    }

    public void setGroupcode(String groupcode) {
        this.groupcode = groupcode;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public singleGroupModel() {
    }

    public singleGroupModel(String groupcode, String groupname) {
        this.groupcode = groupcode;
        this.groupname = groupname;
    }
}
