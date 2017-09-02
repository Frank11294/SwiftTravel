package com.example.frankernesto.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


//import com.google.firebase.messaging.

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button registrar;
    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText editTextUser;
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
            startActivity(new Intent(getApplicationContext(),Tabbed_Main_Activity.class));

        }

        registrar=(Button)findViewById(R.id.registrar);
        editTextMail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        textViewSignin=(TextView)findViewById(R.id.TextViewRegistro);
        editTextUser =(EditText)findViewById(R.id.editTextNom);

        registrar.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        barraDialogo=new ProgressDialog(this);





    }

    private void registrarUsuario(){

        String email = editTextMail.getText().toString().trim();
        String contraseña=editTextPassword.getText().toString().trim();
        final String name = editTextUser.getText().toString();

        if(TextUtils.isEmpty(name)){

            Toast.makeText(this,"Por favor introduzca su nombre de usuario",Toast.LENGTH_SHORT).show();

            return;
        }

        if(TextUtils.isEmpty(email)){

            Toast.makeText(this,"Por favor introduzca su correo",Toast.LENGTH_SHORT).show();

            return;
        }

        if(TextUtils.isEmpty(contraseña)){

            Toast.makeText(this,"Por favor introduzca la contraseña",Toast.LENGTH_SHORT).show();

            return;
        }

        barraDialogo.setMessage("Registrando Usuario...");
        barraDialogo.show();


        firebaseAuth.createUserWithEmailAndPassword(email,contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        barraDialogo.dismiss();
                         if(task.isSuccessful()){

                             FirebaseUser user = firebaseAuth.getCurrentUser();

                             UserProfileChangeRequest actualizarPerfil = new UserProfileChangeRequest.Builder()
                                     .setDisplayName(name).build();

                             user.updateProfile(actualizarPerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     Toast.makeText(getApplicationContext(),"Nombre Actualizado",Toast.LENGTH_SHORT).show();
                                 }
                             });

                             Toast.makeText(MainActivity.this,"Usuario registrado",Toast.LENGTH_SHORT).show();
                             Intent intent=new Intent(getApplicationContext(),Tabbed_Main_Activity.class);

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
                                 Log.d("ERROR-> ",e.getMessage());
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