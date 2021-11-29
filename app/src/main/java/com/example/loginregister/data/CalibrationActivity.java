package com.example.loginregister.data;

import static bolts.Task.delay;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.loginregister.AdminHome;
import com.example.loginregister.BluetoothClass;
import com.example.loginregister.ChatActivity;
import com.example.loginregister.PairingActivity;
import com.example.loginregister.R;

public class CalibrationActivity extends AppCompatActivity {
    final Handler handler = new Handler();
    ScrollView scrollView;
    Button pairButton;
    Button recieveButton;
    Button stopButton;
    TextView CO2status;
    TextView DistanceRun;
    TextView RecordedSteps;
    TextView console;
    Boolean initHilo = false;
    Boolean hilo = true;
    String[] dataRecording;
    int stepCounter;
    static BluetoothClass btClass = AdminHome.btClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        scrollView = findViewById(R.id.consoleScrollView);
        pairButton = findViewById(R.id.btn_conectar);
        recieveButton = findViewById(R.id.btn_recieve);
        stopButton = findViewById(R.id.btn_stopb);

        DistanceRun = findViewById(R.id.tv_run);
        CO2status = findViewById(R.id.tv_co2);
        RecordedSteps = findViewById(R.id.tv_steps);
        console = findViewById(R.id.tv_log);

        stopButton.setEnabled(false);

        pairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btClass.exitErrorOk(true);
                btClass.mensajeConexion(getResources().getString(R.string.txt_connectionsuccess));
                btClass.mensajeConexion(getResources().getString(R.string.txt_connectionfailed));
                initHilo = btClass.conectaBluetooth();
                pairButton.setEnabled(false);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hilo = false;
                initHilo = false;
                delay(500L);
                btClass.mTx(";");
                delay(500L);
                btClass.exitConexion();
            }
        });

        recieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recieveButton.setEnabled(false);
                stopButton.setEnabled(true);
                while (!initHilo) {
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (hilo) {
                            dataRecording = new String[10];
                            for (int i = 0; i < 10; i++) {
                                if (i == 0) { structureData(0,dataRecording[0],"1");}
                                if (i == 1) { structureData(1,dataRecording[1],"2");}
                                if (i == 2) { structureData(2,dataRecording[2],"3");}
                                if (i == 3) { structureData(3,dataRecording[3],"4");}
                                if (i == 4) { structureData(4,dataRecording[4],"5");}
                                if (i == 5) { structureData(5,dataRecording[5],"6");}
                                if (i == 6) { structureData(6,dataRecording[6],"7");}
                                if (i == 7) { structureData(7,dataRecording[7],"8");}
                                if (i == 8) { structureData(8,dataRecording[8],"9");}
                                if (i == 9) { structureData(9,dataRecording[9],":");}
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

    @Override
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
        btClass.exitConexion();
    }


    protected void structureData(int i, String dataRecording, String c) {
        try {
                delay(10L);
                btClass.mTx(c);
                delay(10L);
                dataRecording = btClass.mRx();
                if (dataRecording != "") {
                    if (hilo) {
                        showConsoleViewData(i, dataRecording);
                        showTextViewData(i, dataRecording);
                    }
                    btClass.mensajeReset();
            }
        } catch (NullPointerException ex) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    console.setText("");
                    recieveButton.setEnabled(true);
                    stopButton.setEnabled(false);
                }
            });
        }
    }

    protected void showConsoleViewData(int i ,String stringView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (i == 0) {
                    console.append("\n" + getResources().getString(R.string.str_device) + stringView + "\n");
                }
                if (i == 1) {
                    console.append(getResources().getString(R.string.str_datetime) + stringView + "\n");
                }
                if (i == 2) {
                    console.append(getResources().getString(R.string.str_co2) + " " + stringView + " ppm.\n");
                }
                if (i == 3) {
                    console.append(getResources().getString(R.string.str_estado) + " " + stringView + "\n");
                }
                if (i == 4) {
                    console.append(getResources().getString(R.string.str_dist) + " " + stringView + "cm.\n");
                }
                if (i == 5) {
                    console.append(getResources().getString(R.string.str_personcounter) + " " + stringView + " " + getResources().getString(R.string.str_people) + "\n");
                }
                if (i == 6) {
                    console.append(getResources().getString(R.string.str_accx) + " " + stringView + "°\n");
                }
                if (i == 7) {
                    console.append(getResources().getString(R.string.str_accy) + " " + stringView + "°\n");
                }
                if (i == 8) {
                    console.append(getResources().getString(R.string.str_accz) + " " + stringView + "°\n");
                }
                if (i == 9) {
                    console.append(getResources().getString(R.string.str_stepcounter) + " " + stringView + "." + "\n");
                }
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    protected void showTextViewData(int i, String dataString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (i == 3) {
                    if (dataString.equals("GOOD")){
                        CO2status.setText(getResources().getString(R.string.str_airstatusexcellent));
                        CO2status.setTextColor(Color.BLUE);}
                    if (dataString.equals("NORMAL")){
                        CO2status.setText(dataString + " " + getResources().getString(R.string.str_airstatusgood));}
                    CO2status.setTextColor(Color.GREEN);
                    if (dataString.equals("REGULAR")){
                        CO2status.setText(dataString + " " + getResources().getString(R.string.str_airstatushalf));
                        CO2status.setTextColor(Color.rgb(255,165,0));}
                    if (dataString.equals("BAD")){
                        CO2status.setText(dataString + " " + getResources().getString(R.string.str_airstatusbad));
                        CO2status.setTextColor(Color.RED);}
                }
                if (i == 4){ DistanceRun.setText(dataString + " cm. ");}
                if (i == 5){ RecordedSteps.setText(dataString + " " + getResources().getString(R.string.str_people));}
            }
        });

    }

}
