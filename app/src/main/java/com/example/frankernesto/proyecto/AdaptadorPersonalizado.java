package com.example.frankernesto.proyecto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by frank on 25/08/17.
 */

class AdaptadorPersonalizado extends ArrayAdapter<String>  implements View.OnClickListener{


    private View customView;
    private String [] _paises;
    private int [] _imagenes;
    private Boolean [] Checked ={false,false,false,false};
    private ViewHolder holder=null;
    private  AlertDialog.Builder constructor;
    private CharSequence[] items;
    private SharedPreferences prefs;
    private int posicion=0;

    private ArrayList mToggles;


    AdaptadorPersonalizado(Context context, String[]ciudades,String[]paises,int [] imagenes){

        super(context,R.layout.custom_row,ciudades);

        _paises=new String[paises.length];
        _imagenes=new int[imagenes.length];

        for(int i=0;i<paises.length;i++){
            _paises[i]=paises[i];
            _imagenes[i]=imagenes[i];
        }

        HashMap bool = new HashMap<Integer,Boolean>();

        bool.put("a",true);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        posicion=position;
        customView = convertView;

        if(customView == null){

            customView = inflater.inflate(R.layout.custom_row,parent,false);
            holder=new ViewHolder();



            ToggleButton favBtn = customView.findViewById(R.id.myToggleButton);
            favBtn.setBackgroundDrawable(ContextCompat.getDrawable(customView.getContext(), R.drawable.dislike_1));
            favBtn.setOnClickListener(this);


            holder.textCiudad=customView.findViewById(R.id.Ciudad);
            holder.textPais=customView.findViewById(R.id.Pais);
            holder.imagen=customView.findViewById(R.id.imagen);

            customView.setTag(holder);

            favBtn.setTag(position);



        favBtn.setBackgroundDrawable(ContextCompat.getDrawable(customView.getContext(), R.drawable.dislike_1));
        Checked[position] = false;



        }else{
            holder = (ViewHolder) customView.getTag();
            customView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    constructor.show();
                }
            });

        }



        String ciudad = getItem(position);
        String pais=_paises[position];


        holder.textCiudad.setText(ciudad);
        holder.textPais.setText(pais);
        holder.imagen.setImageResource(_imagenes[position]);


        items = new CharSequence[]{"Crear Viaje", "Guardar", "Cancelar"};

        constructor = new AlertDialog.Builder(getContext());
        constructor.setTitle("Seleccione un opcion: ");

        constructor.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Crear Viaje")) {

                }
                if (items[item].equals("Guardar")) {

                }
                if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }

            }
        });


        return customView;
    }



    @Override
    public void onClick(View view) {
        ToggleButton fav = (ToggleButton) view;


        int position=(Integer) fav.getTag();

        if(!Checked[position]) {
            fav.setBackgroundDrawable(ContextCompat.getDrawable(customView.getContext(), R.drawable.like_1));
            Checked[position]=true;
        } else {
            fav.setBackgroundDrawable(ContextCompat.getDrawable(customView.getContext(), R.drawable.dislike_1));
            Checked[position] = false;
        }

    }


    static class ViewHolder {
        private TextView textCiudad;
        private TextView textPais;
        private ImageView imagen;
    }

    @Override

    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }
}


