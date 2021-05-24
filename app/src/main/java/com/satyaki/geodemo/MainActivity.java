package com.satyaki.geodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        GoogleMap m_googleMaps=googleMap;

        m_googleMaps.setBuildingsEnabled(true);
        m_googleMaps.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(22.5726, 88.3639)));
        m_googleMaps.setMinZoomPreference(10f);
        m_googleMaps.setIndoorEnabled(true);

        m_googleMaps.addMarker(new MarkerOptions()
                .position(new LatLng(22.5726, 88.3639))
                .title("Marker"));
    }
}