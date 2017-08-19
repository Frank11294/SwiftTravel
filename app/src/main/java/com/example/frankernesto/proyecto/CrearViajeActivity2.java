package com.example.frankernesto.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageSwitcher;
import android.widget.TextView;


import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.GeoDataApi;


public class CrearViajeActivity2 extends AppCompatActivity {

    private ImageSwitcher sw;
    private Intent intent;

    private String id , Nom;
    private TextView nombreLoc;
    private GeoDataApi mGeoDataApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje2);

        intent=getIntent();
        id=intent.getStringExtra("ID");
        Nom=intent.getStringExtra("NOMBRE");



//        nombreLoc = (TextView) findViewById(R.id.textView);
//        nombreLoc.setText("id :"+id+" Nombre :"+Nom);

//        sw = (ImageSwitcher)findViewById(R.id.imgsw);
//
//        sw.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//                ImageView imageView = new ImageView(getApplicationContext());
//                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//
//                return imageView;
//            }
//        });

    }

//    private void getFotos(String idLugar){
//        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataApi
//
//
//    }
}
