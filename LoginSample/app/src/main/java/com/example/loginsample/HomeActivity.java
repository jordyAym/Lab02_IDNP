package com.example.loginsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String accountJson = intent.getStringExtra(LoginActivity.LOGIN_SEND);

        if (accountJson != null) {
            Gson gson = new Gson();
            AccountEntity accountEntity = gson.fromJson(accountJson, AccountEntity.class);

            // Ahora tienes el objeto AccountEntity deserializado
            Log.d("HomeActivity", "Recibido: " + accountEntity.getFirstname());
            TextView edtTextBienvenida = findViewById(R.id.edtTextBienvenida);
            String firstname = accountEntity.getFirstname();
            String lastname = accountEntity.getLastname();

            edtTextBienvenida.setText("Bienvenido " + firstname + " " + lastname);
        } else {
            Log.d("HomeActivity", "No se recibió ningún objeto de cuenta");
        }
    }
}