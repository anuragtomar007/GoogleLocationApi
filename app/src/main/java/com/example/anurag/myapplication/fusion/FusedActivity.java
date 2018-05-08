package com.example.anurag.myapplication.fusion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anurag.myapplication.R;
import com.example.anurag.myapplication.gps.LocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

public class FusedActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView latValue;
    private TextView longValue;
    private Boolean mRequestingLocationUpdates = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fused);
        latValue = findViewById(R.id.latValue);
        longValue = findViewById(R.id.longValue);

        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2500);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mRequestingLocationUpdates = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
            Toast.makeText(this, "Asking the Permission", Toast.LENGTH_SHORT).show();
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.v("Location Result", "onLocationResult:Checking the functionality ");
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    double latString = location.getLatitude();
                    double longString = location.getLongitude();
                    latValue.setText(String.valueOf(latString));
                    longValue.setText(String.valueOf(longString));
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.myLooper());
    }

    @Override
    public void onConnected(Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Suspended", "onConnectionSuspended:Checking the functionality ");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Failed", "onConnectionFailed:Checking the functionality ");
    }

    @Override
    public void onLocationChanged(Location location) {
        double latString = location.getLatitude();
        double longString = location.getLongitude();
        Log.v("Location Changed", "onLocationChanged:Checking the functionality ");
        latValue.setText(String.valueOf(latString));
        longValue.setText(String.valueOf(longString));
    }
}