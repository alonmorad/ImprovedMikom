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
    private String linenumber;
    private GeoPoint location;
    private String name;


    public Station(GeoPoint location, String name, String description, String linenumber) {
        this.location = location;
        this.name = name;
        this.description = description;
        this.linenumber = linenumber;
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
