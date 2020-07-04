package com.example.toursathi.Model;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserLocation {

    public GeoPoint geo_point;
    public  @ServerTimestamp
    Date timestamp;

    public UserLocation(GeoPoint geo_point, Date timestamp) {
        this.geo_point = geo_point;
        this.timestamp = timestamp;
    }

    public UserLocation() {
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
