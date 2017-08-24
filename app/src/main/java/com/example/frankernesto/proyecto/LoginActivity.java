package com.example.frankernesto.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataApi.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private Button entrar;
    private EditText editTextMail;
    private EditText editTextPassword;
    private TextView registrarse;
    private ProgressDialog barraDialogo;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null ){
            finish();
            startActivity(new Intent(getApplicationContext(),CrearViajeActivity.class));
        }

        entrar=(Button)findViewById(R.id.Entrar);
        editTextMail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        registrarse=(TextView)findViewById(R.id.TextViewEntrar);


        entrar.setOnClickListener(this);
        registrarse.setOnClickListener(this);
        barraDialogo=new ProgressDialog(this);
    }

    private void userLogin(){
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

        barraDialogo.setMessage("Cargando...");
        barraDialogo.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     barraDialogo.dismiss();
                        if(task.isSuccessful()){
                          finish();
                          startActivity(new Intent(getApplicationContext(),CrearViajeActivity.class));
                        }else{
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                editTextMail.setError(getString(R.string.ERROR_EMAIL_INVALIDO));
                                editTextMail.requestFocus();
                            }catch(Exception e){
                                Log.e("MiApp",e.getMessage());
                            }

                            barraDialogo.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == entrar)
        {
           userLogin();
        }
        if(view == registrarse)
        {
          finish();
          startActivity(new Intent(this,MainActivity.class));
        }
    }
}
