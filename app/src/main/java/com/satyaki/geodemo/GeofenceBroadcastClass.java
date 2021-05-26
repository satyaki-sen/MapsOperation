package com.satyaki.geodemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

class GeofenceBroadcastClass extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        GeofencingEvent geofencingEvent=GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e("HELLO", errorMessage);
            return;
        }

        Toast.makeText(context, "Geofence...", Toast.LENGTH_SHORT).show();
        Log.i("Its","Hi");


        int transition=geofencingEvent.getGeofenceTransition();
        if(transition==Geofence.GEOFENCE_TRANSITION_DWELL){
            Toast.makeText(context, "Dweeling...", Toast.LENGTH_SHORT).show();
        }
    }
}
