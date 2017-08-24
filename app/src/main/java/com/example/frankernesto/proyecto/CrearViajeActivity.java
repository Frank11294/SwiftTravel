package com.example.frankernesto.proyecto;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrearViajeActivity extends AppCompatActivity {
  private final String TAG = "Mi App";
  private Intent intent;

  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mToogle;
  private Toolbar mToolBar;

    private FirebaseAuth firebaseAuth;
    private TextView emailUsuario,nomUsuario;
    private NavigationView navigationView;
    private PlaceAutocompleteFragment autocompleteFragment;



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToogle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.normal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToogle.onOptionsItemSelected(item))
            return true;

        int id=item.getItemId();

        if(id==R.id.about_us){
            Toast.makeText(getApplicationContext(),"Swift Travel\nVersion: 1.0\nAutores: Frank Roman Izaguirre\nRuben Argumosa",Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje);

       //Aqui empieza el rollo este de firebase

        firebaseAuth =  FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();


        //Aqui termina

        //Esto es lo del autocomplete de google
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        //Aqui termina


        //Esto es lo del toolbar personalizado ese q cree



        mToolBar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolBar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else{
            Toast.makeText(getApplicationContext(),"Action bar es null",Toast.LENGTH_SHORT).show();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mToogle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToogle);


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header_View = navigationView.getHeaderView(0);

        emailUsuario = (TextView)header_View.findViewById(R.id.email_header);
        nomUsuario = (TextView)header_View.findViewById(R.id.nom_header);

        emailUsuario.setText(user.getEmail());

        //Esto es para despues cuando tengo hecha la base de datos

//        Intent intento=getIntent();
//        String nomU=intento.getStringExtra("Nombre");
//        nomUsuario.setText(nomU);



       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id=item.getItemId();
             switch (id){
               case R.id.nav_salir:
                   firebaseAuth.signOut();
                   finish();
                   startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                   break;
               case R.id.nav_fav:
                   Toast.makeText(getApplicationContext(),"Favoritos",Toast.LENGTH_SHORT).show();
                   break;

               case R.id.nav_reserv:
                   Toast.makeText(getApplicationContext(),"Reservas",Toast.LENGTH_SHORT).show();
                   break;
               case R.id.nav_opc:
                   Toast.makeText(getApplicationContext(),"Opciones",Toast.LENGTH_SHORT).show();
                   break;
               case R.id.nav_inicio:
                   Toast.makeText(getApplicationContext(),"Inicio",Toast.LENGTH_SHORT).show();
                   break;
               case R.id.nav_listaD:
                   Toast.makeText(getApplicationContext(),"Lista de compra",Toast.LENGTH_SHORT).show();
                   break;
           }
                return false;
            }
        });


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.i(TAG, "Place: " + place.getName());

                String id = place.getId();
                String nombre = place.getName().toString();


                intent = new Intent(getApplicationContext(),CrearViajeActivity2.class);
                intent.putExtra("PlaceID",id);
                intent.putExtra("NOMBRE",nombre);



                startActivity(intent);

            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        autocompleteFragment.setText("");
    }
}
