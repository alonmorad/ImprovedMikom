package com.example.alon.mikommeorer;

/**
 * Created by alonm on 04/02/2018.
 */

public class Lines {
    private int numofstations;
    private Stations [] stations= new Stations[numofstations];
    private int kav_number;
    private String starttime;
    private String endtime;

    public int getNumofstations() {
        return numofstations;
    }

    public void setNumofstations(int numofstations) {
        this.numofstations = numofstations;
    }

    public Stations[] getStations() {
        return stations;
    }

    public void setStations(Stations[] stations) {
        this.stations = stations;
    }

    public int getKav_number() {
        return kav_number;
    }

    public void setKav_number(int kav_number) {
        this.kav_number = kav_number;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }



    public Lines(int numofstations, Stations[] stations, int kav_number, String starttime, String endtime) {
        this.numofstations = numofstations;
        this.stations = stations;
        this.kav_number = kav_number;
        this.starttime = starttime;
        this.endtime = endtime;
    }

}
