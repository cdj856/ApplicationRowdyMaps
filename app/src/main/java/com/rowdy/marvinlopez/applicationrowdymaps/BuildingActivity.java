package com.rowdy.marvinlopez.applicationrowdymaps;

/**
 * Created by marvinlopez on 10/26/16.
 */
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

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class BuildingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        final String[] buildings = {"MS Multidiscplinary Studies Building","BB Business Building","JPL John Peace Library","MH McKinney Humanties Building","FLN Flawn Sciences Building","ART Arts Building","BSB BioSciences Bulding","EB Engineering Building","BSE BioTechnology Sciences & Engineering","AET Applied Engineering and Technology","MB Main Building","NPB North Paseo Building","PNB Plaza North Building","UC University Center","HUC HEB University Center","CC Convocation Center","PE Physical Education Building","RWC Recreation Wellness Center"};
        final String[][] points = {{"MS Multidiscplinary Studies Building","29.583465","-98.619039"},{"BB Business Building","29.584860","-98.618490"},{"JPL John Peace Library","29.584295","-98.617800"},{"MH McKinney Humanties Building","29.584321","-98.619001"},{"FLN Flawn Sciences Building","29.583092","-98.618255"},{"ART Arts Building","29.583348","-98.617796"},{"BSB BioSciences Bulding","29.582154","-98.618486"},{"EB Engineering Building","29.582330","-98.617831"},{"BSE BioTechnology Sciences & Engineering","29.581774","-98.617457"},{"AET Applied Engineering and Technology","29.581219","-98.617843"},{"MB Main Building","29.584722","-98.616840"},{"NPB North Paseo Building","29.585788","-98.619595"},{"PNB Plaza North Building","29.585528","-98.620314"},{"UC University Center","29.583999","-98.620370"},{"HUC HEB University Center","29.582992","-98.620204"},{"CC Convocation Center","29.582390","-98.621604"},{"PE Physical Education Building","29.582982","-98.621926"},{"RWC Recreation Wellness Center","29.581401","-98.622599"}};
        ListAdapter buildingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,buildings);
        ListView buildingListView = (ListView) findViewById(R.id.buildingListView);
        buildingListView.setAdapter(buildingAdapter);

        buildingListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        double lng = 0;
                        double lat = 0;
                        String temp ="";


                        String building = String.valueOf(parent.getItemAtPosition(position));
                        for(int i = 0; i < points.length;i++) {
                            if ((points[i][0].equals(building))) {
                                temp = points[i][1]+", "+points[i][2];
                                lng = Double.parseDouble(points[i][1]);
                                lat = Double.parseDouble(points[i][2]);
                                //Toast.makeText(BuildingActivity.this, points[i][1], Toast.LENGTH_LONG).show();
                            }

                        }
                        //LatLng buildingpoint = new LatLng(29.583844, -98.618608);

                        Toast.makeText(BuildingActivity.this,temp,Toast.LENGTH_LONG).show();
                            MainActivity.buildingpoint = new LatLng(lng, lat);



                        finish();

                    }

                }
        );
    }
}
