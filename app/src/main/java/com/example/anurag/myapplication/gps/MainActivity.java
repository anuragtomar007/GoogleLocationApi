package com.example.anurag.myapplication.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anurag.myapplication.R;
import com.google.android.gms.location.LocationListener;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView latitude;
    private TextView longitude;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude = findViewById(R.id.latValue);
        longitude = findViewById(R.id.longiValue);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Toast.makeText(this, "Provider" + provider + "has been selected", Toast.LENGTH_SHORT);
            onLocationChanged(location);
        } else {
            latitude.setText("Location Not Available");
            longitude.setText("Location Not Available");
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double longi = location.getLongitude();
        latitude.setText(String.valueOf(lat));
        longitude.setText(String.valueOf(longi));
    }
}
