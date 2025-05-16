package com.example.ocwt4b_balataon_latnivalok;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button registerButton, backToLoginButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        emailEditText     = findViewById(R.id.editTextEmail);
        passwordEditText  = findViewById(R.id.editTextPassword);
        registerButton    = findViewById(R.id.buttonRegister);
        backToLoginButton = findViewById(R.id.buttonBack);

        overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "regisztracio_csatorna",
                    "Regisztrációs Értesítések",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String jelszo = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || jelszo.isEmpty()) {
                Toast.makeText(this, "Minden mező kitöltése kötelező", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, jelszo)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(this, "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "regisztracio_csatorna")
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("Sikeres regisztráció")
                                .setContentText("Üdvözlünk az alkalmazásban!")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(
                                        this,
                                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                                        1001
                                );
                            }
                        }
                        notificationManager.notify(1, builder.build());

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        backToLoginButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Értesítési engedély megadva", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Értesítési engedély megtagadva", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
    }
}