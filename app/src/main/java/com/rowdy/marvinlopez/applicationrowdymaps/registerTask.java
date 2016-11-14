package com.rowdy.marvinlopez.applicationrowdymaps;

import java.net.*;
import java.io.*;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.*;

/**
 * Created by jonathan on 11/14/2016.
 */

public class registerTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String...insert) {
            Log.d("task err", "" + insert[0]);
            try {
                URL url = new URL("https://easel1.fulgentcorp.com/bifrost/ws.php?json=");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONArray arr = new JSONArray();
                JSONObject act = new JSONObject();
                act.put("action", "run_sql");
                JSONObject query = new JSONObject();
                query.put("query", insert[0]);
                JSONObject sess = new JSONObject();
                sess.put("session_id", new Integer(1));
                JSONObject checksum = new JSONObject();
                checksum.put("checksum","1");
                arr.put(act);
                arr.put(query);
                arr.put(sess);
                arr.put(checksum);
                Log.d("Task err2", "" + arr.toString());
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(arr.toString());
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
                    br.close();
                    Log.d("success err", "" + sb.toString());
                    return sb.toString();
                } else {
                    Log.d("conn err", con.getResponseMessage());
                }


            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

