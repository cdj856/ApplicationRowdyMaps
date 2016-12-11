package com.rowdy.marvinlopez.applicationrowdymaps;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,LocationListener {
    static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
    private GoogleApiClient mGoogleApiClient;
    protected TextView mLatitudeText ;
    protected TextView mLongitudeText ;
    static GoogleMap mMap;
    private Polyline route;
    static Marker marker;
    static LatLng buildingpoint =new LatLng(29.584493, -98.618944);  //29.583844, -98.618608);
    static LatLng person;
    static LatLng myloc;
    View mapView;
    protected Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    //LocationRequest mLocationRequest;
    //LocationClient mLocationClient;
    Location mCurrentLocation;
    static double lt,lo;

    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());

        if (mGoogleApiClient == null) { //mGoogleApiClient
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
       // buildGoogleApiClient();
       // mGoogleApiClient.connect();

         // added map code before here
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this); // to call onmap
        mLatitudeText = (TextView) findViewById((R.id.textview));
        mLongitudeText = (TextView) findViewById((R.id.textview));

        if(session.isLoggedIn()){
            Toast.makeText(this,"Logged in as " + session.getUserDetails(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"onOptionsItemSelected",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_building) {
            // Handle the nav_building action
            Intent i = new Intent(
                    MainActivity.this,
                    BuildingActivity.class);
            startActivity(i);
            //Toast toast = Toast.makeText(this, "Building list would show", Toast.LENGTH_SHORT);
            //toast.show();
        } else if (id == R.id.nav_routes) {
            Toast toast = Toast.makeText(this, "Turned ON Accessible Routes", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.nav_gps) {
            onStop();
            Toast toast = Toast.makeText(this, "GPS OFF", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.nav_fiends) {
            //Go to freinds screen
            Intent i = new Intent(
                    MainActivity.this,
                    FriendsActivity.class);
            startActivity(i);
            //Toast toast = Toast.makeText(this, "You have no friends...Sorry", Toast.LENGTH_SHORT);
            //toast.show();
        } else if (id == R.id.nav_login) {
            Intent i = new Intent(
                    MainActivity.this,
                    LoginActivity.class);

            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //@Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        //Add a marker in utsa and move the camera
        if(myloc == null) {
            person = buildingpoint;//new LatLng(29.584493, -98.618944);
        }else{
            person = myloc;
        }
       // LatLng current = new LatLng(mLatitudeText,mLongitudeText);

        //Add variables

       //mMap.addMarker(new MarkerOptions().position(person).title("you are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(person));
       // mapView.
       // mMap.moveCamera(CameraUpdateFactory.zoomIn());
        mMap.setMinZoomPreference(20.0F);
        mMap.setMaxZoomPreference(17.0f);

        if(this.mMap != null && buildingpoint.equals(person)==false){
            bPoint(buildingpoint);
            route = googleMap.addPolyline(new PolylineOptions().add( person, buildingpoint).width(5).color(Color.BLUE).geodesic(true));
            //Toast.makeText(this,"working------",Toast.LENGTH_LONG).show();
        }

        /*if (mGoogleApiClient == null) { //mGoogleApiClient
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();*/
       if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
            return;
        }
        mMap.setMyLocationEnabled(true);


    }

    //added mylocation code here onStart() and onStop()
    protected void onStart() {
        //Toast.makeText(this,"testTESTOnStart",Toast.LENGTH_LONG).show();
        super.onStart();
        mGoogleApiClient.connect();
        //super.onStart();
    }

    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        //super.onStop();

    }


    @Override
    public void onConnected(Bundle connectionHint) {
       // Toast.makeText(this,"testTESTOnConnected",Toast.LENGTH_LONG).show();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
           if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

           }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


            }
            return;
        }
        //mMap.setMyLocationEnabled(true);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
/*
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Toast.makeText(this,"LocationServices done",Toast.LENGTH_LONG).show();
        }
        if(mGoogleApiClient.isConnected()==true){
            Toast.makeText(this,"mGoogleApiClient is connected",Toast.LENGTH_LONG).show();
        }

        //here code working but mLastLocation == Null----------------------------------------------------------------

        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        //Toast.makeText(this,permissionCheck,Toast.LENGTH_LONG).show();
        if(permissionCheck == 0){
            Toast.makeText(this,"granted",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"not granted",Toast.LENGTH_LONG).show();
        }
*/
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //mLastLocation.describeContents();
        //Toast.makeText(this,"onConnected",Toast.LENGTH_LONG).show();
        if(mLastLocation != null){
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            lt= mLastLocation.getLatitude();
           // Toast.makeText(this, String.valueOf(lt),Toast.LENGTH_LONG).show();
            lo = mLastLocation.getLongitude();
            //Toast.makeText(this, String.valueOf(lo),Toast.LENGTH_LONG).show();
            myloc = new LatLng(lt, lo);
            mMap.addMarker(new MarkerOptions().position(myloc).title("you are here"));
        }else{
           // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Toast.makeText(this,"LocationServices done",Toast.LENGTH_LONG).show();
            Toast.makeText(this,"mLastLocation==NULL",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    private void bPoint(LatLng latLng){
        //Geocoder geocoder = new Geocoder(this);
        if(marker != null)
            marker.remove();
        MarkerOptions markerOptions = new MarkerOptions().position(buildingpoint);
        marker = mMap.addMarker(markerOptions);

    }

    @Override
    public void onLocationChanged(Location location) {
       /* mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
*/
    }



   /* protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }*/


}
