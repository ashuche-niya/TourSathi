package com.example.toursathi.Constant;

import com.example.toursathi.ResponseModel.User;
import com.example.toursathi.ResponseModel.singleGroupChatModel;
import com.example.toursathi.ResponseModel.singleGroupDetailModel;
import com.example.toursathi.ResponseModel.singleGroupModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("singin/")
    Call<User> getSignIn(@Field("mobileno") String mobileno);

    @FormUrlEncoded
    @POST("registeruser/")
    Call<User> getRegister(@Field("mobileno") String mobileno, @Field("name") String name, @Field("email") String email);


    @FormUrlEncoded
    @POST("homepage/")
    Call<List<singleGroupModel>> getGroupList(@Field("mobileno") String mobileno);

    @FormUrlEncoded
    @POST("groupdetail/")
    Call<List<singleGroupDetailModel>> getGroupDetails(@Field("groupcode") String groupcode);

    @FormUrlEncoded
    @POST("groupchat/")
    Call<List<singleGroupChatModel>> getGroupChat(@Field("groupcode") String groupcode);

    @FormUrlEncoded
    @POST("creategroup/")
    Call<singleGroupModel> getGroupModel(@Field("groupname") String groupname,@Field("mobileno") String mobileno,@Field("name") String name);


    @FormUrlEncoded
    @POST("uploadimage/")
    Call<String> uploadimagecall(@Field("groupcode") String groupcode,@Field("mobileno") String mobileno,@Field("name") String name);

    @FormUrlEncoded
    @POST("sendmsg/")
    Call<String> sendmsgcall(@Field("groupcode") String groupcode,@Field("mobileno") String mobileno
            ,@Field("name") String name,@Field("message") String message
    ,@Field("time") String time);
}
