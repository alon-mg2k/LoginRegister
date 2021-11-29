package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginregister.AdminHome;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.loginregister.data.CO2StatusActivity;
import com.example.loginregister.data.CalibrationActivity;

public class PairingActivity extends AppCompatActivity {
    ListView dispList;
    Button buttonBack;
    SharedPreferences preferences;
    public static BluetoothClass btClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);

        dispList = findViewById(R.id.listaDisp);
        buttonBack = findViewById(R.id.btn_backb);
        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        String usertype = preferences.getString("tipoUs","");

        if (usertype.equals("Admin")) {
            AdminHome.btClass = new BluetoothClass(this,dispList, AdminHome.class);
            AdminHome.btClass.onBluetooth();

            dispList.setOnItemClickListener((adapterView,view,i,l) -> {
                AdminHome.btClass.bluetoothSeleccion(i);
            });
        }
        else if (usertype.equals("Users")) {
            CO2StatusActivity.btClass = new BluetoothClass(this,dispList,CO2StatusActivity.class);
            CO2StatusActivity.btClass.onBluetooth();

            dispList.setOnItemClickListener((adapterView,view,i,l) -> {
                CO2StatusActivity.btClass.bluetoothSeleccion(i);
            });
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usertype.equals("Admin")) {
                    startActivity(new Intent(getApplicationContext(),AdminHome.class));
                    overridePendingTransition(0,0);
                }
                else if (usertype.equals("Users")) {
                    startActivity(new Intent(getApplicationContext(), UserHome.class));
                    overridePendingTransition(0,0);
                }
            }
        });
    }
}