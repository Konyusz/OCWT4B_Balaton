package com.example.ocwt4b_balataon_latnivalok;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LatvanyossagAdapter adapter;
    private List<Latvanyossag> lista = new ArrayList<>();
    private FirebaseFirestore db;

    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;

    private final ActivityResultLauncher<Intent> listaLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    int pozicio = result.getData().getIntExtra("kivalasztott_pozicio", -1);
                    if (pozicio >= 0 && pozicio < lista.size()) {
                        viewPager.setCurrentItem(pozicio, true);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        adapter = new LatvanyossagAdapter(lista);
        viewPager.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        kerEngedelytEsKerHelyet();

        Button logoutButton = findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        Button listazButton = findViewById(R.id.buttonListazas);
        listazButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListaActivity.class);
            intent.putStringArrayListExtra("latvanyossagok_nevek", getLatvanyossagNevek());
            listaLauncher.launch(intent);
        });

        Button editButton = findViewById(R.id.buttonEdit);
        editButton.setOnClickListener(v -> {
            int currentPosition = viewPager.getCurrentItem();
            Latvanyossag l = lista.get(currentPosition);
            Intent intent = new Intent(this, EditLatvanyossagActivity.class);
            intent.putExtra("id",        l.getId());
            intent.putExtra("nev",       l.getNev());
            intent.putExtra("leiras",    l.getLeiras());
            intent.putExtra("kepUrl",    l.getKepUrl());
            intent.putExtra("lat",       l.getLat());
            intent.putExtra("lng",       l.getLng());
            startActivity(intent);
        });

        Button addButton = findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddLatvanyossagActivity.class);
            addLauncher.launch(intent);
        });
    }

    private void kerEngedelytEsKerHelyet() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        } else {
            getHelyzetEsLatvanyossagok();
        }
    }

    private void getHelyzetEsLatvanyossagok() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1001
            );
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        currentLocation = location;
                        betoltLatvanyossagok();
                    } else {
                        Toast.makeText(this, "Nem elérhető a helyzet", Toast.LENGTH_SHORT).show();
                        betoltLatvanyossagok();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba a helyadat lekérésekor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    betoltLatvanyossagok();
                });
    }

    private void betoltLatvanyossagok() {
        db.collection("latvanyossagok").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    lista.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Latvanyossag l = doc.toObject(Latvanyossag.class);
                        assert l != null;
                        l.setId(doc.getId());
                        lista.add(l);
                    }

                    if (currentLocation != null) {
                        lista.sort((a, b) -> {
                            float[] distA = new float[1];
                            float[] distB = new float[1];
                            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), a.getLat(), a.getLng(), distA);
                            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), b.getLat(), b.getLng(), distB);
                            return Float.compare(distA[0], distB[0]);
                        });
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    getHelyzetEsLatvanyossagok();
                }
            }
    );

    private ArrayList<String> getLatvanyossagNevek() {
        ArrayList<String> nevek = new ArrayList<>();
        for (Latvanyossag l : lista) {
            nevek.add(l.getNev());
        }
        return nevek;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getHelyzetEsLatvanyossagok();
        } else {
            Toast.makeText(this, "Helymeghatározás engedély megtagadva", Toast.LENGTH_SHORT).show();
            betoltLatvanyossagok();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getHelyzetEsLatvanyossagok();
    }
}
