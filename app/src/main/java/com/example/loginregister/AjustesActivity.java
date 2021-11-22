package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.loginregister.Prevalent.Prevalent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AjustesActivity extends AppCompatActivity {

    //Declaración de variables
    String name, email, id, dispositivo;
    TextView nombre, correo, id_admin, dispo;
    private FirebaseAuth mAuth;
    private Object view;
    DatabaseReference UsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        //inicializamos variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.ajustes);

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

                        startActivity(new Intent(getApplicationContext(),
                               AdminHome.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.videocall:
                        startActivity(new Intent(getApplicationContext(),
                                VideoCallActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ajustes:

                        return true;
                }

                return false;
            }
        });

    //Asignamos variables para TextView
        mAuth = FirebaseAuth.getInstance();
        nombre = findViewById(R.id.nom_info);
        id_admin = findViewById(R.id.id_info);
        correo = findViewById(R.id.email_info);


        //Método que muestra la información del usuario activo en los TextView declarados anteriormente
        userInfoDisplay(id_admin, correo, nombre, dispo);

    }
    //Método para obtener la información del usuario desde la base de datos
    private void userInfoDisplay(TextView id_admin, TextView correo, TextView nombre, TextView dispo) {
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(Prevalent.currentOnlineUser.getId());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    id = snapshot.child("id").getValue().toString();
                    email = snapshot.child("email").getValue().toString();
                    name = snapshot.child("name").getValue().toString();

                    nombre.setText(name);
                    correo.setText(email);
                    id_admin.setText(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //Método para cerrar sesión de la cuenta y al completarse nos dirige a la pantalla para ingresar
    public void LogOut(View view){
        if(mAuth.getCurrentUser() != null)
            mAuth.signOut();
        Intent i = new Intent(this, LoginRegisterActivity.class);
        startActivity(i);

    }

    public void irCalidad(View view){
        Intent i = new Intent(this, CalidadActivity.class);
        startActivity(i);

    }
}
