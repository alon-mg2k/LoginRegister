package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.loginregister.data.CO2StatusActivity;
import com.example.loginregister.data.RDUActivity;
import com.example.loginregister.data.RSUActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserHome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        //inicializamos variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.chat:
                        startActivity(new Intent(getApplicationContext(),
                                ChatActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:

                        return true;
                    case R.id.videocall:
                        startActivity(new Intent(getApplicationContext(),
                                VideoCallActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ajustes:
                        startActivity(new Intent(getApplicationContext(),
                                UAjustesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

        Button queryButton = findViewById(R.id.QueryButton);
        Button RCbutton = findViewById(R.id.RsButton);
        Button RSbutton = findViewById(R.id.RdButton);

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CO2StatusActivity.class));
                overridePendingTransition(0,0);
            }
        });

        RCbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RSUActivity.class));
                overridePendingTransition(0,0);
            }
        });

        RSbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RDUActivity.class));
                overridePendingTransition(0,0);
            }
        });
    }


    //Método para cerrar sesión de la cuenta y al completarse nos dirige a la pantalla para ingresar
    public void LogOut(View view){
        FirebaseAuth mAuth = null;
        if(mAuth.getCurrentUser() != null)
            mAuth.signOut();
        Intent i = new Intent(this, LoginRegisterActivity.class);
        startActivity(i);

    }
}
