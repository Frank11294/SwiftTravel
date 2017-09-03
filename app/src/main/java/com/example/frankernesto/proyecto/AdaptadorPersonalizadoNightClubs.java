package com.example.frankernesto.proyecto;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by frank on 3/09/17.
 */

public class AdaptadorPersonalizadoNightClubs extends ArrayAdapter<String> implements View.OnClickListener{

    private View customView;
    private Bitmap[] _imagenesHoteles;
    private ArrayList<Float> _Rating;
    private ViewHolder holder=null;



    AdaptadorPersonalizadoNightClubs(Context context, ArrayList<String>nombreHoteles, ArrayList <Float>Rating, Bitmap [] imagenesHoteles){

        super(context,R.layout.custom_row_night_clubs,nombreHoteles);

        _imagenesHoteles=new Bitmap[imagenesHoteles.length];
        _Rating=new ArrayList<>();
        _Rating=Rating;


        for(int i=0;i<imagenesHoteles.length;i++){
            this._imagenesHoteles=imagenesHoteles;
        }

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        customView = convertView;

        if(customView == null){

            customView = inflater.inflate(R.layout.custom_row_night_clubs,parent,false);

            holder=new ViewHolder();



            holder.textHotel=customView.findViewById(R.id.nombre_nightClub);
            holder.imagenHotel=customView.findViewById(R.id.imagen_nightClub);
            holder.ratingBar=customView.findViewById(R.id.ratingBar_nightclub);



            customView.setTag(holder);


        }else{
            holder =(ViewHolder) customView.getTag();
        }

        if(!(position>_imagenesHoteles.length-1)){
            String nombre_Hotel = getItem(position);
            float rate=_Rating.get(position);
            Bitmap aux=_imagenesHoteles[position];

            holder.textHotel.setText(nombre_Hotel);
            holder.ratingBar.setRating(rate);
            holder.imagenHotel.setImageBitmap(aux);
        }



        return customView;
    }




    static class ViewHolder {

        private TextView textHotel;
        private RatingBar ratingBar;
        private ImageView imagenHotel;


    }

    @Override

    public int getViewTypeCount() {

        return 1;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public void onClick(View view) {

    }
}
