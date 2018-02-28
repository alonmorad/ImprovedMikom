package com.example.alon.mikommeorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.security.AccessControlContext;

/**
 * Created by alonm on 04/02/2018.
 */

public class Station {
    private String description;
    private String linenumber;
    private GeoPoint location;
    private String name;


    public Station(GeoPoint location, String name, String description, String linenumber) {
        this.location = location;
        this.name = name;
        this.description = description;
        this.linenumber=linenumber;
    }

    public Station(){

    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLocationLatLng() {
        return new LatLng(location.getLatitude(),location.getLongitude());
    }
    public String getLinenumber() {
        return linenumber;
    }

    public void setLinenumber(String linenumber) {
        this.linenumber = linenumber;
    }

    public MarkerOptions toMarkerOptions(AccessControlContext context) {
        return new MarkerOptions()
                .position(getLocationLatLng())
                .title(getName())
                .snippet(getDescription());
    }

    @Override
    public String toString() {
        return "Station{" +
                "location=" + location +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", linenumber='" + linenumber + '\'' +
                '}';
    }
}
