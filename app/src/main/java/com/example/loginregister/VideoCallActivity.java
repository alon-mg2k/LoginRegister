package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class VideoCallActivity extends AppCompatActivity {
    EditText secretcodeBox;
    Button joinBtn, shareBtn;
    FirebaseAuth mAuth;

    FirebaseUser currentUser;
    SharedPreferences preferences;
    String tipoUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        tipoUs = preferences.getString("tipoUs", "");

        //inicializamos variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.videocall);

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
                        if (tipoUs == "Admin") {
                            startActivity(new Intent(getApplicationContext(), AdminHome.class));
                            overridePendingTransition(0, 0);
                        }
                        if (tipoUs == "Users") {
                            startActivity(new Intent(getApplicationContext(), UserHome.class));
                            overridePendingTransition(0, 0);
                        }
                        return true;
                    case R.id.videocall:

                        return true;
                    case R.id.ajustes:
                        if (tipoUs == "Admin") {
                            startActivity(new Intent(getApplicationContext(),AjustesActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        if (tipoUs == "Users") {
                            startActivity(new Intent(getApplicationContext(), UAjustesActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        return true;
                }

                return false;
            }
        });

        secretcodeBox = findViewById(R.id.codeBox_);
        joinBtn= findViewById(R.id.joinBtn);
        shareBtn= findViewById(R.id.shareBtn);

        URL serverURL;
        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions =
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JitsiMeetConferenceOptions options= new JitsiMeetConferenceOptions.Builder()
                        .setRoom(secretcodeBox.getText().toString())
                        .setWelcomePageEnabled(false)
                        .build();

                JitsiMeetActivity.launch(VideoCallActivity.this, options);

            }
        });


    }

    private class val {
    }
}
