package com.example.alon.mikommeorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

/**
 * Created by alonm on 04/02/2018.
 */

public class Station {
    private GeoPoint location;
    private String name;
    private String description;

    public Station(GeoPoint location, String name, String description) {
        this.location = location;
        this.name = name;
        this.description = description;
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

    public MarkerOptions toMarkerOptions(Context context) {
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
                '}';
    }
}
