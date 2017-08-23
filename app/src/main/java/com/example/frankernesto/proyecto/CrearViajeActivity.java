package com.example.frankernesto.proyecto;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class CrearViajeActivity extends AppCompatActivity {
  private final String TAG = "Mi App";
  private Intent intent;

  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mToogle;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToogle.onOptionsItemSelected(item)){
         return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje);
        setTitle("INICIO");

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mToogle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open,R.string.close);


        mDrawerLayout.addDrawerListener(mToogle);

        mToogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




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



}
