package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    //Método para abrir pantalla para registrar a persona que cuenta con el chaleco
    public void irAdmin(View view){
        Intent i = new Intent(this, RegisterAdminActivity.class);
        startActivity(i);
    }

    //Método para abrir pantalla para registrar a persona que va a monitorear el ritmo cardiaco de otra
    public void irUser(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

}