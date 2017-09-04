package com.example.frankernesto.proyecto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by frank on 24/08/17.
 */

public class ActivityTabWhislist extends Fragment {
    private DatabaseReference databaseViajes;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;

    private ArrayList<String>listaViajes=new ArrayList<>();

    private ListView listViewViajes;
    private String Nom;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_main_whislist, container, false);
        firebaseAuth =  FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        listViewViajes=rootView.findViewById(R.id.lista_viajes);

        arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaViajes);
        listViewViajes.setAdapter(arrayAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Nom = getArguments().getString("Nom");
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        databaseViajes = database.getReference().child("Usuarios").child("User-"+user.getUid()).child("Viajes");

        databaseViajes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Viajes viaje = (Viajes)dataSnapshot.getValue(Viajes.class);
                String Viaje = "Nombre del Viaje: "+viaje.getNombreViaje()+"\nCiudad: "+viaje.getNombreLugar()+"\nHotel: "+viaje.getHotel();
                System.out.print(dataSnapshot.getValue());
                listaViajes.add(Viaje);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Viajes viaje = (Viajes)dataSnapshot.getValue(Viajes.class);
                String Viaje = viaje.getNombreViaje();
                System.out.print(dataSnapshot.getValue());
                listaViajes.add(Viaje);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Viajes viaje = (Viajes)dataSnapshot.getValue(Viajes.class);
                String Viaje = viaje.getNombreViaje();
                System.out.print(dataSnapshot.getValue());
                listaViajes.remove(Viaje);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return rootView;
    }

    @IgnoreExtraProperties
    public static class Viajes {

        private String FechaIn;
        private String FechaOut;
        private String Hotel;
        private String IDViaje;
        private String NombreLugar;
        private String NombreViaje; // etc. all fieldnames in the database





        public Viajes() {
        }

        public String getNombreViaje() {
            return NombreViaje;
        }

        public void setNombreViaje(String nombreViaje) {
            NombreViaje = nombreViaje;
        }

        public String getNombreLugar() {
            return NombreLugar;
        }

        public void setNombreLugar(String nombreLugar) {
            NombreLugar = nombreLugar;
        }

        public String getIDViaje() {
            return IDViaje;
        }

        public void setIDViaje(String IDViaje) {
            this.IDViaje = IDViaje;
        }

        public String getHotel() {
            return Hotel;
        }

        public void setHotel(String hotel) {
            Hotel = hotel;
        }

        public void setFechaIn(String fechaIn) {
            FechaIn = fechaIn;
        }

        public String getFechaIn() {
            return FechaIn;
        }

        public String getFechaOut() {
            return FechaOut;
        }

        public void setFechaOut(String fechaOut) {
            FechaOut = fechaOut;
        }

        @Override
        public String toString() {
            return  this.NombreViaje + "" +this.NombreLugar + "" + this.IDViaje + ""+this.FechaIn +""+FechaOut+""+Hotel;
        }
    }



}



