package com.rowdy.marvinlopez.applicationrowdymaps;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONObject;

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
    static Polyline route;
    SessionManager session;
    String[] friends = new String[20];
    String[][] friends2 = new String[20][3];
    //String[] friends = {"Marvin", "Darren","Jonathan"};
    //String[] friends = {"", ""};
    //String[][] friends2 = {{"","",""},{"","",""}};
    //String[][] friends2 = {{"Marvin","29.584422","-98.617395"},{ "Darren","29.581707","-98.617471"},{"Jonathan","29.581971","-98.623123"}};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_select);

        for(int ind = 0; ind < friends.length; ind++){
            friends[ind] = "";
            friends2[ind][0] = "";
            friends2[ind][1] = "";
            friends2[ind][2] = "";

        }
        friends2[0][0] = "";

        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn()) {
            grabFromDatabase();
        }

        ListAdapter friendAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friends);
        ListView friendListView = (ListView) findViewById(R.id.friendview);
        friendListView.setAdapter(friendAdapter);

        friendListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(route != null){
                            route.remove();
                        }

                        friend = String.valueOf(parent.getItemAtPosition(position));
                       for(int i =0;i<friends2.length;i++) {
                           if ((friends2[i][0].equals(friend))) {
                               //Toast.makeText(friendselect.this,friends2[i][0],Toast.LENGTH_LONG).show();
                               double lt = Double.parseDouble(friends2[i][1]);
                               double lo = Double.parseDouble(friends2[i][2]);
                               buildingpoint = new LatLng(lt,lo);
                              // Toast.makeText(friendselect.this,String.valueOf(buildingpoint),Toast.LENGTH_LONG).show();


                               if(marker != null)
                                    marker.remove();
                                 MarkerOptions markerOptions = new MarkerOptions().position(buildingpoint).title(friend);
                                 marker = MainActivity.mMap.addMarker(markerOptions);
                                 route = MainActivity.mMap.addPolyline(new PolylineOptions().add( MainActivity.person,buildingpoint ).width(5).color(Color.BLUE).geodesic(true));


                           }
                       }finish();
                }});

    }

    private void grabFromDatabase(){
        try{
            //Pulling friends ID's
            authenticateTask thing = new authenticateTask();
            String resultSession = thing.execute("").get();
            String resultSalt = thing.getSalt();
            userTask friends = new userTask();

            String friendsQuery;
            friendsQuery = "SELECT friend_one, friend_two FROM Friends WHERE (friend_one = '" + session.getUserId() +
                    "' OR friend_two = '" + session.getUserId() + "')";

            String friendsResult = friends.execute(resultSession, friendsQuery, resultSalt).get();
            Log.d("friends result", friendsResult);

            JSONArray friendsResponse = new JSONArray(friendsResult);
            JSONObject friendsMessage = friendsResponse.getJSONObject(1);
            JSONArray friendsArr = friendsMessage.getJSONArray("message");

            List<Integer> friendIds = new ArrayList<>();
            for(int x = 0; x < friendsArr.length();x++){
                JSONArray temp = friendsArr.getJSONArray(x);
                int one = temp.getJSONObject(0).getInt("friend_one");
                int two = temp.getJSONObject(1).getInt("friend_two");
                if(one == session.getUserId()){
                    friendIds.add(two);
                } else {
                    friendIds.add(one);
                }
            }
            Log.d("list of friend id", " " + friendIds.toString());


            //Pulling friend usernames and locations
            String getFriends;
            getFriends = "SELECT userName, lat, lon FROM Users WHERE (userId = '" + friendIds.get(0) +
                    "')";
            for(int x = 1; x < friendIds.size(); x++){
                getFriends = getFriends + " OR (userId = '" + friendIds.get(x) + "')";
            }
            Log.d("friends info query", getFriends);
            userTask getFriendInfo = new userTask();
            String friendsInfo = getFriendInfo.execute(resultSession, getFriends, resultSalt).get();
            Log.d("friends info result", friendsInfo);

            JSONArray friendsInfoResponse = new JSONArray(friendsInfo);
            JSONObject friendsInfoMessage = friendsInfoResponse.getJSONObject(1);
            JSONArray friendsInfoArr = friendsInfoMessage.getJSONArray("message");
            Log.d("friends info arr", friendsInfoArr.toString());
            for(int x = 0; x < friendsInfoArr.length(); x++){
                JSONArray temp = friendsInfoArr.getJSONArray(x);
                Log.d("Friend", temp.toString());
                String username = temp.getJSONObject(0).getString("userName");
                double lat = temp.getJSONObject(1).getDouble("lat");
                double lon = temp.getJSONObject(2).getDouble("lon");
                Log.d("friend info", "" + username  + "  " + lat + " " + lon);
                this.friends[x] = username;
                this.friends2[x][0] = username;
                this.friends2[x][1] = "" + lat;
                this.friends2[x][2] = "" + lon;
            }

        } catch (Exception e){
            Log.d("err", "building failed");
        }
    }

}