package com.example.frankernesto.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.GeoDataApi;
import com.squareup.picasso.Picasso;


public class CrearViajeActivity2 extends AppCompatActivity {

    private ImageSwitcher sw;
    private Intent intent;

    private String id , Nom;
    private TextView nombreLoc;
    private GeoDataApi mGeoDataApi;
    private final String API_KEY="AIzaSyD68b2rZ58lgs4ZsdoSgFaYtLYyIVnb7dE";

    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje2);

       // imagen = (ImageView) findViewById(R.id.imageView2);

        intent=getIntent();
        id=intent.getStringExtra("ID");
        Nom=intent.getStringExtra("NOMBRE");


        String url = String.format("https://maps.googleapis.com/maps/api/place/photo?maxWidth=%d&photoreference=%s&key=%s",200,id,API_KEY);

        Picasso.with(this).load("https://www.google.es/search?q=random+photo&tbm=isch&imgil=ashA-nYuhh7bAM%253A%253BWXKPgRO8rZhp1M%253Bhttp%25253A%25252F%25252Fwww.thedesignwork.com%25252F40-random-pictures-of-conceptual-and-creative-ideas%25252F&source=iu&pf=m&fir=ashA-nYuhh7bAM%253A%252CWXKPgRO8rZhp1M%252C_&usg=__LGg6-y7IIWdO2moxZdT991f9TNA%3D&biw=1324&bih=932&ved=0ahUKEwjttubZkOLVAhUFExoKHedJDRcQyjcIQw&ei=PZKXWa2kOYWmaOeTtbgB#imgrc=ashA-nYuhh7bAM:").into(imagen);




    }


}
