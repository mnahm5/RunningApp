package com.example.mnahm5.runningapp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.fitness.data.Application;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Record extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int REQUEST_PERMISSION_GPS = 1;
    private int count = 0;
    private String btMsg;
    private Marker startMarker = null;
    private Marker endMarker = null;
    private boolean recordIndicator = false;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button btTop = (Button) findViewById(R.id.btTop);
        final Button btMiddle = (Button) findViewById(R.id.btMiddle);
        final Button btBottom = (Button) findViewById(R.id.btBottom);

        btTop.setVisibility(View.GONE);
        btMiddle.setVisibility(View.GONE);

        Toast.makeText(Record.this,"Please Wait while we pinpoint your location",Toast.LENGTH_SHORT).show();

        btBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    recordIndicator = true;
                    btMsg = "Pause/Stop";
                    btBottom.setText(btMsg);
                }
                else if (count == 1) {
                    recordIndicator = false;
                    btMsg = "Save";
                    btBottom.setText(btMsg);
                    btMiddle.setVisibility(View.VISIBLE);
                    btTop.setVisibility(View.VISIBLE);
                }
                else if (count == 2) {
                    Toast.makeText(Record.this,"Count = 2",Toast.LENGTH_SHORT).show();
                }
                count++;
            }
        });

        btMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Record.this)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Record.this,"Map Cleared",Toast.LENGTH_SHORT).show();
                                btTop.setVisibility(View.GONE);
                                btMiddle.setVisibility(View.GONE);
                                btMsg = "Start";
                                btBottom.setText(btMsg);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Record.this,"Not Gonna Clear",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        btTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btTop.setVisibility(View.GONE);
                btMiddle.setVisibility(View.GONE);
                btMsg = "Pause/Stop";
                btBottom.setText(btMsg);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showGPSPermissions();
    }

    private void showGPSPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(Record.this, "GPS Required for this App",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_GPS);
            }
        }
        else {
            Toast.makeText(Record.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (!recordIndicator) {
                        LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                        if (startMarker != null) {
                            startMarker.remove();
                        }
                        startMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentLocation)
                            .title("Start Position"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                    }
                    else {
                        LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                        if (endMarker != null) {
                            endMarker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                        endMarker = mMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title("Start Position"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Record.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Record.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

}
