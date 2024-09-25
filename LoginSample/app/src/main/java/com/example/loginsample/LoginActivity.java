package com.example.loginsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsample.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        EditText edtUsername = binding.edtUsername;
        EditText edtPassword = binding.edtPassword;
        Button btnLogin = binding.btnLogin;
        Button btnAddAccount = binding.btnAddAccount;


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtUsername.getText().toString().equals("admin") && edtPassword.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(), "Bienvenido a mi APP", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Bienvenido a mi APP");
                }else{
                    Toast.makeText(getApplicationContext(), "Error en la autenticación", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error en la autenticación");
                }
            }
        });
        btnAddAccount.setOnClickListener(v ->  {
            //Creación del intent para la comunicacion con AccountActivity
            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
            //Ejecución del activity
            startActivity(intent);
        });

    }
}