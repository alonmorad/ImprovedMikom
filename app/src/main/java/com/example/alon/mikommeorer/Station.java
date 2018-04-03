package com.example.alon.mikommeorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.security.AccessControlContext;

/**
 * Created by alonm on 04/02/2018.
 */

public class Station implements Parcelable {
    private String description;
    private String hours;
    private String linenumber;
    private GeoPoint location;
    private String name;
    private String picture_url;

    public Station(GeoPoint location, String name, String description, String linenumber, String hours, String picture_url) {
        this.location = location;
        this.name = name;
        this.description = description;
        this.linenumber = linenumber;
        this.hours=hours;
        this.picture_url=picture_url;
    }

    public Station() {

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

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public LatLng getLocationLatLng() {
        return new LatLng(location.getLatitude(), location.getLongitude());
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

    public Station(Parcel in) {
        this.description = in.readString();
        this.name = in.readString();
        this.linenumber = in.readString();
        this.hours=in.readString();
        this.picture_url=in.readString();
        this.location = new GeoPoint(in.readDouble(), in.readDouble());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.description);
        parcel.writeString(this.name);
        parcel.writeString(this.linenumber);
        parcel.writeString(this.hours);
        parcel.writeString(this.picture_url);
        parcel.writeDouble(location.getLatitude());
        parcel.writeDouble(location.getLongitude());
        //parcel.writeDoubleArray(new double[]{this.location.getLatitude(), this.location.getLongitude()});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int i) {
            return new Station[i];
        }

    };
}
