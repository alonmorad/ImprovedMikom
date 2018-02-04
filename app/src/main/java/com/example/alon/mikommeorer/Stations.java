package com.example.alon.mikommeorer;

/**
 * Created by alonm on 04/02/2018.
 */

public class Stations {
    private double longitude;
    private double latitude;
    private String station_name;
    private String station_description;

    public Stations(double longitude, double latitude, String station_name, String station_description) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.station_name = station_name;
        this.station_description = station_description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getStation_description() {
        return station_description;
    }

    public void setStation_description(String station_description) {
        this.station_description = station_description;
    }


}
