package com.example.ocwt4b_balataon_latnivalok;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LatvanyossagAdapter adapter;
    private List<Latvanyossag> lista = new ArrayList<>();

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        adapter = new LatvanyossagAdapter(lista);
        viewPager.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        betoltLatvanyossagok();

        Button logoutButton = findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Firebase kijelentkezés
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish(); // Ne lehessen visszalépni
        });


        for (Latvanyossag l : lista) {
            db.collection("latvanyossagok")
                    .add(l)
                    .addOnSuccessListener(documentReference ->
                            Log.d("FIREBASE", "Sikeres feltöltés: " + documentReference.getId()))
                    .addOnFailureListener(e ->
                            Log.e("FIREBASE", "Hiba a feltöltéskor", e));
        }
    }


    private void betoltLatvanyossagok() {
        db.collection("latvanyossagok").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    lista.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Latvanyossag l = doc.toObject(Latvanyossag.class);
                        lista.add(l);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

