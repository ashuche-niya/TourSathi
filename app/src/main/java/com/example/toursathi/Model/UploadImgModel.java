package com.example.toursathi.Model;

public class UploadImgModel {
    String imagepath;
    String imagename;
    String imageuploadedby;

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getImageuploadedby() {
        return imageuploadedby;
    }

    public void setImageuploadedby(String imageuploadedby) {
        this.imageuploadedby = imageuploadedby;
    }

    public UploadImgModel(String imagepath) {
        this.imagepath = imagepath;
    }

    public UploadImgModel() {
    }

    public UploadImgModel(String imagepath, String imagename, String imageuploadedby) {
        this.imagepath = imagepath;
        this.imagename = imagename;
        this.imageuploadedby = imageuploadedby;
    }
}
