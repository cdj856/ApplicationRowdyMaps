package com.rowdy.marvinlopez.applicationrowdymaps;

/**
 * Created by marvinlopez on 10/26/16.
 */
import android.content.Intent;
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

        final String[] buildings = {"MS Multidiscplinary Studies Building","BB Business Building","JPL John Peace Library","MH McKinney Humanties Building","FLN Flawn Sciences Building","ART Arts Building","BSB BioSciences Bulding","EB Engineering Building","BSE BioTechnology Sciences & Engineering","AET Applied Engineering and Technology","MB Main Building","NPB North Paseo Building","PNB Plaza North Building","UC University Center","HUC HEB University Center","CC Convocation Center","PE Physical Education Building","RWC Recreation Wellness Center","AC Activity Center","BOS Bosque Street Building","BSA Business Services Annex","CAR Center for Archaeological Research","CDC Child Development Center","CRW Central Receiving & Warehouse","FSB Facilities Services Building","GSR Graduate School & Research Building","MBT Margaret Batts Tobin Laboratories","MEM MEMS Lab","PDS Power & Dynamics Systems Lab","SCG Sculpture & Ceramics Graduate Studio","SEL Science and Engineering Lab","SRL Science Research Laboratories","TEP Thermal Energy Plant"};
        final String[][] points = {{"MS Multidiscplinary Studies Building","29.583465","-98.619039"},{"BB Business Building","29.584860","-98.618490"},{"JPL John Peace Library","29.584295","-98.617800"},{"MH McKinney Humanties Building","29.584321","-98.619001"},{"FLN Flawn Sciences Building","29.583092","-98.618255"},{"ART Arts Building","29.583348","-98.617796"},{"BSB BioSciences Bulding","29.582154","-98.618486"},{"EB Engineering Building","29.582330","-98.617831"},{"BSE BioTechnology Sciences & Engineering","29.581774","-98.617457"},{"AET Applied Engineering and Technology","29.581219","-98.617843"},{"MB Main Building","29.584722","-98.616840"},{"NPB North Paseo Building","29.585788","-98.619595"},{"PNB Plaza North Building","29.585528","-98.620314"},{"UC University Center","29.583999","-98.620370"},{"HUC HEB University Center","29.582992","-98.620204"},{"CC Convocation Center","29.582390","-98.621604"},{"PE Physical Education Building","29.582982","-98.621926"},{"RWC Recreation Wellness Center","29.581401","-98.622599"},{"AC Activity Center","29.585435","-98.625596"},{"BOS Bosque Street Building","29.585034","-98.621225"},{"BSA Business Services Annex","29.582718","-98.629163"},{"CAR Center for Archaeological Research","29.582577","-98.629945"},{"CDC Child Development Center","29.579157","-98.627806"},{"CRW Central Receiving & Warehouse","29.583102","-98.630244"},{"FSB Facilities Services Building","29.581724","-98.630044"},{"GSR Graduate School & Research Building","29.585791","-98.620490"},{"MBT Margaret Batts Tobin Laboratories","29.584230","-98.629638"},{"MEM MEMS Lab","29.583389","-98.628244"},{"PDS Power & Dynamics Systems Lab","29.583490","-98.628113"},{"RRC Roadrunner Café", "29.585699","-98.624658"},{"SCG Sculpture & Ceramics Graduate Studio","29.582809","-98.628350"},{"SEL Science and Engineering Lab","29.583592","-98.627987"},{"SRL Science Research Laboratories","29.583852","-98.629172"},{"TEP Thermal Energy Plant","29.584632","-98.621304"}};
        final String[][] obstacles = {{"MS Multidiscplinary Studies Building","29.583980","-98.619059","29.583280","-98.618583","29.582988","-98.619152","29.583695","-98.619643"},{"BB Business Building","29.585064","-98.617533","29.584670","-98.618311","29.585190","-98.618628","29.585568","-98.617882"},{"JPL John Peace Library","29.584930","-98.617416","29.584238","-98.616871","29.583753","-98.618267","29.584236","-98.618596"},{"MH McKinney Humanties Building","29.584895","-98.619011","29.584467","-98.618502","29.583956","-98.619508","29.584513","-98.619765"},{"FLN Flawn Sciences Building","29.582763","-98.617875","29.582317","-98.618602","29.582865","-98.618905","29.583304","-98.618167"},{"ART Arts Building","29.583082","-98.616777","29.582867","-98.617578","29.583385","-98.618018","29.583660","-98.617492"},{"BSB BioSciences Bulding","29.581899","-98.618103","29.581824","-98.618344","29.582179","-98.618698","29.582328","-98.618414"},{"EB Engineering Building","29.582557","-98.617747","29.582265","-98.617551","29.582020","-98.618028","29.582305","-98.618224"},{"BSE BioTechnology Sciences & Engineering","29.581319","-98.617010","29.581179","-98.617310","29.581918","-98.617953","29.582081","-98.617481"},{"AET Applied Engineering and Technology","29.580951","-98.617458","29.580828","-98.617738","29.581607","-98.618280","29.581740","-98.617991"},{"MB Main Building","29.584925","-98.617385","29.585312","-98.616529","29.584659","-98.616089","29.584116","-98.617138"},{"NPB North Paseo Building","29.585589","-98.619195","29.585395","-98.619576","29.586138","-98.620080","29.586335","-98.619699"},{"PNB Plaza North Building","29.585251","-98.619974","29.585130","-98.620215","29.585860","-98.620768","29.586141","-98.620234"},{"UC University Center","29.583385","-98.620613","29.583562","-98.621233","29.584579","-98.620061","29.583798","-98.619822"},{"HUC HEB University Center","29.582515","-98.620141","29.583127","-98.620634","29.583533","-98.619762","29.582656","-98.619853"},{"CC Convocation Center","29.582398","-98.620923","29.581972","-98.621751","29.582373","-98.622003","29.582788","-98.621190"},{"PE Physical Education Building","29.583001","-98.621366","29.582840","-98.621712","29.583237","-98.621889","29.583370","-98.621621"},{"RWC Recreation Wellness Center","29.581413","-98.622165","29.580513","-98.622682","29.581227","-98.623873","29.582277","-98.622945"},{"AC Activity Center","29.585347","-98.625303","29.585139","-98.625704","29.585461","-98.625924","29.585668","-98.625519"},{"BOS Bosque Street Building","29.585074","-98.620907","29.584823","-98.621392","29.585015","-98.621517","29.585258","-98.621038"},{"BSA Business Services Annex","29.582642","-98.628917","29.582494","-98.629081","29.582754","-98.629406","29.582908","-98.629242"},{"CAR Center for Archaeological Research","29.582573","-98.629657","29.582291","-98.629949","29.582550","-98.630290","29.582832","-98.629990"},{"CDC Child Development Center","29.578953","-98.627556","29.579056","-98.628056","29.579211","-98.628063","29.579333","-98.627561"},{"CRW Central Receiving & Warehouse","29.583221","-98.629994","29.582889","-98.630348","29.583056","-98.630558","29.583388","-98.630207"},{"FSB Facilities Services Building","29.581328","-98.630306","29.581491","-98.630518","29.582104","-98.629955","29.581857","-98.629732"},{"GSR Graduate School & Research Building","29.585243","-98.619971","29.585119","-98.620218","29.585863","-98.620771","29.585863","-98.620771"},{"MBT Margaret Batts Tobin Laboratories","29.584540","-98.629536","29.584427","-98.629244","29.583888","-98.629808","29.584072","-98.630027"},{"MEM MEMS Lab","29.583346","-98.628085","29.583247","-98.628201","29.583433","-98.628425","29.583532","-98.628312"},{"PDS Power & Dynamics Systems Lab","29.583437","-98.627918","29.583341","-98.628020","29.583558","-98.628288","29.583654","-98.628182"},{"RRC Roadrunner Café", "29.585722","-98.625054","29.585962","-98.624440","29.585719","-98.624244","29.585411","-98.624858"},{"SCG Sculpture & Ceramics Graduate Studio","29.582605","-98.628455","29.582796","-98.628627","29.583050","-98.628175","29.582831","-98.628027"},{"SEL Science and Engineering Lab","29.583539","-98.627804","29.583447","-98.627903","29.583666","-98.628173","29.583759","-98.628073"},{"SRL Science Research Laboratories","29.583501","-98.628980","29.583878","-98.629696","29.584340","-98.629219","29.583682","-98.628801"},{"TEP Thermal Energy Plant","29.584842","-98.621144","29.584634","-98.620999","29.584413","-98.621438","29.584628","-98.621583"}};

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
                        startActivity(new Intent(BuildingActivity.this,MainActivity.class));





                        finish();

                    }

                }
        );
    }
}
