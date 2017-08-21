package com.example.frankernesto.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.maps.GoogleMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;



public class CrearViajeActivity2 extends AppCompatActivity {

    private String TAG="URL ERROR >>> : ";
    private Intent intent;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private GeoDataApi mGeoDataApi;

    private String Placeid, Nom;
    private TextView nombreLoc;
    private final String API_KEY = "AIzaSyCDabyMbAGESJzhzvPSb-mVx2eQOinJtHs";

    private ImageView imagen;
    String respuesta = null;

    URLServicio servicio;
    private ProgressDialog barraDialogo;
    private ArrayList<String> fotosR;

    public CrearViajeActivity2()  {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        imagen = findViewById(R.id.imageView2);
        nombreLoc = findViewById(R.id.textView);

        intent = getIntent();

        Placeid = intent.getStringExtra("PlaceID");
        Nom = intent.getStringExtra("NOMBRE");

        nombreLoc.setText(Nom);
        Log.e("ID DEL LUGAR: ",Placeid);

        //aqui extraigo el reference de google maps API Places

       respuesta=String.format("https://maps.googleapis.com/maps/api/place/details/json?placeid=%s&key=%s",Placeid,API_KEY);


       servicio= new URLServicio();
       String jsonStr = servicio.UrlServicio(respuesta);


        fotosR = new ArrayList<>();
        if(jsonStr!=null){

            try{
                JSONObject jsonObj = new JSONObject(jsonStr);

                JSONObject jsonObjResult = jsonObj.getJSONObject("result");

                JSONArray photos = jsonObjResult.getJSONArray("photos");


                for (int i = 0; i < photos.length(); i++) {

                    JSONObject c = photos.getJSONObject(i);

                    String photoReference = c.getString("photo_reference");

                    fotosR.add(photoReference);

                }


            }catch(JSONException ex){
                Log.e(TAG, "Extraccion de datos JSON falla : " + ex.getMessage());

                Toast.makeText(getApplicationContext(),
                        "Extraccion de datos JSON falla: " + ex.getMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            }

        }else{
            Log.e(TAG, "Ha fallado la extraccion del JSON.");
        }


         // AQUI ES DONDE HAGO LA CONSULTA A GOOGLE PLACE PHOTOS
        String imagenUrl =String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",fotosR.get(0),API_KEY);
        try{
            URL url2 = new URL(imagenUrl);
            Bitmap bmp = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
            imagen.setImageBitmap(bmp);

        }catch(MalformedURLException ex){

        }catch(IOException ex){

        }



    }

}

