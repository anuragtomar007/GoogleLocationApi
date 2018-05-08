package com.example.anurag.myapplication.gps;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

public class LocationService extends Service {
    private LocationListener locationListener;
    private LocationManager locationManager;
    private String provider;
    private int sec = 2;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_updated");
                i.putExtra("Coordinates", location.getLongitude() + " " + location.getLatitude());
                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Toast.makeText(this, "Provider" + provider + "has been selected", Toast.LENGTH_SHORT);
            locationListener.onLocationChanged(location);
        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,sec*1000,0,locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
    }
}

