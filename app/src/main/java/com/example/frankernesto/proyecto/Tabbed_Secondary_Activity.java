package com.example.frankernesto.proyecto;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class Tabbed_Secondary_Activity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private NavigationView navigationView;
    private SectionsPagerAdapter mSectionsPagerAdapter_secondary;
    private ViewPager mViewPager_secondary;
    private Intent getDatosCrearActivity;
    private String idLugar,nomLugar;
    private Date fechaIn,fechaOut;
    private TextView city,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed__secondary_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(toolbar);

        //Menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_3);
        mToogle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView  = (NavigationView) findViewById(R.id.nav_view_3);
        //Fin de menu


        mSectionsPagerAdapter_secondary = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager_secondary = (ViewPager) findViewById(R.id.container_secondary);
        mViewPager_secondary.setAdapter(mSectionsPagerAdapter_secondary);



        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_secondary);
        tabLayout.setupWithViewPager(mViewPager_secondary);

        mViewPager_secondary.setCurrentItem(0,true);


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout, tabLayout, false);
            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
            tab.select();
        }

        getDatosCrearActivity = getIntent();
        nomLugar=getDatosCrearActivity.getStringExtra("NombreLugar");
        idLugar=getDatosCrearActivity.getStringExtra("IdLugar");
        this.fechaIn=(Date)getDatosCrearActivity.getSerializableExtra("FechaIn");
        this.fechaOut=(Date)getDatosCrearActivity.getSerializableExtra("FechaOut");

        city=(TextView) findViewById(R.id.City);
        date=(TextView) findViewById(R.id.Date);

        String diaEntrada  = (String) DateFormat.format("dd",   fechaIn); // 20
        String mesEntrada  = (String) DateFormat.format("MMM",  fechaIn); // Jun

        String diaSalida  = (String) DateFormat.format("dd",   fechaOut); // 20
        String mesSalida  = (String) DateFormat.format("MMM",  fechaOut); // Jun


        city.setText(nomLugar.toUpperCase());
        date.setText(diaEntrada+" "+mesEntrada+" - "+diaSalida+" "+mesSalida);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabbed__secondary_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToogle.onOptionsItemSelected(item)){
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(),"FUNCIONA",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
          switch (position){
              case 0:
                  ActivityTabHotels tab1= new ActivityTabHotels();
                  Bundle bundle = new Bundle();
                  bundle.putString("nomLugar",nomLugar);
                  tab1.setArguments(bundle);
                  return tab1;
              case 1:
                  ActivityTabVuelos tab2= new ActivityTabVuelos();
//                  Bundle bundle2 = new Bundle();
//                  bundle2.putString("idLugar",idLugar);
//                  tab2.setArguments(bundle2);
                  return tab2;
              case 2:
                  ActivityTabRenta tab3= new ActivityTabRenta();
                  Bundle bundle3 = new Bundle();
                  bundle3.putString("nomLugar",nomLugar);
                  tab3.setArguments(bundle3);
                  return tab3;
              case 3:
                  AcitivityTabOcio tab4= new AcitivityTabOcio();
                  Bundle bundle4 = new Bundle();
                  bundle4.putString("nomLugar",nomLugar);
                  tab4.setArguments(bundle4);
                  return tab4;
              default:
                  return null;
          }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HOTELES";
                case 1:
                    return "VUELOS";
                case 2:
                    return "MUSEOS";
                case 3:
                    return "OCIO";
            }
            return null;
        }
    }
}
