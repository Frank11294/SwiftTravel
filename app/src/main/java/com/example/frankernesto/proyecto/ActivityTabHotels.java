package com.example.frankernesto.proyecto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ActivityTabHotels extends Fragment {

    private String respuesta = null;
    private final String API_KEY = "AIzaSyBshUoGvYcxLkaJU_wgCzeGe3ek4E4h898";
    private String PlaceName = null, imagenUrl;
    private URLServicio servicio;
    private ArrayList<String> NombreHoteles, FotoHoteles;
    private ArrayList<Float> RatingHoteles;
    private Bitmap[] Fotos_Hoteles;
    private ListView miLista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_secondary_hoteles, container, false);

//         ESTO NO SE PUEDE QUEDAR ASI (estas dos lineas hay q arreglarlas)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Bundle bundle = getArguments();
        if (bundle != null) {
            PlaceName = getArguments().getString("nomLugar");
        }

        respuesta = String.format("https://maps.googleapis.com/maps/api/place/textsearch/json?query=hotels+in+%s&key=%s", PlaceName, API_KEY);
        servicio = new URLServicio();
        String jsonStr = servicio.UrlServicio(respuesta);

        NombreHoteles = new ArrayList<>();
        FotoHoteles = new ArrayList<>();
        RatingHoteles = new ArrayList<>();


        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                JSONArray arrayResults = jsonObj.getJSONArray("results");

                JSONArray photos = arrayResults.getJSONObject(0).getJSONArray("photos");

                for (int i=0;i<arrayResults.length();i++){
                    JSONObject aux = arrayResults.getJSONObject(i);
                    Double ratingHotel = aux.getDouble("rating");
                    String nombreHotel = aux.getString("name");
                    NombreHoteles.add(nombreHotel);
                    RatingHoteles.add(ratingHotel.floatValue());
                }

                for (int i = 0; i < photos.length(); i++) {
                    JSONObject c = photos.getJSONObject(i);
                    String photoReference = c.getString("photo_reference");
                    FotoHoteles.add(photoReference);
                }


            } catch (JSONException ex) {
                Log.e("HOTEL JSON PROBLEM: ", "Extraccion de datos JSON falla : " + ex.getMessage());

                Toast.makeText(rootView.getContext(),
                        "Extraccion de datos JSON falla: " + ex.getMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            }

        } else {
            Log.e("HOTEL JSON PROBLEM: ", "Ha fallado la extraccion del JSON.");
        }


        Fotos_Hoteles = new Bitmap[FotoHoteles.size()];

        for (int i = 0; i < FotoHoteles.size(); i++) {
            imagenUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s", FotoHoteles.get(i), API_KEY);
            URL url = null;
            try {
                url = new URL(imagenUrl);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Fotos_Hoteles[i] = bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            ListAdapter myAdapter = new AdaptadorPersonalizadoHoteles(rootView.getContext(), NombreHoteles, RatingHoteles, Fotos_Hoteles);

            miLista = (ListView) rootView.findViewById(R.id.miListaHoteles);

            miLista.setAdapter(myAdapter);

            miLista.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        }
                    }
            );
            return rootView;
        }
}
