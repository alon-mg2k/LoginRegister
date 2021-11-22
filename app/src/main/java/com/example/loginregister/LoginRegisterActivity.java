package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
    }

    //Método para dirigirnos a la pantalla de iniciar sesión
    public void irIniciar(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    //Método para dirigirnos a la pantalla de registrar
    public void irRegistrar(View view){
        Intent i = new Intent(this, UserActivity.class);
        startActivity(i);

    }
}