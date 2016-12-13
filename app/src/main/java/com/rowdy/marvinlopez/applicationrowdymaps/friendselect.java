package com.rowdy.marvinlopez.applicationrowdymaps;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by marvinlopez on 12/13/16.
 */

public class friendselect extends AppCompatActivity {
    String friend;
    static Marker marker;
    static LatLng buildingpoint;// =new LatLng(29.584493, -98.618944);
    static double lt,lo;
    static Polyline route3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_select);

        final String[] friends = {"Marvin", "Darren","Jonathan"};
        final String[][] friends2 = {{"Marvin","29.584422","-98.617395"},{ "Darren","29.581707","-98.617471"},{"Jonathan","29.581971","-98.623123"}};

        ListAdapter friendAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friends);
        ListView friendListView = (ListView) findViewById(R.id.friendview);
        friendListView.setAdapter(friendAdapter);
        if(BuildingActivity.route1 != null){
            BuildingActivity.route1.remove();
            BuildingActivity.markerB.remove();
        }

        friendListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        friend = String.valueOf(parent.getItemAtPosition(position));
                       for(int i =0;i<friends2.length;i++) {
                           if ((friends2[i][0].equals(friend))) {
                               //Toast.makeText(friendselect.this,friends2[i][0],Toast.LENGTH_LONG).show();
                               double lt = Double.parseDouble(friends2[i][1]);
                               double lo = Double.parseDouble(friends2[i][2]);
                               buildingpoint = new LatLng(lt,lo);
                              // Toast.makeText(friendselect.this,String.valueOf(buildingpoint),Toast.LENGTH_LONG).show();


                               if(marker != null)
                                    route3.remove();
                                 MarkerOptions markerOptions = new MarkerOptions().position(buildingpoint).title(friend);
                                 marker = MainActivity.mMap.addMarker(markerOptions);
                                 route3 = MainActivity.mMap.addPolyline(new PolylineOptions().add( MainActivity.person,buildingpoint ).width(5).color(Color.BLUE).geodesic(true));


                           }
                       }finish();
                }});

    }

}