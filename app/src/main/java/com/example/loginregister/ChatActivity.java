package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.loginregister.Adaptadores.AdapterMensajes;
import com.example.loginregister.Firebase_.MensajeEnviar;
import com.example.loginregister.Firebase_.MensajeRecibir;
import com.example.loginregister.Model.Users;
import com.example.loginregister.Model.Users2;
import com.example.loginregister.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ChatActivity extends AppCompatActivity {
    private ImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;

    private AdapterMensajes adapter;
    private ImageButton btnEnviarFoto;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseAuth mUser;
    private FirebaseUser currentUser;

    FirebaseAuth mAuth;
    private String NOMBRE_USUARIO;
    private String NOMBRE_USUARIO2;

    private static  final int PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL =2;
    private String fotoPerfilCadena;

    private String msj_foto;
    private String msj_foto2;
    private String tipoUs;
    private String id;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fotoPerfil= (ImageView) findViewById(R.id.fotoPerfil);
        nombre = (TextView) findViewById(R.id.nombre);
        rvMensajes= (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje= (EditText) findViewById(R.id.txtMensaje);
        btnEnviar= (Button) findViewById(R.id.btnEnviar);
        btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);
        fotoPerfilCadena = "";

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference= database.getReference("chatV2");//sala de chat (nombre donde se guardan los msjs) version2.0
        storage= FirebaseStorage.getInstance();

        adapter =  new AdapterMensajes(this);
        LinearLayoutManager l= new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        currentUser = mAuth.getCurrentUser();
        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        tipoUs = preferences.getString("tipoUs", "");
        id = preferences.getString("id","");


        //inicializamos variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.chat);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.chat:
                        return true;

                    case R.id.home:
                        if (tipoUs.equals("Admin")) {
                            startActivity(new Intent(getApplicationContext(), AdminHome.class));
                            overridePendingTransition(0, 0);
                        }
                        if (tipoUs.equals("Users")) {
                            startActivity(new Intent(getApplicationContext(), UserHome.class));
                            overridePendingTransition(0, 0);
                        }
                        return true;

                    case R.id.videocall:
                        startActivity(new Intent(getApplicationContext(), VideoCallActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.ajustes:
                        if (tipoUs.equals("Admin")) {
                            startActivity(new Intent(getApplicationContext(),AjustesActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        if (tipoUs.equals("Users")) {
                            startActivity(new Intent(getApplicationContext(), UAjustesActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        return true;
                }

                return false;
            }
        });

        //Obtiene los datos de la pantalla anterior
        /*Bundle datos = this.getIntent().getExtras();
        String tipoUs = datos.getString("tipoUS");*/

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.push().setValue( new MensajeEnviar(txtMensaje.getText().toString(),nombre.getText().toString(),"","1", ServerValue.TIMESTAMP));
                txtMensaje.setText("");

            }

        });
        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "selecciona una foto"), PHOTO_SEND);
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "selecciona una foto"), PHOTO_PERFIL);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,  String s) {
                MensajeRecibir m = dataSnapshot.getValue(MensajeRecibir.class);
                adapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getUserNameChat(currentUser,tipoUs,id);
    }
    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PHOTO_SEND && resultCode==RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());

            fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (!task.isSuccessful()){
                        Uri uri = task.getResult();
                        MensajeEnviar m= new MensajeEnviar(NOMBRE_USUARIO + R.id.msj_foto2,uri.toString(), nombre.getText().toString(), fotoPerfilCadena, "2", ServerValue.TIMESTAMP);
                        databaseReference.push().setValue(m);
                    }
                }
            });



        }
        else if (requestCode == PHOTO_PERFIL && resultCode == RESULT_OK){

            Uri u = data.getData();
            storageReference = storage.getReference("foto_perfil");
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());

            fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (!task.isSuccessful()){
                        Uri uri = task.getResult();
                        fotoPerfilCadena= uri.toString();
                        MensajeEnviar m= new MensajeEnviar(NOMBRE_USUARIO + R.id.msj_foto ,uri.toString(), nombre.getText().toString(), fotoPerfilCadena, "2", ServerValue.TIMESTAMP);
                        databaseReference.push().setValue(m);
                        Glide.with(ChatActivity.this).load(uri.toString()).into(fotoPerfil);
                    }
                }
            });



        }
    }

    private void getUserNameChat(FirebaseUser currentUser, String tipoUs, String id) {
        if(currentUser!= null){
            if (tipoUs.equals("Admin")){
                userReference = database.getReference().child(tipoUs).child(Prevalent.currentOnlineUser.getId());
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Users user = dataSnapshot.getValue(Users.class);
                            NOMBRE_USUARIO = user.getName();
                            nombre.setText(NOMBRE_USUARIO);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else if (tipoUs.equals("Users")){
                userReference = database.getReference().child(tipoUs).child(Prevalent.currentOnlineUser2.getId());
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Users2 user2 = dataSnapshot.getValue(Users2.class);
                            NOMBRE_USUARIO = user2.getName();
                            nombre.setText(NOMBRE_USUARIO);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        tipoUs = preferences.getString("tipoUs", "");
        id = preferences.getString("id","");
        getUserNameChat(currentUser,tipoUs,id);
    }
}