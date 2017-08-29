package com.example.frankernesto.proyecto;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by frank on 28/08/17.
 */

public class AdaptadorPersonalizado_Swipe extends PagerAdapter {

      private Context contexto;
      private Bitmap [] fotosL;
      private LayoutInflater inflaLayout;


    public AdaptadorPersonalizado_Swipe(Context context,Bitmap[] fotos) {
        this.contexto=context;
        this.fotosL=fotos;
    }

    @Override
    public int getCount() {
        return fotosL.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflaLayout = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = inflaLayout.inflate(R.layout.swipe_photo_layout,container,false);
        ImageView imageView = item_view.findViewById(R.id.spl_imageView);
        imageView.setImageBitmap(fotosL[position]);
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
