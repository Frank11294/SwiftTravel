package com.example.frankernesto.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViajesActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;


    private Button buttonLogout, crearViaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajes);
        textViewUserEmail = (TextView)findViewById(R.id.textViewUserEmail);
        firebaseAuth =  FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail.setText("Bienvenido "+user.getEmail());


        buttonLogout = (Button)findViewById(R.id.Exit);
        crearViaje = (Button)findViewById(R.id.CREAR_VIAJE);

        buttonLogout.setOnClickListener(this);
        crearViaje.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        if(view == crearViaje){
            startActivity(new Intent(this,CrearViajeActivity.class));
        }


    }
}
