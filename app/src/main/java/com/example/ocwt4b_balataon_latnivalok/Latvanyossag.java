package com.example.ocwt4b_balataon_latnivalok;

public class Latvanyossag {
    private String id; // Firestore dokumentum azonosítója
    private String nev;
    private String leiras;
    private String kepUrl;
    private double lat; // Szélességi fok
    private double lng; // Hosszúsági fok

    public Latvanyossag() {

    }

    public Latvanyossag(String id, String nev, String leiras, String kepUrl, double lat, double lng) {
        this.id = id;
        this.nev = nev;
        this.leiras = leiras;
        this.kepUrl = kepUrl;
        this.lat = lat;
        this.lng = lng;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeiras() {
        return leiras;
    }

    public void setLeiras(String leiras) {
        this.leiras = leiras;
    }

    public String getKepUrl() {
        return kepUrl;
    }

    public void setKepUrl(String kepUrl) {
        this.kepUrl = kepUrl;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}