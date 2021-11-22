package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginregister.AdminHome;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.loginregister.data.CalibrationActivity;

public class PairingActivity extends AppCompatActivity {
    ListView dispLista;
    Button buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);

        dispLista = findViewById(R.id.listaDisp);
        buttonBack = findViewById(R.id.btn_backb);

        AdminHome.btClass = new BluetoothClass(this,dispLista, CalibrationActivity.class);
        AdminHome.btClass.onBluetooth();

        dispLista.setOnItemClickListener((adapterView,view,i,l) -> {
            AdminHome.btClass.bluetoothSeleccion(i);
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminHome.class));
                overridePendingTransition(0,0);
            }
        });
    }
}