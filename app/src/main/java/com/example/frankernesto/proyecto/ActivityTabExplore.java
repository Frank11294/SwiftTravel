package com.example.frankernesto.proyecto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by frank on 24/08/17.
 */

public class ActivityTabExplore extends  Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_explore, container, false);
        return rootView;
    }
}


