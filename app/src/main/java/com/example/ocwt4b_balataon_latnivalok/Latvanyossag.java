package com.example.ocwt4b_balataon_latnivalok;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void addLatvanyossag(Latvanyossag l) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("latvanyossagok")
                .document(l.getId())
                .set(l)
                .addOnSuccessListener(aVoid ->
                        Log.d("CRUD", "Létrehozva: " + l.getId()))
                .addOnFailureListener(e ->
                        Log.e("CRUD", "Hiba a létrehozáskor", e));
    }

    private void getAllLatvanyossagok() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("latvanyossagok")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Latvanyossag> lista = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Latvanyossag l = doc.toObject(Latvanyossag.class);
                        lista.add(l);
                    }
                })
                .addOnFailureListener(e ->
                        Log.e("CRUD", "Hiba a lekérdezéskor", e));
    }

    private void getLatvanyossagById(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("latvanyossagok")
                .document(id)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Latvanyossag l = doc.toObject(Latvanyossag.class);
                    }
                })
                .addOnFailureListener(e ->
                        Log.e("CRUD", "Hiba a dokumentum lekérésekor", e));
    }

    private void updateLatvanyossag(Latvanyossag l) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> changes = new HashMap<>();
        changes.put("leiras", l.getLeiras());
        changes.put("kepUrl", l.getKepUrl());
        // bővítheted más mezőkkel is...

        db.collection("latvanyossagok")
                .document(l.getId())
                .update(changes)
                .addOnSuccessListener(aVoid ->
                        Log.d("CRUD", "Frissítve: " + l.getId()))
                .addOnFailureListener(e ->
                        Log.e("CRUD", "Hiba a frissítéskor", e));
    }
    private void deleteLatvanyossag(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("latvanyossagok")
                .document(id)
                .delete()
                .addOnSuccessListener(aVoid ->
                        Log.d("CRUD", "Törölve: " + id))
                .addOnFailureListener(e ->
                        Log.e("CRUD", "Hiba a törléskor", e));
    }
}