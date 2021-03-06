package com.satyaki.geodemo;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

class GeofencingClass extends ContextWrapper {

    PendingIntent geofencePendingIntent;

    public GeofencingClass(Context base) {
        super(base);
    }


    public GeofencingRequest getGeofencingRequest(Geofence geofence){

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.addGeofence(geofence);
        builder.setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER);
       // builder.addGeofences(geofenceList);
        return builder.build();
    }


    public Geofence getGeofence(String id, LatLng latLng,float radius){

        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude,latLng.longitude,radius)
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(2000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();

    }

    public PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        Log.i("Yoo","Ok");
        Intent intent = new Intent(this,GeofenceBroadcastClass.class);

        Toast.makeText(this, "Working.....", Toast.LENGTH_SHORT).show();
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }


}
