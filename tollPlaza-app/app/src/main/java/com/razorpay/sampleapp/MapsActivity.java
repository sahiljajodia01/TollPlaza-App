package com.razorpay.sampleapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Double d1, d2, d3;
    LatLng ll1 , ll2, ll3;
    Button b;
    CalculateDistance calculateDistance = new CalculateDistance();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                }



            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        b = (Button) findViewById(R.id.button1);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        Marker m1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.787844, -122.391658))
                .title("Tool Booth1"));

        Marker m2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.783740, -122.394224))
                .title("Title2"));

        Marker m3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.788118, -122.388762))
                .title("Title3"));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                Log.i("LOCATION",location.toString());

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLocation).title("YourLocation")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                ll1 = new LatLng(37.787844, -122.391658);
                ll2 = new LatLng(37.783740, -122.394224);
                ll3 = new LatLng(37.788118, -122.388762);
                mMap.addMarker(new MarkerOptions()
                        .position(ll1)
                        .title("Tool Booth1"));
                mMap.addMarker(new MarkerOptions()
                        .position(ll2)
                        .title("Tool Booth2"));
                mMap.addMarker(new MarkerOptions()
                        .position(ll3)
                        .title("Tool Booth3"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,0));
                Log.i("Location",location.toString());
                d1 = calculateDistance.CalculationByDistance(userLocation, ll1);
                Log.i("d1",d1.toString());
                d2 = calculateDistance.CalculationByDistance(userLocation, ll1);
                d3 = calculateDistance.CalculationByDistance(userLocation, ll1);
                if (d1 <= 500 || d2 <= 500 || d3 <= 500)
                    //Toast.makeText(MapsActivity.this, "Danger", Toast.LENGTH_SHORT).show();
                    b.setVisibility(View.VISIBLE);
                else
                    b.setVisibility(View.INVISIBLE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(MapsActivity.this, PaymentActivity.class);
                        startActivity(in);
                    }
                });
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

        if(Build.VERSION.SDK_INT < 23) {

            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }
        else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());


                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLocation).title("YourLocation").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));

            }
        }
    }



}