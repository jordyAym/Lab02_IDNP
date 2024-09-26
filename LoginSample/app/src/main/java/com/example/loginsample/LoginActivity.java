package com.example.loginsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsample.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public final static String LOGIN_SEND="";
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    private AccountEntity accountEntity;
    private String accountEntityString;


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

                /*if(edtUsername.getText().toString().equals("admin") && edtPassword.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(), "Bienvenido a mi APP", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Bienvenido a mi APP");

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("ACCOUNT", accountEntityString);

                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "Error en la autenticación", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error en la autenticación");
                }*/

                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                Log.d("LOINGIN", "B"+ validateCredentials(username, password));
                if (validateCredentials(username, password)) {
                    // Credenciales correctas: mostrar mensaje y abrir HomeActivity
                    Toast.makeText(getApplicationContext(), "Bienvenido " + username, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Bienvenido a mi APP");

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    String firstname = accountEntity.getFirstname();

                    //intent.putExtra("ACCOUNT", accountEntity);


                    Gson gson = new Gson();
                    String accountJson = gson.toJson(accountEntity);
                    intent.putExtra(LOGIN_SEND, accountJson);

                    startActivity(intent);
                } else {
                    // Credenciales incorrectas
                    Toast.makeText(getApplicationContext(), "Cuenta no encontrada", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnAddAccount.setOnClickListener(v ->  {
            //Creación del intent para la comunicacion con AccountActivity
            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
            //Ejecución del activity
            //startActivity(intent);

            //Llamando al activityResultLauncher
            activityResultLauncher.launch(intent);
        });

    }

    //Método para verificar las credenciales desde cuentas.txt
    private boolean validateCredentials(String username, String password) {
        try {
            // Abrir archivo cuentas.txt desde el almacenamiento interno
            InputStream inputStream = openFileInput("cuentas.txt");

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                Gson gson = new Gson();

                // Leer archivo línea por línea
                while ((line = reader.readLine()) != null) {
                    // Convertir cada línea a un objeto AccountEntity
                    AccountEntity account = gson.fromJson(line, AccountEntity.class);

                    // Verificar credenciales
                    if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                        accountEntity = account;
                        return true; // Login correcto
                    }
                }
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Credenciales no encontradas
    }

    // Método para guardar la cuenta en el archivo cuentas.txt
    private void saveAccountToFile(String accountJson) {
        try {
            // Abrir el archivo cuentas.txt en modo append (si no existe, lo crea)
            FileOutputStream fos = openFileOutput("cuentas.txt", Context.MODE_APPEND);
            fos.write((accountJson + "\n").getBytes());  // Escribir la cuenta en una nueva línea
            fos.close();
            Log.d("LoginActivity", "Cuenta guardada en cuentas.txt");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("LoginActivity", "Error al guardar la cuenta", e);
        }
    }

    ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(
            //Para abrir un segundo activity y recuperar los datos
            new ActivityResultContracts.StartActivityForResult(),
            //Callback
            new ActivityResultCallback<ActivityResult>() {
                @Override
                //Aquí se recupera el valor
                public void onActivityResult(ActivityResult activityResult) {
                    Integer resultCode = activityResult.getResultCode();
                    if (resultCode==AccountActivity.ACCOUNT_ACEPTAR){
                        Intent data = activityResult.getData();
                        //String account_record = data.getStringExtra(AccountActivity.ACCOUNT_RECORD);
                        accountEntityString = data.getStringExtra(AccountActivity.ACCOUNT_RECORD);
                        //Se recuperan los datos del gson
                        Gson gson = new Gson();
                        // AccountEntity accountEntity = gson.fromJson(account_record, AccountEntity.class);
                        accountEntity = gson.fromJson(accountEntityString, AccountEntity.class);

                        // Guardar la cuenta en cuentas.txt
                        saveAccountToFile(accountEntityString);

                        //Imprimiendo
                        String firstname = accountEntity.getFirstname();
                        Toast.makeText(getApplicationContext(), "Nombre:"+firstname, Toast.LENGTH_LONG).show();

                        Log.d("LoginActivity", "Nombre: "+firstname);
                    }
                    else if(resultCode == AccountActivity.ACCOUNT_CANCELAR){
                        Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_LONG).show();
                        Log.d("LoginActivity", "Cancelado");
                    }
                }
            }
    );
}