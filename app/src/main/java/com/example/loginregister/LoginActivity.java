package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregister.Model.Users;
import com.example.loginregister.Model.Users2;
import com.example.loginregister.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    //Declaración de variables
    private EditText InputEmail, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private Users usuario;
    private Users2 usuario2;
    private static FirebaseAuth mAuth;
    private FirebaseAuth mUser;
    private DatabaseReference mDatabase;
    private String parentDbName = "Admin";
    SharedPreferences sharedPreferences;

    private TextView AdminLink, UserLink, TipoUsuarioUser, TipoUsuarioAdmin, IrRegistrar, IrRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Variables Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Variables de los elementos de la pantalla
        LoginButton = findViewById(R.id.btn_iniciarsesion);
        InputPassword = findViewById(R.id.txt_login_pass);
        InputEmail = findViewById(R.id.txt_email_login);
        loadingBar = new ProgressDialog(this);

        AdminLink = findViewById(R.id.admin_panel_link);
        UserLink = findViewById(R.id.user_panel_link);
        TipoUsuarioUser = findViewById(R.id.tipo_usuario_user);
        TipoUsuarioAdmin = findViewById(R.id.tipo_usuario_admin);

        //IrRegistrar = findViewById(R.id.go_resgister);
        //IrRecuperar = findViewById(R.id.forget_password_link);




        //Al presionar sobre el botón se inicia el método LoginUser()
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

        //Al presionar sobre el link se asigna invisibilidad a los textos que hacen referencia a
        //iniciar sesión como usuario con dispositivo y se asigna el nombre del nodo de la base de datos
        //donde se buscará la información del usuario que ingresará con su cuenta
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminLink.setVisibility(View.INVISIBLE);
                TipoUsuarioAdmin.setVisibility(View.VISIBLE);
                UserLink.setVisibility(View.VISIBLE);
                TipoUsuarioUser.setVisibility(View.INVISIBLE);
                parentDbName = "Admin";

            }
        });

        //Al presionar sobre el link se asigna invisibilidad a los textos que hacen referencia a
        // iniciar sesión como usuario que solo monitorea
        // y se asigna el nombre del nodo de la base de datos
        //donde se buscará la información del usuario que ingresará con su cuenta


        UserLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminLink.setVisibility(View.VISIBLE);
                TipoUsuarioAdmin.setVisibility(View.INVISIBLE);
                UserLink.setVisibility(View.INVISIBLE);
                TipoUsuarioUser.setVisibility(View.VISIBLE);
                parentDbName = "Users";

            }
        });

        //Al presionar el textView de registrarse se inicia la pantalla de registrar cuenta
       /* IrRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, LoginRegisterActivity.class);
                startActivity(i);
            }
        });*/

        //Al presionar el textView de olvidaste contraseña se inicia la pantalla de olvidar contraseña
       /* IrRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, UserActivity.class);
                startActivity(i);
            }
        });*/


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mUser.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }


    //Método para iniciar sesión
    private void LoginUser() {
        //Obtener los datos que escribio el usuario
        final String email = InputEmail.getText().toString().trim();
        final String password = InputPassword.getText().toString();

        String msj2 = getResources().getString(R.string.msj_espera2);
        //Condiciones para ver si no dejo campos vacios
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,R.string.campo_email, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,R.string.campo_contrasena, Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle(R.string.cargar);
            loadingBar.setMessage(msj2);
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();



            //método para iniciar sesión en firebase authentication
            mUser.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        // Sign in success, update UI with the signed-in user's information
                        Log.d("msj", "signInWithEmail:success");
                        FirebaseUser user = mUser.getCurrentUser();

                        mDatabase.child(parentDbName).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for(final DataSnapshot snapshot1:snapshot.getChildren())
                                {
                                    mDatabase.child(parentDbName).child(snapshot1.getKey()).addValueEventListener(new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot)
                                        {
                                            usuario = snapshot1.getValue(Users.class);
                                            String correo = usuario.getEmail();
                                            String password1 = usuario.getPassword();
                                            //String tipousuario = usuario.getTipoUs();

                                            usuario2 = snapshot1.getValue(Users2.class);

                                            sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("tipoUs", parentDbName);
                                            editor.apply();

                                            if(user.isEmailVerified()){

                                                if(email.equals(correo) && password.equals(password1))
                                                {
                                                    if(parentDbName.equals("Admin")){
                                                        loadingBar.dismiss();

                                                        editor = sharedPreferences.edit();
                                                        editor.putString("id", usuario.getId());
                                                        editor.apply();

                                                        Prevalent.currentOnlineUser = usuario;
                                                        Intent intent = new Intent(LoginActivity.this, AdminHome.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else if(parentDbName.equals("Users")){

                                                        editor = sharedPreferences.edit();
                                                        editor.putString("id", usuario2.getId());
                                                        editor.apply();

                                                        loadingBar.dismiss();
                                                        Intent intent = new Intent(LoginActivity.this, UserHome.class);
                                                        Prevalent.currentOnlineUser2 = usuario2;
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                } else {
                                                    loadingBar.dismiss();
                                                    //Toast.makeText(LoginActivity.this, R.string.msj_error_login, Toast.LENGTH_SHORT).show();
                                                }


                                            }else{
                                                Toast.makeText(LoginActivity.this, R.string.msj_verificar, Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = getIntent();
                                                finish();
                                                startActivity(intent);
                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) { }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });

                    }else{
                        // If sign in fails, display a message to the user.
                        loadingBar.dismiss();
                        Log.w("msj", "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    private void reload() { }


}