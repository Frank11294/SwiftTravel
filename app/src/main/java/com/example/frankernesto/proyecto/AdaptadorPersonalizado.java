package com.example.frankernesto.proyecto;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private  int pos=0;

    private ArrayList mToggles;

    HashMap<Integer,Boolean> toggleButtonSeguidorEstado ;

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
        customView = convertView;
        if(customView == null){

            pos=position;

            customView = inflater.inflate(R.layout.custom_row,parent,false);
            holder=new ViewHolder();

            ToggleButton favBtn = customView.findViewById(R.id.myToggleButton);
            favBtn.setBackgroundDrawable(ContextCompat.getDrawable(customView.getContext(), R.drawable.dislike));
            favBtn.setOnClickListener(this);

            holder.textCiudad=customView.findViewById(R.id.Ciudad);
            holder.textPais=customView.findViewById(R.id.Pais);
            holder.imagen=customView.findViewById(R.id.imagen);
            customView.setTag(holder);

        }else{
            holder = (ViewHolder) customView.getTag();
        }

        String ciudad = getItem(position);
        String pais=_paises[position];


        holder.textCiudad.setText(ciudad);
        holder.textPais.setText(pais);
        holder.imagen.setImageResource(_imagenes[position]);





        return customView;
    }

    @Override
    public void onClick(View view) {
        ToggleButton fav = (ToggleButton) view;
        if(!Checked[pos]) {
            Toast.makeText(customView.getContext(),"ON in Posicion: "+pos,Toast.LENGTH_SHORT).show();
            fav.setBackgroundDrawable(ContextCompat.getDrawable(customView.getContext(), R.drawable.like));
            Checked[pos]=true;
        } else {
            Toast.makeText(customView.getContext(),"OFF in Posicion: "+pos,Toast.LENGTH_SHORT).show();
            fav.setBackgroundDrawable(ContextCompat.getDrawable(customView.getContext(), R.drawable.dislike));
            Checked[pos] = false;
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

}


