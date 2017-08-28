package com.example.frankernesto.proyecto;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Tabbed_Main_Activity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static int PROFILE_PIC_COUNT=0;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private ImageButton img_header;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed__main_);




        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(1,true);




        //Esto es lo del toolbar personalizado ese q cree------------------------------------------------------

        mToolBar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolBar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mToogle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToogle);

       // Esto es solo para para poner nombre y email en el header
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header_View = navigationView.getHeaderView(0);

        emailUsuario = (TextView)header_View.findViewById(R.id.email_header);
        nomUsuario = (TextView)header_View.findViewById(R.id.nom_header);
        img_header = (ImageButton)header_View.findViewById(R.id.img_header);



        //Aqui empieza el rollo este de firebase---------------------------------------------------------------

        firebaseAuth =  FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //Aqui termina

        emailUsuario.setText(user.getEmail());
        // Esto es para la imagen


        final CharSequence[] items = {"Hacer Foto", "Fotos de la galeria", "Cancelar"};

        final AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setTitle("Añadir Foto!");
        constructor.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Hacer Foto")) {
                    PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Fotos de la galeria")) {
                    PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("Cancelar")) {
                    PROFILE_PIC_COUNT = 0;
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



        //Esto es para despues cuando tengo hecha la base de datos

//        Intent intento=getIntent();
//        String nomU=intento.getStringExtra("Nombre");
//        nomUsuario.setText(nomU);


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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent_imagen) {
        super.onActivityResult(requestCode, resultCode, intent_imagen);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    setFoto(intent_imagen);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                   setFoto(intent_imagen);
                }
                break;
        }
    }

    private void setFoto(Intent intent_imagen) {
        Bitmap foto;
        foto=getCroppedBitmap(Bitmap.createScaledBitmap((Bitmap)intent_imagen.getExtras().get("data"), 190, 165, true));
        img_header.setImageBitmap(null);
        img_header.setForeground(null);
        img_header.setImageBitmap(foto);
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

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

}
