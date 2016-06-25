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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.fitness.data.Application;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

public class Record extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int REQUEST_PERMISSION_GPS = 1;
    private int count = 0;
    private boolean recordIndicator = false;
    private TextView tvAccuracy, tvDistance, tvTime;
    private Button btTop, btMiddle, btBottom;
    private ArrayList<ArrayList<LatLng>> polylinePointsList = new ArrayList<ArrayList<LatLng>>();
    private ArrayList<Polyline> polylineArrayList = new ArrayList<Polyline>();
    private ArrayList<Marker> markerArrayList = new ArrayList<Marker>();
    private String markerTitle;
    private int noOfPolylines;

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

        btTop = (Button) findViewById(R.id.btTop);
        btMiddle = (Button) findViewById(R.id.btMiddle);
        btBottom = (Button) findViewById(R.id.btBottom);
        tvAccuracy = (TextView) findViewById(R.id.tvAccuracy);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvTime = (TextView) findViewById(R.id.tvTime);

        Toast.makeText(Record.this,"Please Wait while we pinpoint your location",Toast.LENGTH_SHORT).show();
        changeToState1();

        btBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    changeToState2();
                }
                else if (count == 1) {
                    changeToState3();
                }
                else if (count == 2) {
                    Toast.makeText(Record.this,"Count = 2",Toast.LENGTH_SHORT).show();
                }
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
                                changeToState1();
                                Toast.makeText(Record.this,"Map Cleared",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Record.this,"Run not cleared",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        btTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToState2();
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
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String msg = "Accuracy: " + location.getAccuracy();
                    tvAccuracy.setText(msg);
                    if (!recordIndicator) {
                        LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                        if (noOfPolylines < markerArrayList.size()) {
                            markerArrayList.get(noOfPolylines).remove();
                            Marker newMarker = mMap.addMarker(new MarkerOptions()
                                    .position(currentLocation)
                                    .title(markerTitle));
                            markerArrayList.set(noOfPolylines,newMarker);
                        }
                        else {
                            Marker newMarker = mMap.addMarker(new MarkerOptions()
                                    .position(currentLocation)
                                    .title(markerTitle));
                            markerArrayList.add(noOfPolylines,newMarker);
                        }
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(25));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    }
                    else {
                        LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                        if (noOfPolylines < (markerArrayList.size() - 1)) {
                            markerArrayList.get(noOfPolylines + 1).remove();
                            Marker newMarker = mMap.addMarker(new MarkerOptions()
                                    .position(currentLocation)
                                    .title(markerTitle));
                            markerArrayList.set(noOfPolylines + 1, newMarker);
                        }
                        else {
                            Marker newMarker = mMap.addMarker(new MarkerOptions()
                                    .position(currentLocation)
                                    .title(markerTitle));
                            markerArrayList.add(noOfPolylines + 1, newMarker);
                        }
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(25));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        drawPolyline();
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

    public void drawPolyline() {
        Marker startMarker, endMarker;
        Polyline polyline;
        ArrayList<LatLng> points;
        if (noOfPolylines < polylinePointsList.size()) {
            points = polylinePointsList.get(noOfPolylines);
        }
        else {
            points = new ArrayList<LatLng>();
        }
        if (noOfPolylines < markerArrayList.size() && noOfPolylines < polylinePointsList.size()) {
            startMarker = markerArrayList.get(noOfPolylines);
            startMarker.setTitle("Start Position");
            LatLng startPoint = new LatLng(startMarker.getPosition().latitude, startMarker.getPosition().longitude);
            points.add(startPoint);
        }
        if (noOfPolylines < (markerArrayList.size() - 1) && noOfPolylines < polylinePointsList.size()) {
            endMarker = markerArrayList.get(noOfPolylines + 1);
            LatLng endPoint = new LatLng(endMarker.getPosition().latitude, endMarker.getPosition().longitude);
            points.add(endPoint);
        }
        if (noOfPolylines < polylineArrayList.size()) {
            polyline = polylineArrayList.get(noOfPolylines);
            polyline.remove();
        }
//        else {
//            LatLng startPoint = new LatLng(startMarker.getPosition().latitude, startMarker.getPosition().longitude);
//            polylinePoints.add(startPoint);
//        }
//        LatLng endPoint = new LatLng(endMarker.getPosition().latitude, endMarker.getPosition().longitude);
//        polylinePoints.add(endPoint);
        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            polylineOptions.add(point);
        }
        polyline = mMap.addPolyline(polylineOptions);
        if (noOfPolylines < polylineArrayList.size()) {
            polylineArrayList.set(noOfPolylines, polyline);
        }
        else {
            polylineArrayList.add(noOfPolylines, polyline);
        }
        if (noOfPolylines < polylinePointsList.size()) {
            polylinePointsList.set(noOfPolylines, points);
        }
        else {
            polylinePointsList.add(noOfPolylines, points);
        }
    }

    public void changeToState1() {
        markerTitle = "Current Position";
        recordIndicator = false;
        count = 0;
        noOfPolylines = 0;
        polylinePointsList.clear();
        polylineArrayList.clear();
        markerArrayList.clear();
        String btMsg = "Start";
        btBottom.setText(btMsg);
        btTop.setVisibility(View.GONE);
        btMiddle.setVisibility(View.GONE);
        if (mMap != null) {
            mMap.clear();
        }
    }

    public void changeToState2() {
        markerTitle = "End Position";
        recordIndicator = true;
        count = 1;
//        noOfPolylines++;
        polylinePointsList.add(new ArrayList<LatLng>());
        String btMsg = "Pause/Stop";
        btBottom.setText(btMsg);
        btTop.setVisibility(View.GONE);
        btMiddle.setVisibility(View.GONE);
    }

    public void changeToState3() {
        markerTitle = "Current Position";
        recordIndicator = false;
        count = 2;
        noOfPolylines++;
        String btMsg = "Save";
        btBottom.setText(btMsg);
        btMiddle.setVisibility(View.VISIBLE);
        btTop.setVisibility(View.VISIBLE);
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
