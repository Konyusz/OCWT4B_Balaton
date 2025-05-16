package com.example.ocwt4b_balataon_latnivalok;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private FirebaseAuth auth;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(
                R.anim.no_anim,
                R.anim.slide_out_bottom
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(
                R.anim.no_anim,
                R.anim.slide_out_bottom
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        emailEditText    = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton      = findViewById(R.id.buttonLogin);
        registerButton   = findViewById(R.id.buttonRegister);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String jelszo = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || jelszo.isEmpty()) {
                Toast.makeText(this, "Kérem töltsd ki az összes mezőt", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, jelszo)
                    .addOnSuccessListener(authResult -> {
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this,
                                "Sikertelen bejelentkezés: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(
                    R.anim.slide_in_bottom,
                    R.anim.no_anim
            );
        });
    }
}