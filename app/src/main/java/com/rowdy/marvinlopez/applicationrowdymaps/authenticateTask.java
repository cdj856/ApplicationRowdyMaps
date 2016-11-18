package com.rowdy.marvinlopez.applicationrowdymaps;

import java.net.*;
import java.io.*;

import android.os.AsyncTask;
import android.util.Log;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.*;
import java.security.*;
import java.util.*;
import java.io.StringWriter;


//import org.apache.http.client.*;



/**
 * Created by jonathan on 11/14/2016.
 */

public class authenticateTask extends AsyncTask<String, String, String> {
    private String salt = "";
    private String session_id = "0";

        @Override
        protected String doInBackground(String...insert) {
            Log.d("task err", "" + insert[0]);
            try {

                /***********************************************************************************
                 * For ease of editing, im putting the json stuff here and connection stuff below
                 **********************************************************************************/


                String password = "tPP3M2cBQsMUsVjq";

                String encodedPassword = Encoder.encode(password);

                //Creating JSON array of JSON objects
                JSONArray arrLogin = new JSONArray();
                JSONObject actLog = new JSONObject();
                actLog.put("action", "login");
                JSONObject login = new JSONObject();
                login.put("login", "fa2016_madmappers");
                JSONObject pass = new JSONObject();
                pass.put("password", encodedPassword);
                JSONObject app = new JSONObject();
                app.put("app_code", "fpcwvmCthCnAbvs5");
                JSONObject sessType = new JSONObject();
                sessType.put("session_type", "id_salt");

                //Add these to JSON array before checksum (they are required to get checksum)
                arrLogin.put(actLog);
                arrLogin.put(login);
                arrLogin.put(pass);
                arrLogin.put(app);
                arrLogin.put(sessType);

                String newChecksum = Encoder.encode(arrLogin.toString());

                //Create JSON object for the calculated checksum and append that to the array
                JSONObject checksumLogin = new JSONObject();
                checksumLogin.put("checksum", newChecksum);
                arrLogin.put(checksumLogin);

                Log.d("Task err1", arrLogin.toString());

                /***********************************************************************************
                 * Connection properties here
                 **********************************************************************************/


                //Setting connection and method
                URL url = new URL("https://easel1.fulgentcorp.com/bifrost/ws.php?json=" + arrLogin.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");


                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(URLEncoder.encode(arrLogin.toString(),"UTF-8"));
                wr.flush();

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    //br.close();
                    Log.d("response err", "" + sb.toString());
                    //return sb.toString();
                } else {
                    Log.d("conn err", con.getResponseMessage());
                }

                JSONArray response = new JSONArray(sb.toString());
                Log.d("response string", response.toString());
                session_id = response.getJSONObject(3).getString("session_id");
                salt = response.getJSONObject(4).getString("session_salt");

            }catch(Exception e){
                e.printStackTrace();
            }
            return session_id;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }

        public String getSalt(){
            return salt;
        }
    }

