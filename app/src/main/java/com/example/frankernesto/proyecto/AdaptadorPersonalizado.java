package com.example.frankernesto.proyecto;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by frank on 25/08/17.
 */

class AdaptadorPersonalizado extends ArrayAdapter<String>  {

    private ToggleButton favorite;
    private View customView;
    private String [] _paises;
    private int [] _imagenes;
    private Boolean [] onOff ={false,false,false,false};
    private int pos;
    private ArrayList itemChecked;

    AdaptadorPersonalizado(Context context, String[]ciudades,String[]paises,int [] imagenes){

        super(context,R.layout.custom_row,ciudades);

        _paises=new String[paises.length];
        _imagenes=new int[imagenes.length];

        for(int i=0;i<paises.length;i++){
            _paises[i]=paises[i];
            _imagenes[i]=imagenes[i];
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        customView = inflater.inflate(R.layout.custom_row,parent,false);

        String ciudad = getItem(position);
        String pais=_paises[position];


        TextView textCiudad = customView.findViewById(R.id.Ciudad);
        TextView textPais = customView.findViewById(R.id.Pais);
        ImageView imagen = customView.findViewById(R.id.imagen);
        favorite = customView.findViewById(R.id.myToggleButton);

        textCiudad.setText(ciudad);
        textPais.setText(pais);
        imagen.setImageResource(_imagenes[position]);

        favorite.setBackgroundDrawable(ContextCompat.getDrawable(customView.getContext(), R.drawable.dislike));


       favorite.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (onOff[position]) {
                   favorite.setChecked(false);
                   onOff[position] = false;
                   Toast.makeText(customView.getContext(), "is off on position: "+position, Toast.LENGTH_SHORT).show();
                   favorite.setBackgroundDrawable(ContextCompat.getDrawable(customView.getContext(), R.drawable.dislike));
               } else {

                   onOff[position] = true;
                   favorite.setChecked(true);
                   Toast.makeText(customView.getContext(), "is on on position: "+position, Toast.LENGTH_SHORT).show();
                   favorite.setBackgroundDrawable(ContextCompat.getDrawable(customView.getContext(), R.drawable.like));
               }
           }
       });

        favorite.setChecked(onOff[position]);

        return customView;
    }
}
