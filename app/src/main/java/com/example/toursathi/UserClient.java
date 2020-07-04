package com.example.toursathi;

import android.app.Application;

import com.example.toursathi.Model.UserLocation;
import com.example.toursathi.ResponseModel.User;
import com.example.toursathi.ResponseModel.singleGroupDetailModel;
import com.example.toursathi.ResponseModel.singleGroupModel;

import java.util.ArrayList;
import java.util.List;

public class UserClient extends Application {
    User user =new User();
    singleGroupModel singleGroupModel= new singleGroupModel();
    List<singleGroupModel> singleGroupModelList = new ArrayList<>();
    List<singleGroupDetailModel> groupdetaillist = new ArrayList<>();
    UserLocation userLocation = new UserLocation();

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public List<singleGroupDetailModel> getGroupdetaillist() {
        return groupdetaillist;
    }

    public void setGroupdetaillist(List<singleGroupDetailModel> groupdetaillist) {
        this.groupdetaillist = groupdetaillist;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public com.example.toursathi.ResponseModel.singleGroupModel getSingleGroupModel() {
        return singleGroupModel;
    }

    public void setSingleGroupModel(com.example.toursathi.ResponseModel.singleGroupModel singleGroupModel) {
        this.singleGroupModel = singleGroupModel;
    }

    public List<com.example.toursathi.ResponseModel.singleGroupModel> getSingleGroupModelList() {
        return singleGroupModelList;
    }

    public void setSingleGroupModelList(List<com.example.toursathi.ResponseModel.singleGroupModel> singleGroupModelList) {
        this.singleGroupModelList = singleGroupModelList;
    }

    public UserClient() {
    }

    public UserClient(User user, com.example.toursathi.ResponseModel.singleGroupModel singleGroupModel, List<com.example.toursathi.ResponseModel.singleGroupModel> singleGroupModelList) {
        this.user = user;
        this.singleGroupModel = singleGroupModel;
        this.singleGroupModelList = singleGroupModelList;
    }
}
