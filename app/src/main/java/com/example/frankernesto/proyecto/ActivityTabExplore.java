package com.example.frankernesto.proyecto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * Created by frank on 24/08/17.
 */

public class ActivityTabExplore extends  Fragment {

    ListView miLista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_main_explore, container, false);

        String [] countries={"Francia","Inglaterra","Alemania","Italia"};
        String [] cities ={"Paris","Londres","Berlin","Venecia"};


        int [] imagenes = {R.mipmap.paris,R.mipmap.london,R.mipmap.berlin,R.mipmap.venice};

        ListAdapter myAdapter = new AdaptadorPersonalizado(rootView.getContext(),cities,countries,imagenes);

        miLista = (ListView) rootView.findViewById(R.id.miLista);

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


