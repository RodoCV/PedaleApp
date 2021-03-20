package com.example.pedaleapp.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pedaleapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class Map_inacap extends Fragment implements OnMapReadyCallback {
    // --- Elementos de Fragment ---
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    // --- ---

    public Map_inacap() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_inacap, container, false);

        // --- Obtener el SupportMapFragment y obtener notificación cuando el mapa esté listo para usarlo ---
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        // --- ---

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng inacapTalca = new LatLng(-35.4365336 ,-71.6234193); //Coordenadas de ubicación, para el marcador.
        mMap.addMarker(new MarkerOptions().position(inacapTalca).title("INACAP Sede Talca")); //Coloca un marcador. Su posición y título en el mapa.
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(inacapTalca.latitude, inacapTalca.longitude), 15.0f)); //Coloca la cámara en el marcador y ajusta el zoom.
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(inacapTalca));
    }
}