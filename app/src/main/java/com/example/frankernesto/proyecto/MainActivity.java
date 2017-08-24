package com.example.frankernesto.proyecto;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button registrar;
    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText nombreUsuario;
    private TextView textViewSignin;
    private ProgressDialog barraDialogo;
    private FirebaseAuth firebaseAuth;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null ){
            finish();
            startActivity(new Intent(getApplicationContext(),Tabbed_Main_Activity.class));
        }

        registrar=(Button)findViewById(R.id.registrar);
        editTextMail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        textViewSignin=(TextView)findViewById(R.id.TextViewRegistro);
        nombreUsuario=(EditText)findViewById(R.id.editTextNom);

        registrar.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        barraDialogo=new ProgressDialog(this);





    }

    private void registrarUsuario(){
        String email = editTextMail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
         name=nombreUsuario.getText().toString().trim();

        if(TextUtils.isEmpty(name)){

            Toast.makeText(this,"Por favor introduzca su nombre de usuario",Toast.LENGTH_SHORT).show();

            return;
        }

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
                             Intent intent=new Intent(getApplicationContext(),Tabbed_Main_Activity.class);
                             intent.putExtra("Nombre",name);
                             barraDialogo.dismiss();
                             finish();
                             startActivity(intent);
                         }else{
                             try{
                                 throw task.getException();
                             }catch(FirebaseAuthWeakPasswordException e){
                                 editTextPassword.setError(getString(R.string.ERROR_CONTRASENA_DEBIL));
                                 editTextPassword.requestFocus();
                             }catch(FirebaseAuthInvalidCredentialsException e){
                                 editTextMail.setError(getString(R.string.ERROR_EMAIL_INVALIDO));
                                 editTextMail.requestFocus();
                             }catch(FirebaseAuthUserCollisionException e) {
                                 editTextMail.setError(getString(R.string.ERROR_USUARIO_EXISTE));
                                 editTextPassword.requestFocus();
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