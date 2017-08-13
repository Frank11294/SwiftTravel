package com.example.frankernesto.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button registrar;
    private EditText editTextMail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog barraDialogo;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null ){
            finish();
            startActivity(new Intent(getApplicationContext(),ViajesActivity.class));
        }

        registrar=(Button)findViewById(R.id.registrar);
        editTextMail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        textViewSignin=(TextView)findViewById(R.id.TextViewRegistro);

        registrar.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        barraDialogo=new ProgressDialog(this);


    }

    private void registrarUsuario(){
        String email = editTextMail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){

            Toast.makeText(this,"Por favor introduzca su correo",Toast.LENGTH_SHORT).show();

            return;
        }

        if(TextUtils.isEmpty(password)){

            Toast.makeText(this,"Por favor introduzca la contraseña",Toast.LENGTH_SHORT).show();

            return;
        }

        barraDialogo.setMessage("Registrando Usuario...");
        barraDialogo.show();


        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        barraDialogo.dismiss();
                         if(task.isSuccessful()){
                             Toast.makeText(MainActivity.this,"Usuario registrado",Toast.LENGTH_SHORT).show();
                             barraDialogo.dismiss();
                             finish();
                             startActivity(new Intent(getApplicationContext(),ViajesActivity.class));
                         }else{
                             Toast.makeText(MainActivity.this,"Fallo en el registro..  intente de nuevo"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                             barraDialogo.dismiss();
                         }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == registrar)
        {
            registrarUsuario();
        }
        if(view == textViewSignin)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
