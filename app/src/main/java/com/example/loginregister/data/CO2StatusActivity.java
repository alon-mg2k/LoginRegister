package com.example.loginregister.data;

import static bolts.Task.delay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginregister.BluetoothClass;
import com.example.loginregister.PairingActivity;
import com.example.loginregister.R;
import com.example.loginregister.UserHome;

public class CO2StatusActivity extends AppCompatActivity {
    public static BluetoothClass btClass;
    Button backButton;
    Button verifyButton;
    Boolean initHilo = false;
    Boolean hilo = true;
    TextView CO2Status;
    TextView CO2Message;
    TextView PPMData;
    String[] dataRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co2_status);

        backButton = findViewById(R.id.BackButton);
        verifyButton = findViewById(R.id.verifyButton);
        CO2Status = findViewById(R.id.tv_statusCO2f);
        PPMData = findViewById(R.id.tv_ppmdata);
        CO2Message = findViewById(R.id.tv_messagewarning);

        btClass = new BluetoothClass(this,PairingActivity.class);
        btClass.exitErrorOk(true);
        btClass.mensajeConexion(getResources().getString(R.string.txt_connectionsuccess));
        btClass.mensajeConexion(getResources().getString(R.string.txt_connectionfailed));
        initHilo = btClass.conectaBluetooth();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hilo = false;
                initHilo = false;
                delay(500L);
                btClass.mTx(";");
                delay(500L);
                btClass.exitConexion();
                startActivity(new Intent(getApplicationContext(),
                        UserHome.class));
                overridePendingTransition(0, 0);
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (!initHilo) {
                    try {
                        Thread.sleep(200L);
                        initHilo = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                hilo = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (hilo) {
                            dataRecording = new String[2];
                            for (int i = 0; i < 2; i++) {
                                if (i == 0) { structureData(0,dataRecording[0],"3");}
                                if (i == 1) { structureData(1,dataRecording[1],"4");}
                            }
                        }
                    }
                }).start();

                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void structureData(int i, String dataRecording, String c) {
        try {
            delay(100L);
            btClass.mTx(c);
            delay(100L);
            dataRecording = btClass.mRx();
            if (dataRecording != "") {
                if (hilo) {
                    showTextViewData(i, dataRecording);
                }
                btClass.mensajeReset();
            }
        } catch (NullPointerException ex) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CO2Status.setText(getResources().getString(R.string.str_qualitydefault));
                    CO2Status.setTextColor(Color.DKGRAY);
                    CO2Message.setText(getResources().getString(R.string.str_qualitydefault));
                    CO2Status.setTextColor(Color.DKGRAY);
                    PPMData.setText(getResources().getString(R.string.str_qualitydefault));
                    CO2Status.setTextColor(Color.DKGRAY);
                }
            });
        }
    }

    protected void showTextViewData(int i, String dataString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (i == 0) {
                    PPMData.setText("PPM: " + dataString + " ppm.");
                }
                if (i == 1) {
                    if (dataString.equals("GOOD")) {
                        CO2Status.setText(getResources().getString(R.string.str_estado) + " " +getResources().getString(R.string.str_airstatusexcellent));
                        CO2Status.setTextColor(Color.BLUE);
                        CO2Message.setText(getResources().getString(R.string.str_warning_good));
                    }
                    if (dataString.equals("NORMAL")){
                        CO2Status.setText(getResources().getString(R.string.str_estado) + " " + getResources().getString(R.string.str_airstatusgood));
                        CO2Status.setTextColor(Color.GREEN);
                        CO2Message.setText(getResources().getString(R.string.str_warning_normal));
                    }
                    if (dataString.equals("REGULAR")){
                        CO2Status.setText(getResources().getString(R.string.str_estado) + " " + getResources().getString(R.string.str_airstatushalf));
                        CO2Status.setTextColor(Color.rgb(255,165,0));
                        CO2Message.setText(getResources().getString(R.string.str_warning_regular));
                    }
                    if (dataString.equals("BAD")){
                        CO2Status.setText(getResources().getString(R.string.str_estado) + " " + getResources().getString(R.string.str_airstatusbad));
                        CO2Status.setTextColor(Color.RED);
                        CO2Message.setText(getResources().getString(R.string.str_warning_bad));
                    }
                }
            }
        });

    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (btClass != null) {
            hilo = false;
            initHilo = true;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        initHilo = false;
        hilo = false;
        super.onDestroy();
    }
}