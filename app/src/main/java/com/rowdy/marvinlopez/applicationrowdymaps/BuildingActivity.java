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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class BuildingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        String[] buildings = {"MS Multidiscplinary Studies Building","BB Business Building","JPL John Peace Library","MH McKinney Humanties Building","FLN Flawn Sciences Building","ART Arts Building","BSB BioSciences Bulding","EB Engineering Building","BSE BioTechnology Sciences & Engineering","AET Applied Engineering and Technology","MB Main Building","NPB North Paseo Building","PNB Plaza North Building","UC University Center","HUC HEB University Center","CC Convocation Center","PE Physical Education Building","RWC Recreation Wellness Center"};
        ListAdapter buildingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,buildings);
        ListView buildingListView = (ListView) findViewById(R.id.buildingListView);
        buildingListView.setAdapter(buildingAdapter);
    }
}
