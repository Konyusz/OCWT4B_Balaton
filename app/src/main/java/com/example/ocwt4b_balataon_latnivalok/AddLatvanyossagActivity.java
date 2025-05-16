package com.example.ocwt4b_balataon_latnivalok;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AddLatvanyossagActivity extends AppCompatActivity {

    private EditText etNev, etLeiras, etKepUrl, etLat, etLng;
    private Button btnSave;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_latvanyossag);

        db = FirebaseFirestore.getInstance();

        etNev     = findViewById(R.id.etNev);
        etLeiras  = findViewById(R.id.etLeiras);
        etKepUrl  = findViewById(R.id.etKepUrl);
        etLat     = findViewById(R.id.etLat);
        etLng     = findViewById(R.id.etLng);
        btnSave   = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String nev    = etNev.getText().toString().trim();
            String leiras = etLeiras.getText().toString().trim();
            String url    = etKepUrl.getText().toString().trim();
            String latS   = etLat.getText().toString().trim();
            String lngS   = etLng.getText().toString().trim();

            if (nev.isEmpty() || leiras.isEmpty() || url.isEmpty() || latS.isEmpty() || lngS.isEmpty()) {
                Toast.makeText(this, "Minden mezőt tölts ki!", Toast.LENGTH_SHORT).show();
                return;
            }

            double lat = Double.parseDouble(latS);
            double lng = Double.parseDouble(lngS);
            String id = UUID.randomUUID().toString();  // egyedi ID

            Latvanyossag uj = new Latvanyossag(id, nev, leiras, url, lat, lng);
            db.collection("latvanyossagok")
                    .document(id)
                    .set(uj)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Sikeres hozzáadás!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
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
