package com.example.loginregister.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.loginregister.Adaptadores.AdapterDataRQ;
import com.example.loginregister.AdminHome;
import com.example.loginregister.Model.DeviceInformation;
import com.example.loginregister.Model.Users;
import com.example.loginregister.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class RQActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button buttonBack;
    Switch userSwitch;
    Switch dispSwitch;
    Switch dateSwitch;
    Switch modeSwitch;
    Spinner etUser;
    Spinner etDisp;
    Spinner etMode;
    EditText etDate;

    DatabaseReference deviceDatabase;
    ArrayList<DeviceInformation> dInfoList;
    ArrayAdapter<String> modeAdapter, userAdapter, deviceAdapter;
    AdapterDataRQ tableAdapter;
    DatePickerDialog pickerDialog;
    RecyclerView rview;

    int day, month, year;
    String username, device, date;
    String mode;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rqactivity);

        deviceDatabase = FirebaseDatabase.getInstance().getReference();
        rview = findViewById(R.id.tableRecycler);
        rview.setLayoutManager(new LinearLayoutManager(this));
        dInfoList = new ArrayList<DeviceInformation>();

        buttonBack = findViewById(R.id.backButton);
        userSwitch = findViewById(R.id.user_switch);
        dispSwitch = findViewById(R.id.disp_switch);
        dateSwitch = findViewById(R.id.date_switch);
        modeSwitch = findViewById(R.id.mode_switch);

        etUser = findViewById(R.id.user_spinner);
        etDisp = findViewById(R.id.disp_spinner);
        etDate = findViewById(R.id.date_dt);
        etMode = findViewById(R.id.mode_spinner);

        etMode.setEnabled(false);
        etDate.setEnabled(false);
        etDate.setFocusable(false);
        etDisp.setEnabled(false);
        etUser.setEnabled(false);

        modeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getDeviceModes());
        etMode.setAdapter(modeAdapter);
        etMode.setSelection(0);

        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getAdmin());
        etUser.setAdapter(userAdapter);
        etUser.setSelection(0);

        deviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getDevice());
        etDisp.setAdapter(deviceAdapter);
        etDisp.setSelection(0);

        tableAdapter = new AdapterDataRQ(R.layout.rqactivity_dataview,dInfoList);
        rview.setAdapter(tableAdapter);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        AdminHome.class));
                overridePendingTransition(0,0);
            }
        });

        userSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onClick(userSwitch);
            }
        });

        dispSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onClick(dispSwitch);
            }
        });

        dateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onClick(dateSwitch);
            }
        });

        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onClick(modeSwitch);
            }
        });

        etUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                username = etUser.getSelectedItem().toString();
                if (!username.equals(getResources().getString(R.string.str_spinner_selectuser))) {
                    dInfoList.clear();

                    ObtainData("Device (Dynamic)", username, dInfoList);
                    ObtainData("Device (Static)", username, dInfoList);

                    //tableAdapter.setArrayInfo(dInfoList);
                }
            } public void onNothingSelected(AdapterView<?> parent) {}
        });

        etDisp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        device = etDisp.getSelectedItem().toString();
                        if (!device.equals(getResources().getString(R.string.str_spinner_selectdevice))) {
                            dInfoList.clear();

                            ObtainData("Device (Dynamic)",device,dInfoList);
                            ObtainData("Device (Static)",device,dInfoList);

                            //tableAdapter.setArrayInfo(dInfoList);
                        }
                    }
                });
            } public void onNothingSelected(AdapterView<?> parent) {}
        });

        etMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mode = etMode.getSelectedItem().toString();
                        if (!mode.equals(getResources().getString(R.string.str_spinner_selectmode))) {
                            dInfoList.clear();

                            if (mode.equals(getResources().getString(R.string.str_devicemode_dynamic))) {
                                ObtainData("Device (Dynamic)", "Device (Dynamic)",dInfoList);
                            }
                            if (mode.equals(getResources().getString(R.string.str_devicemode_static))) {
                                ObtainData("Device (Static)", "Device (Static)",dInfoList);
                            }

                            //tableAdapter.setArrayInfo(dInfoList);
                        }
                    }
                });
            } public void onNothingSelected(AdapterView<?> parent) {}
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etDate.getText().equals(null) || etDate.getText().equals("")) {
                    calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    pickerDialog = new DatePickerDialog(RQActivity.this, RQActivity.this, year, month, day);
                    pickerDialog.show();
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                date = dayOfMonth + "/" + (month+1) + "/" +  year;
                etDate.setText(date);

                dInfoList.clear();

                ObtainData("Device (Dynamic)", date, dInfoList);
                ObtainData("Device (Static)", date, dInfoList);

            }
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

    private final ArrayList<String> getDevice() {
        ArrayList<String> deviceList = new ArrayList<>();
        deviceList.add(getResources().getString(R.string.str_spinner_selectdevice));
        deviceList.add("DINA001");
        return deviceList;
    }
    private final ArrayList<String> getAdmin() {
        ArrayList<String> adminList = new ArrayList<>();
        adminList.add(getResources().getString(R.string.str_spinner_selectuser));
        deviceDatabase.child("Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot adminSnapshot : snapshot.getChildren()){
                    Users users = adminSnapshot.getValue(Users.class);
                    adminList.add(users.getName());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        return adminList;
    }

    private final ArrayList<String> getDeviceModes() {
        ArrayList<String> deviceModes = new ArrayList<>();
        deviceModes.add(getResources().getString(R.string.str_spinner_selectmode));
        deviceModes.add(getResources().getString(R.string.str_devicemode_static));
        deviceModes.add(getResources().getString(R.string.str_devicemode_dynamic));
        return deviceModes;
    }

    private void ObtainData(String dbParent, String value, ArrayList<DeviceInformation> arrayInfo) {
        deviceDatabase.child(dbParent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        DeviceInformation dInfo = userSnapshot.getValue(DeviceInformation.class);
                        String stringSnapshot = userSnapshot.child("username").toString();
                        String deviceSnapshot = userSnapshot.child("name").toString();
                        String dateSnapshot = userSnapshot.child("datetime").toString();
                        String modeSnapshot = snapshot.getKey();

                                if(userSwitch.isChecked()){
                                    if (stringSnapshot.contains(value)) { arrayInfo.add(dInfo);
                                        Log.e("Username", stringSnapshot + " " + value + " mode: " + " " + modeSnapshot);
                                    }
                                }
                                if(dispSwitch.isChecked()){
                                    if (deviceSnapshot.contains(value)) { arrayInfo.add(dInfo);
                                        Log.e("Device", deviceSnapshot + " " + value + " mode: " + " " + modeSnapshot);
                                    }
                                }
                                if(dateSwitch.isChecked()){
                                    if (dateSnapshot.contains(value)) { arrayInfo.add(dInfo);
                                        Log.e("Date", dateSnapshot + " " + value + " mode: " + " " + modeSnapshot);
                                    }
                                }
                                if(modeSwitch.isChecked()){
                                    if (modeSnapshot.contains(value)) { arrayInfo.add(dInfo);
                                        Log.e("Mode", modeSnapshot + " " + value + " mode: " + " " + modeSnapshot);
                                    }
                                }
                    }
                    tableAdapter.setArrayInfo(arrayInfo);
                    tableAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void onClick(Switch view){
        if (view.getId() == R.id.user_switch) {
            if(userSwitch.isChecked()){
                etUser.setEnabled(true);
                disableOtherFilters(etDisp,dispSwitch);
                disableOtherFilters2(etDate,dateSwitch);
                disableOtherFilters(etMode,modeSwitch);
            }
            else { etUser.setEnabled(false); }
        }
        if (view.getId() == R.id.disp_switch) {
            if(dispSwitch.isChecked()){
                etDisp.setEnabled(true);
                disableOtherFilters(etUser,userSwitch);
                disableOtherFilters2(etDate,dateSwitch);
                disableOtherFilters(etMode,modeSwitch);}
            else { etDisp.setEnabled(false); }
        }
        if (view.getId() == R.id.date_switch) {
            if(dateSwitch.isChecked()){
                etDate.setEnabled(true);
                disableOtherFilters(etUser,userSwitch);
                disableOtherFilters(etDisp,dispSwitch);
                disableOtherFilters(etMode,modeSwitch);}
            else { etDate.setEnabled(false); }
        }
        if (view.getId() == R.id.mode_switch) {
            if(modeSwitch.isChecked()){
                etMode.setEnabled(true);
                disableOtherFilters(etUser,userSwitch);
                disableOtherFilters(etDisp,dispSwitch);
                disableOtherFilters2(etDate,dateSwitch);}
            else { etMode.setEnabled(false); }
        }
    }

    public void disableOtherFilters(Spinner sp, Switch sw) {
        sp.setSelection(0);
        sp.setEnabled(false);
        sw.setChecked(false);
    }

    public void disableOtherFilters2(EditText et, Switch sw) {
        et.setText("");
        et.setEnabled(false);
        sw.setChecked(false);
    }

}