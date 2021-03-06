package com.satyaki.geodemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    Double latitude, longitude;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient fusedLocationClient;
    GoogleMap m_googleMaps;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder builder;
    GeofencingClient geofencingClient;
    LatLng latLng;
    GeofencingClass geofencingClass;
    String GEOFENCE_ID="999";
    LocationCallback locationCallback;
    int n_check=0;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);


        geofencingClient = LocationServices.getGeofencingClient(this);
        geofencingClass = new GeofencingClass(this);

        createLocationRequest();
        checkLocationPermission();

        latLng=new LatLng(22.5703204,88.3942849);

    }

    public void addCircle(LatLng latLng, float radius) {

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(54,225,0,0));
        circleOptions.strokeWidth(4);
        m_googleMaps.addCircle(circleOptions);
    }

    public void addGeofencing(){

        Geofence geofence = geofencingClass.getGeofence(GEOFENCE_ID, latLng, 50);
        GeofencingRequest geofencingRequest = geofencingClass.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofencingClass.getGeofencePendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("Success","Done ok");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Check",e.toString());
                e.printStackTrace();
            }
        });

    }


    public void createLocationRequest() {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder();

    }

    public void stopLocation(){
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }


    public void requestCurrentLocations() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

           locationCallback=new LocationCallback(){

                @Override
                public void onLocationResult(LocationResult locationResult) {

                    if (locationResult != null && locationResult.getLocations().size()>0) {

                        int latestLocation=locationResult.getLocations().size()-1;
                        latitude=locationResult.getLocations().get(latestLocation).getLatitude();
                        longitude=locationResult.getLocations().get(latestLocation).getLongitude();

                        Log.i("Code",latitude+" "+longitude);

                        m_googleMaps.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("Marker"));
                        m_googleMaps.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));

                        Log.i("OKKK",latitude.toString());
                        Log.i("OKKKK",longitude.toString());

                        n_check+=1;
                    }
                    if(n_check==1){

                        addCircle(latLng,50);
                        addGeofencing();
                    }

                }

            };

            LocationServices.getFusedLocationProviderClient(MainActivity.this)
                    .requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper());

            //addCircle(latLng,100);

        }

    }





    @Override
    public void onMapReady(GoogleMap googleMap) {

         m_googleMaps=googleMap;

        m_googleMaps.setBuildingsEnabled(true);
        m_googleMaps.setMinZoomPreference(15f);
        m_googleMaps.setIndoorEnabled(true);

    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else
            {
            requestCurrentLocations();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                             requestCurrentLocations();
                    }

                }
                else
                    {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission....
                }

    }

}