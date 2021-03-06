package com.rowdy.marvinlopez.applicationrowdymaps;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.app.Dialog;

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
    static PolylineOptions polylineOptions = new PolylineOptions();
    static Marker marker, marker2;
    static LatLng buildingpoint =new LatLng(29.584493, -98.618944);
    static LatLng person;
    static LatLng jpl2; //= new LatLng(29.584248,-98,618562);
    static LatLng jpl1;
    static LatLng myloc;
    View mapView;
    protected Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    Location mCurrentLocation;
    static double lt,lo;
    //static int pina = 0;
    static String curbuilding;
    private ImageView mimageView;

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
            Toast.makeText(this,"Logged in as " + session.getUsername(),Toast.LENGTH_LONG).show();
            //Toast.makeText(this,"Logged in as " + session.getUserDetails(),Toast.LENGTH_LONG).show();
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
        /*} else if (id == R.id.floor_plans) {
            Toast toast = Toast.makeText(this, "Turned ON Accessible Routes", Toast.LENGTH_SHORT);
            toast.show();*/
        } else if (id == R.id.nav_routes) {
            Toast toast = Toast.makeText(this, "Display Floor Plans", Toast.LENGTH_SHORT);
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
        } else if(id == R.id.nav_logout){
            Toast toast = Toast.makeText(this, "Logout", Toast.LENGTH_SHORT);
            if(session.isLoggedIn()){
                String name = session.getUsername();
                session.logoutUser();
                toast = Toast.makeText(this, "Logging out " + name, Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT);
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
            toast.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //@Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
       // final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();


        jpl1 = new LatLng(29.584248,-98.618562);
        jpl2 = new LatLng(29.584388, -98.617039);

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


        MarkerOptions markerOptions2 = new MarkerOptions().position(buildingpoint);
        marker2 = mMap.addMarker(markerOptions2);
        //polylineOptions.addAll(BuildingActivity.paray);
        //polylineOptions.width(5).color(Color.BLUE);
        /*if(this.mMap != null && buildingpoint.equals(person)==false){
            bPoint(buildingpoint);
            if(pina != 0 ) {
                //pointstobuild = Integer.parseInt(mappingroutearray[pina][1]);
                Toast.makeText(this, String.valueOf(pina), Toast.LENGTH_LONG).show();
                Toast.makeText(this, String.valueOf(pointstobuild), Toast.LENGTH_LONG).show();*/
                route = googleMap.addPolyline(new PolylineOptions().add(jpl1,jpl2 ).visible(false));
                route = googleMap.addPolyline(new PolylineOptions().add(new LatLng(29.584004,-98.617714),new LatLng(29.584810,-98.617525) ).visible(false).width(200));
                route.setClickable(true);
                //route.isClickable();

        googleMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener()
        {
            @Override
            public void onPolylineClick(Polyline route)
            {

                final AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
              //  final AlertDialog.Builder alertadd1 = new AlertDialog.Builder(MainActivity.this);
                //final AlertDialog.Builder alertadd2 = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View view = factory.inflate(R.layout.sample, null);
                final View view1 = factory.inflate(R.layout.sample2,null);
                final View view2 = factory.inflate(R.layout.sample3,null);

                alertadd.setView(view);
                //final AlertDialog alertDialog = alertadd.show();

                alertadd.setPositiveButton("4 floor", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
                        alertadd.setView(view2);
                        alertadd.show();

                    }
                });
                alertadd.setNegativeButton("3 floor", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
                        alertadd.setView(view1);
                        alertadd.show();

                    }
                });
               alertadd.show();



            }
        });
        /*


            }
            for(int i = 0; i < pina;i++){
                double point1 = Double.parseDouble(mappingroutearray[pina][i+2]);
                double point2 = Double.parseDouble(mappingroutearray[pina][i+3]);
                LatLng tmpp = new LatLng(point1,point2);
                MarkerOptions markerOptions = new MarkerOptions().position(tmpp);
                marker = mMap.addMarker(markerOptions);
              //  route = googleMap.addPolyline(new PolylineOptions().add( person, tmpp).width(5).color(Color.BLUE).geodesic(true));
               // person = tmpp;
            /

            //route = googleMap.addPolyline(new PolylineOptions().add( person, buildingpoint).width(5).color(Color.BLUE).geodesic(true));
            //Toast.makeText(this,"working------",Toast.LENGTH_LONG).show();
        }*/


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
       /* if(marker != null)
            marker.remove();
        MarkerOptions markerOptions = new MarkerOptions().position(buildingpoint).title("--------------");
        marker = mMap.addMarker(markerOptions);*/

    }



    @Override
    public void onLocationChanged(Location location) {

    }





}
