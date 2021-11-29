package com.example.loginregister.data;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static bolts.Task.delay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.example.loginregister.Adaptadores.AdapterDataRS;
import com.example.loginregister.LoginActivity;
import com.example.loginregister.Model.DeviceInformation;

import com.example.loginregister.AdminHome;
import com.example.loginregister.BluetoothClass;
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

import org.w3c.dom.Text;

public class RSActivity extends AppCompatActivity {
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
    AdapterDataRS adapter;
    RecyclerView view;
    ArrayList<DeviceInformation> dInfoList = new ArrayList<>();

    DeviceInformation dInfo;
    Map<Object,Object> dbRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsactivity);

        pairButton = findViewById(R.id.btn_conectar);
        recieveButton = findViewById(R.id.btn_recieve);
        buttonBack = findViewById(R.id.btn_stopb);

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
                                deviceDatabase.child("Device (Static)").push().setValue(dbRecord);
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


    protected void structureData(int i,Map dbMap, String dataRecording, String c) {
        try {
            delay(100L);
            btClass.mTx(c);
            delay(100L);
            dataRecording = btClass.mRx();
            if (dataRecording != "") {
                if (hilo) {
                    if (i == 0) { dbMap.put("name", dataRecording); }
                    if (i == 1) { dbMap.put("datetime", dataRecording); }
                    if (i == 2) { dbMap.put("co2", dataRecording); }
                    if (i == 3) { dbMap.put("state", dataRecording); showTextViewData(i, dataRecording); }
                    if (i == 4) { dbMap.put("distance", dataRecording); showTextViewData(i, dataRecording);}
                    if (i == 5) { dbMap.put("personCounter", dataRecording); showTextViewData(i, dataRecording);}
                    if (i == 6) { dbMap.put("x", dataRecording); }
                    if (i == 7) { dbMap.put("y", dataRecording); }
                    if (i == 8) { dbMap.put("z", dataRecording); }
                    if (i == 9) { dbMap.put("stepCounter", dataRecording); }
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

    private void requestDatabase() {
            deviceDatabase.child("Device (Static)").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dInfoList.clear();
                    for (final DataSnapshot deviceSnapshot : dataSnapshot.getChildren()) {
                        DeviceInformation dInfo = deviceSnapshot.getValue(DeviceInformation.class);
                        dInfoList.add(dInfo);
                    }
                    adapter = new AdapterDataRS(dInfoList,R.layout.rsactivity_dataview);
                    view.setAdapter(adapter);
                    view.scrollToPosition(dInfoList.size() - 1);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) { }
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

}