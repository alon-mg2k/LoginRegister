package com.example.loginregister.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.loginregister.Adaptadores.AdapterDataRQU;
import com.example.loginregister.Model.DeviceInformation;
import com.example.loginregister.R;
import com.example.loginregister.UserHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class RSUActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ArrayList<DeviceInformation> queryInfo;
    ArrayAdapter<String> deviceAdapter;
    AdapterDataRQU tableAdapter;
    Button backButton;
    Switch dispSwitch;
    Switch dateSwitch;
    Spinner dispSpinner;
    EditText editText;
    DatabaseReference staticDatabase;
    DatePickerDialog pickerDialog;
    Calendar calendar;
    RecyclerView rview;

    int year, month, day;
    String device, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsuactivity);

        rview = findViewById(R.id.tableRecycler);
        rview.setLayoutManager(new LinearLayoutManager(this));
        queryInfo = new ArrayList<DeviceInformation>();

        backButton = findViewById(R.id.BackButton);
        staticDatabase = FirebaseDatabase.getInstance().getReference();

        dispSwitch = findViewById(R.id.disp_switch);
        dateSwitch = findViewById(R.id.date_switch);
        dispSpinner = findViewById(R.id.disp_spinner);
        editText = findViewById(R.id.date_dt);

        editText.setEnabled(false);
        editText.setFocusable(false);
        dispSpinner.setEnabled(false);

        deviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getDevice());
        dispSpinner.setAdapter(deviceAdapter);
        dispSpinner.setSelection(0);

        tableAdapter = new AdapterDataRQU(queryInfo, R.layout.rquactivity_dataview);
        rview.setAdapter(tableAdapter);

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

        dispSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        device = dispSpinner.getSelectedItem().toString();
                        if (!device.equals(getResources().getString(R.string.str_spinner_selectdevice))) {
                            queryInfo.clear();
                            ObtainData("Device (Static)",device,queryInfo);
                        }
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                pickerDialog = new DatePickerDialog(RSUActivity.this, RSUActivity.this, year, month, day);
                pickerDialog.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        UserHome.class));
                overridePendingTransition(0,0);
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                date = dayOfMonth + "/" + (month+1) + "/" +  year;
                editText.setText(date);

                queryInfo.clear();

                ObtainData("Device (Static)", date, queryInfo);
            }
        });
    }

    private void ObtainData(String dbParent, String value, ArrayList<DeviceInformation> arrayInfo) {
        staticDatabase.child(dbParent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        DeviceInformation dInfo = userSnapshot.getValue(DeviceInformation.class);
                        String deviceSnapshot = userSnapshot.child("name").toString();
                        String dateSnapshot = userSnapshot.child("datetime").toString();

                        if(dispSwitch.isChecked()){
                            if (deviceSnapshot.contains(value)) { arrayInfo.add(dInfo);
                                Log.e("Device", deviceSnapshot + " " + value);
                            }
                        }
                        if(dateSwitch.isChecked()){
                            if (dateSnapshot.contains(value)) { arrayInfo.add(dInfo);
                                Log.e("Date", dateSnapshot + " " + value);
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
        if (view.getId() == R.id.disp_switch) {
            if(dispSwitch.isChecked()) {
                dispSpinner.setEnabled(true);
                disableOtherFilters2(editText,dateSwitch);
            }
            else { dispSpinner.setEnabled(false); }
        }
        if (view.getId() == R.id.date_switch) {
            if(dateSwitch.isChecked()) {
                editText.setEnabled(true);
                disableOtherFilters(dispSpinner,dispSwitch);
            }
            else { editText.setEnabled(false); }
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

    private final ArrayList<String> getDevice() {
        ArrayList<String> deviceList = new ArrayList<>();
        deviceList.add(getResources().getString(R.string.str_spinner_selectdevice));
        deviceList.add("DINA001");
        return deviceList;
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
}