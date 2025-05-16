package com.example.ocwt4b_balataon_latnivalok;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditLatvanyossagActivity extends AppCompatActivity {

    private EditText etNev, etLeiras, etKepUrl, etLat, etLng;
    private Button btnUpdate, btnDelete;

    private String docId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_latvanyossag);

        etNev     = findViewById(R.id.etNev);
        etLeiras  = findViewById(R.id.etLeiras);
        etKepUrl  = findViewById(R.id.etKepUrl);
        etLat     = findViewById(R.id.etLat);
        etLng     = findViewById(R.id.etLng);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        docId    = intent.getStringExtra("id");
        etNev.setText(intent.getStringExtra("nev"));
        etLeiras.setText(intent.getStringExtra("leiras"));
        etKepUrl.setText(intent.getStringExtra("kepUrl"));
        etLat.setText(String.valueOf(intent.getDoubleExtra("lat", 0)));
        etLng.setText(String.valueOf(intent.getDoubleExtra("lng", 0)));

        btnUpdate.setOnClickListener(v -> {
            Map<String,Object> changes = new HashMap<>();
            changes.put("nev",    etNev.getText().toString().trim());
            changes.put("leiras", etLeiras.getText().toString().trim());
            changes.put("kepUrl", etKepUrl.getText().toString().trim());
            changes.put("lat",    Double.parseDouble(etLat.getText().toString().trim()));
            changes.put("lng",    Double.parseDouble(etLng.getText().toString().trim()));

            db.collection("latvanyossagok")
                    .document(docId)
                    .update(changes)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Sikeres módosítás", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });

        btnDelete.setOnClickListener(v -> {
            db.collection("latvanyossagok")
                    .document(docId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Sikeres törlés", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
        Button back = findViewById(R.id.buttonBack);
        back.setOnClickListener(v -> finish());
    }
}