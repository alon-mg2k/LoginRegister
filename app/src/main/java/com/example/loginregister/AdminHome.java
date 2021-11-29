package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginregister.data.*;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class AdminHome extends AppCompatActivity {
    public static BluetoothClass btClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_admin_home);

        //inicializamos variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        if (btClass == null) {
            this.onResume();
            startActivity(new Intent(getApplicationContext(),PairingActivity.class));
            overridePendingTransition(0,0);
        }
        else {
            btClass = new BluetoothClass(this, PairingActivity.class);
        }

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
                                AjustesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });



        Button calButton = findViewById(R.id.QueryButton);
        Button RecordDynamicButton = findViewById(R.id.RdButton);
        Button RecordStaticButton = findViewById(R.id.RsButton);
        Button ReportGeneralButton = findViewById(R.id.RPButton);

        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CalibrationActivity.class));
                overridePendingTransition(0,0);
            }
        });

        RecordDynamicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RDActivity.class));
                overridePendingTransition(0,0);
            }
        });

        RecordStaticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RSActivity.class));
                overridePendingTransition(0,0);
            }
        });

        ReportGeneralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RQActivity.class));
                overridePendingTransition(0,0);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause() { super.onPause();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}



   