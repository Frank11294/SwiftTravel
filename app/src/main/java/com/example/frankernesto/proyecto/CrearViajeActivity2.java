package com.example.frankernesto.proyecto;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


public class CrearViajeActivity2 extends AppCompatActivity {
    private Toolbar mToolBar;
    private String TAG="URL ERROR >>> : ";
    private Intent intent;
    private String Placeid, Nom,imagenUrl;
    private TextView nombreLoc;
    private final String API_KEY = "AIzaSyBshUoGvYcxLkaJU_wgCzeGe3ek4E4h898";
    private ImageView imagen;
    private TextView nombreCiudad;
    private String respuesta = null;
    URLServicio servicio;
    private ProgressDialog barraDialogo;
    private ArrayList<String> fotosR;
    private Bitmap [] Fotos_Lugares;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private Button CrearViaje;
    private ViewPager viewPager;
    private AdaptadorPersonalizado_Swipe adaptador;

    private EditText fechaIn,fechaOut;
    private int anno_in, mes_in, dia_in,anno_out,mes_out,dia_out;
    private static  int TIPO_DIALOGO=0;
    private static DatePickerDialog.OnDateSetListener oyenteSelectorFecha_IN,oyenteSelectorFecha_OUT;

    private NavigationView navigationView;


    public CrearViajeActivity2()  {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje2);


        CrearViaje=findViewById(R.id.crearViaje);
        mToolBar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(mToolBar);


// Y esto es el menu

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_2);
        mToogle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView  = (NavigationView) findViewById(R.id.nav_view_2);
        fechaIn=findViewById(R.id.fecha_entrada);
        fechaOut=findViewById(R.id.fecha_salida);
        nombreCiudad=findViewById(R.id.nombre_ciudad);


        intent = getIntent();

        Placeid = intent.getStringExtra("PlaceID");
        Nom = intent.getStringExtra("NOMBRE");

        nombreCiudad.setText(Nom.toUpperCase());


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


                for (int i = 0; i < 4; i++) {

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


         Fotos_Lugares = new Bitmap [fotosR.size()];

        for (int i=0;i<fotosR.size();i++){
            imagenUrl =String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",fotosR.get(i),API_KEY);
            URL url = null;
            try {
                url = new URL(imagenUrl);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Fotos_Lugares[i]=bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
// Esto es lo de las imagenes
        viewPager=(ViewPager)findViewById(R.id.view_pager_fotos);
        adaptador = new AdaptadorPersonalizado_Swipe(this,Fotos_Lugares);
        viewPager.setAdapter(adaptador);

//Esto es lo de los puntos de orientacion de la imagenes
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);


        Calendar calendario =Calendar.getInstance();

        anno_in =calendario.get(Calendar.YEAR);
        mes_in =calendario.get(Calendar.MONTH);
        dia_in =calendario.get(Calendar.DAY_OF_MONTH);

        anno_out =calendario.get(Calendar.YEAR);
        mes_out =calendario.get(Calendar.MONTH);
        dia_out =calendario.get(Calendar.DAY_OF_MONTH);

        oyenteSelectorFecha_IN = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                anno_in =year;
                mes_in =month;
                dia_in =day;
                fechaIn.setHint(anno_in +"/"+ mes_in +"/"+ dia_in);
            }
        };

        oyenteSelectorFecha_OUT = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                anno_out =year;
                mes_out =month;
                dia_out =day;
                fechaOut.setHint(anno_out +"/"+ mes_out +"/"+ dia_out);
            }
        };

       fechaIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               TIPO_DIALOGO=0;
               mostrarCalendario(view);

           }
       });

        fechaOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TIPO_DIALOGO=1;
                mostrarCalendario(view);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch (id){
                    case R.id.nav_salir:
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
                         startActivity(new Intent(getApplicationContext(),Tabbed_Main_Activity.class));
                        break;
                    case R.id.nav_listaD:
                        Toast.makeText(getApplicationContext(),"Lista de compra",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });


        CrearViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Tabbed_Secondary_Activity.class));
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToogle.onOptionsItemSelected(item)){
            return true;
        }
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case 0:
                return new DatePickerDialog(this, oyenteSelectorFecha_IN, anno_in, mes_in, dia_in);
            case 1:
                return new DatePickerDialog(this, oyenteSelectorFecha_OUT, anno_out, mes_out, dia_out);

        }
        return null;
    }

    public void mostrarCalendario(View control){
        showDialog(TIPO_DIALOGO);
    }
}



