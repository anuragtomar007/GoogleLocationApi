package com.example.anurag.myapplication.gps;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.anurag.myapplication.R;

public class MainActivityService extends AppCompatActivity {
    private TextView textView;
    private Button start;
    private BroadcastReceiver broadcastReceiver;
    private Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_layout);
        textView = findViewById(R.id.textCoordinates);
        start = findViewById(R.id.startService);
        stop = findViewById(R.id.stopService);

        if (!runtime_Permission()) {
            enable_buttons();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    textView.append("\n"+intent.getExtras().get("Coordinates"));
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_updated"));

    }

    private void enable_buttons() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityService.this,LocationService.class);
                startService(i);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityService.this,LocationService.class);
                stopService(i);
            }
        });
    }

    private Boolean runtime_Permission() {
        if (Build.VERSION.SDK_INT > 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
            } else {
                runtime_Permission();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver!=null)
            unregisterReceiver(broadcastReceiver);
    }
}
