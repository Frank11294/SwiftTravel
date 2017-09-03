package com.example.frankernesto.proyecto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class Tabbed_Main_Activity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1888;
    private static final int SELECT_FILE = 2888;
    private static int PROFILE_PIC_COUNT=0;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private ImageView img_header;

    private final String TAG = "Mi App";
    private Intent intent;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private Toolbar mToolBar;


    private TextView emailUsuario,nomUsuario;
    private NavigationView navigationView;
    private PlaceAutocompleteFragment autocompleteFragment;
    private CharSequence[] items;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private StorageReference mStorage;
    private ProgressDialog Progreso;

    private  AlertDialog.Builder constructor;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToogle.syncState();


    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed__main_);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

         // Al container se le pasa el adaptador de arriba que es el que pilla las pestañas
        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(0,true);


        //Esto es lo del toolbar personalizado ese q cree------------------------------------------------------

        mToolBar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolBar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mToogle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToogle);

       // Esto es solo para para poner nombre y email en el header
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        final View header_View = navigationView.getHeaderView(0);

        emailUsuario = (TextView)header_View.findViewById(R.id.email_header);
        nomUsuario = (TextView)header_View.findViewById(R.id.nom_header);
        img_header = (ImageView)header_View.findViewById(R.id.img_header);



        //Aqui empieza el rollo este de firebase---------------------------------------------------------------

        Progreso=new ProgressDialog(this);

        firebaseAuth =  FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        user = firebaseAuth.getCurrentUser();


        String nombreUsuario=user.getDisplayName();
        String email=user.getEmail();

        emailUsuario.setText(email);


        if(nombreUsuario!=null){
            nomUsuario.setText(user.getDisplayName().toUpperCase());
        }



        mStorage.child("/Photos/Profile/"+user.getUid().substring(4)+"-default.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Glide.with(Tabbed_Main_Activity.this)
                        .asBitmap()
                        .apply(RequestOptions.centerCropTransform())
                        .load(bytes)
                        .into(img_header);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        // Esto es para la imagen


        items = new CharSequence[]{"Hacer Foto", "Fotos de la galeria", "Cancelar"};

        constructor = new AlertDialog.Builder(this);
        constructor.setTitle("Añadir Foto!");

        constructor.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Hacer Foto")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);

                }
                if (items[item].equals("Fotos de la galeria")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1);

                }
                if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }

            }
        });

        img_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constructor.show();
            }
        });




        //Esto es lo del autocomplete de google-----------------------------------------------------------------
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("");
        //Aqui termina



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
                        String nombreUsuario=user.getDisplayName();
                        if(nombreUsuario!=null){
                            nomUsuario.setText(user.getDisplayName().toUpperCase());
                        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent_imagen) {
        super.onActivityResult(requestCode, resultCode, intent_imagen);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    setFotoCamara(intent_imagen);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    setFotoGaleria(intent_imagen);
                }
                break;
        }
    }

    private void setFotoGaleria(Intent intent_imagen)  {
        Progreso.setMessage("Subiendo foto de Perfil...");
        Progreso.show();

        Uri selectedImage = intent_imagen.getData();
        StorageReference filePath=mStorage.child("Photos").child("Profile").child(user.getUid().substring(4)+"-default.jpg");

        filePath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Progreso.dismiss();

                Uri downloadUri = taskSnapshot.getDownloadUrl();

                Glide.with(Tabbed_Main_Activity.this)
                        .asBitmap()
                        .apply(RequestOptions.centerCropTransform())
                        .load(downloadUri)
                        .into(img_header);

                Toast.makeText(getApplicationContext(),"Foto subida con exito...",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setFotoCamara(Intent intent_imagen)  {

        Bundle extras = intent_imagen.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");


        img_header.setImageBitmap(imageBitmap);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.normal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToogle.onOptionsItemSelected(item)){
           return true;
        }
        int id = item.getItemId();

        if (id == R.id.about_us) {
            Toast.makeText(getApplicationContext(),"Swift Travel\nVersion: 1.0\nAutores: Frank Roman Izaguirre\nRuben Argumosa",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //He eliminado la clase PlaceHolder

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
          //Return the current tabs
            switch (position){
                case 0:
                    ActivityTabExplore tab1= new ActivityTabExplore();
                return tab1;

                case 1:
                    ActivityTabWhislist tab2=new ActivityTabWhislist();
                    return tab2;
                default:

                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "EXPLORAR";
                case 1:
                    return "VIAJES";
            }
            return null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        autocompleteFragment.setText("");
    }




}
