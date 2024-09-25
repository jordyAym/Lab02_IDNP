package com.example.loginsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;

public class AccountActivity extends AppCompatActivity {

    public final static String ACCOUNT_RECORD="";

    public final static Integer ACCOUNT_ACEPTAR=100;
    public final static Integer ACCOUNT_CANCELAR=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnAceptar = findViewById(R.id.btnAceptar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        //Creación de variables para recuperar los valores del formulario

        EditText edtFirstName = findViewById(R.id.edtFirstname);
        EditText edtLastname = findViewById(R.id.edtLastname);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPhone = findViewById(R.id.edtPhone);
        EditText edtUsername2 = findViewById(R.id.edtUsername2);
        EditText edtPassword2 = findViewById(R.id.edtPassword2);


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Aquí se recuperan los datos del formulario
                AccountEntity accountEntity =  new AccountEntity();
                accountEntity.setFirstname(edtFirstName.getText().toString());
                accountEntity.setLastname(edtLastname.getText().toString());
                accountEntity.setEmail(edtEmail.getText().toString());
                accountEntity.setPhone(edtPhone.getText().toString());
                accountEntity.setUsername(edtUsername2.getText().toString());
                accountEntity.setPassword(edtPassword2.getText().toString());

                // Guardar la cuenta en el archivo cuentas.txt
                saveAccount(accountEntity);

                Gson gson = new Gson();
                String accountJson = gson.toJson(accountEntity);

                Intent data = new Intent();
                data.putExtra(ACCOUNT_RECORD, accountJson);

                setResult(ACCOUNT_ACEPTAR, data);
                finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(ACCOUNT_CANCELAR);
                finish();
            }
        });
    }

    //Método para guardar una cuenta con todos sus datos
    private void saveAccount(AccountEntity accountEntity) {
        //Convertir la cuenta a una cadena JSON
        Gson gson = new Gson();
        String accountJson = gson.toJson(accountEntity) + "\n";

        try {
            //Abrir el archivo cuentas.txt en modo de añadir (append)
            FileOutputStream fos = openFileOutput("cuentas.txt", Context.MODE_APPEND);
            fos.write(accountJson.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}