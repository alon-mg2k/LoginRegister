package com.example.loginregister.data;

import static bolts.Task.delay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginregister.Adaptadores.AdapterDataRD;
import com.example.loginregister.Adaptadores.AdapterDataRS;
import com.example.loginregister.AdminHome;
import com.example.loginregister.BluetoothClass;
import com.example.loginregister.Model.DeviceInformation;
import com.example.loginregister.Model.Users;
import com.example.loginregister.Prevalent.Prevalent;
import com.example.loginregister.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RDActivity extends AppCompatActivity {
    static BluetoothClass btClass = AdminHome.btClass;
    String userName;
    DatabaseReference deviceDatabase;
    DatabaseReference userDatabase;
    SharedPreferences sharedPreferences;

    Button buttonBack, pairButton, recieveButton;
    boolean initHilo = false, hilo = true;

    TextView CO2status;
    TextView DistanceRun;
    TextView RecordedSteps;
    AdapterDataRD adapter;
    RecyclerView view;
    ArrayList<DeviceInformation> dInfoList = new ArrayList<>();
    ArrayList<Float> CO2Array = new ArrayList<>();
    ArrayList<Integer> DistArray = new ArrayList<>();

    DeviceInformation dInfo;
    Map<Object,Object> dbRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdactivity);

        buttonBack = findViewById(R.id.btn_stopb);
        pairButton = findViewById(R.id.btn_conectar);
        recieveButton = findViewById(R.id.btn_recieve);

        view = findViewById(R.id.DataView);
        CO2status = findViewById(R.id.tv_co2);
        DistanceRun = findViewById(R.id.tv_run);
        RecordedSteps = findViewById(R.id.tv_steps);

        deviceDatabase = FirebaseDatabase.getInstance().getReference();
        userDatabase = FirebaseDatabase.getInstance().getReference();

        view.setLayoutManager(new LinearLayoutManager(this));

        buttonBack.setOnClickListener(new View.OnClickListener() {
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

        pairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btClass.exitErrorOk(true);
                btClass.mensajeConexion(getResources().getString(R.string.txt_connectionsuccess));
                btClass.mensajeConexion(getResources().getString(R.string.txt_connectionfailed));
                initHilo = btClass.conectaBluetooth();
                pairButton.setEnabled(false);
                buttonBack.setEnabled(true);
            }
        });

        recieveButton.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                recieveButton.setEnabled(false);
                buttonBack.setEnabled(true);
                while(!initHilo) {
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

                            dbRecord = new HashMap<>();
                            dInfo = new DeviceInformation();

                            for (int i = 0; i < 11; i++) {
                                if (i == 0) { structureData(0,dbRecord,dInfo.getName(),"1");}
                                if (i == 1) { structureData(1,dbRecord,dInfo.getDatetime(),"2");}
                                if (i == 2) { structureData(2,dbRecord,dInfo.getCo2(),"3");}
                                if (i == 3) { structureData(3,dbRecord,dInfo.getState(),"4");}
                                if (i == 4) { structureData(4,dbRecord,dInfo.getDistance(),"5");}
                                if (i == 5) { structureData(5,dbRecord,dInfo.getPersonCounter(),"6");}
                                if (i == 6) { structureData(6,dbRecord,dInfo.getX(),"7");}
                                if (i == 7) { structureData(7,dbRecord,dInfo.getY(),"8");}
                                if (i == 8) { structureData(8,dbRecord,dInfo.getZ(),"9");}
                                if (i == 9) { structureData(9,dbRecord,dInfo.getStepCounter(),":");}
                                if (i == 10) { getUserName(10,dbRecord); }
                            }
                            deviceDatabase.child("Device (Dynamic)").push().setValue(dbRecord);
                            requestDatabase();
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

    private void getUserName(int i, Map dbMap) {
        if (i == 10) {
            sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
            String id = sharedPreferences.getString("id", "");
            userDatabase.child("Admin").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userName = snapshot.child("name").getValue().toString();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            dbMap.put("username", userName);
        }
    }

    protected void structureData(int i,Map dbMap, String dataRecording, String c) {
        try {
            delay(100L);
            btClass.mTx(c);
            delay(100L);
            dataRecording = btClass.mRx();
            if (dataRecording != "") {
                if (hilo) {
                    float CO2Average = 0; int TotalDist = 0;
                    if (i == 0) { dbMap.put("name", dataRecording); }

                    if (i == 1) { dbMap.put("datetime", dataRecording); }

                    if (i == 2) {
                        CO2Average = getPromCO2(dataRecording,CO2Array);
                        dbMap.put("co2", Float.toString(CO2Average)); }

                    if (i == 3) {
                        String AQStat = getState(CO2Average);
                        dbMap.put("state", AQStat);
                        showTextViewData(i, AQStat); }

                    if (i == 4) {
                        TotalDist = getSumDist(dataRecording,DistArray);
                        dbMap.put("distance", Integer.toString(TotalDist));
                        showTextViewData(i, Integer.toString(TotalDist));}

                    if (i == 5) { dbMap.put("personCounter", dataRecording); }

                    if (i == 6) { dbMap.put("x", dataRecording); }

                    if (i == 7) { dbMap.put("y", dataRecording); }

                    if (i == 8) { dbMap.put("z", dataRecording); }

                    if (i == 9) { dbMap.put("stepCounter", dataRecording); showTextViewData(i, dataRecording);}

                }
                btClass.mensajeReset();
            }
        } catch (NullPointerException ex) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recieveButton.setEnabled(true);
                }
            });
        }
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
                if (i == 9){ RecordedSteps.setText(dataString + " " + getResources().getString(R.string.str_steps));}
            }
        });

    }

    private void requestDatabase() {
        deviceDatabase.child("Device (Dynamic)").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dInfoList.clear();
                    for (final DataSnapshot deviceSnapshot : dataSnapshot.getChildren()) {
                        DeviceInformation dInfo = deviceSnapshot.getValue(DeviceInformation.class);
                        dInfoList.add(dInfo);
                    }
                    adapter = new AdapterDataRD(dInfoList,R.layout.rsactivity_dataview,DistArray,CO2Array);
                    view.setAdapter(adapter);
                    view.scrollToPosition(dInfoList.size() - 1);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) { }
        });
    }
    public void onResume() {
        super.onResume();
    }
    public void onPause() {
        super.onPause();
    }
    public void onDestroy() {
        super.onDestroy();
    }

    private int getSumDist(String dataRecording, ArrayList<Integer> distArray) {
        int promDist = 0;
        distArray.add(Integer.parseInt(dataRecording));
        for (int i = 0; i < distArray.size() ; i++) { promDist += distArray.get(i); }
        return promDist;
    }

    private float getPromCO2(String dataRecording, ArrayList<Float> CO2Array) {
        float promCO2 = 0;
        CO2Array.add(Float.parseFloat(dataRecording));
        for (int i = 0; i < CO2Array.size(); i++) {
            promCO2 += CO2Array.get(i);
        }
        return promCO2 / CO2Array.size();
    }

    private String getState(float promCO2) {
        String promStatus = "";
        if (promCO2 <= 350 ) { promStatus = "GOOD"; }
        if (promCO2 > 351 && promCO2 <= 800 ) { promStatus = "NORMAL"; }
        if (promCO2 > 801 && promCO2 >= 1200 ) { promStatus = "REGULAR"; }
        if (promCO2 > 1200 ) { promStatus = "BAD"; }
        return promStatus;
    }
}