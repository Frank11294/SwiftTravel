package com.example.frankernesto.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.maps.GoogleMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class CrearViajeActivity2 extends AppCompatActivity {

    private String TAG="URL ERROR >>> : ";
    private Intent intent;

    private String Placeid, Nom,imagenUrl;
    private TextView nombreLoc;
    private final String API_KEY = "AIzaSyBshUoGvYcxLkaJU_wgCzeGe3ek4E4h898";

    private ImageView imagen;
    private TextView nombre;

    private String respuesta = null;

    URLServicio servicio;
    private ProgressDialog barraDialogo;
    private ArrayList<String> fotosR;


    public CrearViajeActivity2()  {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje2);

        intent = getIntent();

        Placeid = intent.getStringExtra("PlaceID");
        Nom = intent.getStringExtra("NOMBRE");

        imagen = findViewById(R.id.imageView2);
        nombre = findViewById(R.id.textView);

        nombre.setText(Nom);
        nombre.setTextColor(Color.WHITE);

        // ESTO NO SE PUEDE QUEDAR ASI (estas dos lineas hay q arreglarlas)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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


//          AQUI ES DONDE HAGO LA CONSULTA A GOOGLE PLACE PHOTOS

        imagenUrl =String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",fotosR.get(0),API_KEY);
        try{
            URL url2 = new URL(imagenUrl);
            Bitmap bmp = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
            imagen.setImageBitmap(bmp);
        }catch(MalformedURLException ex){

        }catch(IOException ex){

        }
    }
}


