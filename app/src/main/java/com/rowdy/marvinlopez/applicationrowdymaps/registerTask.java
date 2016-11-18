package com.rowdy.marvinlopez.applicationrowdymaps;

import java.net.*;
import java.io.*;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.*;
import java.security.*;

/**
 * Created by jonathan on 11/17/2016.
 */

public class registerTask extends AsyncTask<String, String, String> {
    protected String doInBackground(String...insert){
        try {
            String session = insert[0];
            int sessionId = Integer.parseInt(session);
            String salt = insert[2];
            JSONArray arr = new JSONArray();
            JSONObject act = new JSONObject();
            act.put("action", "run_sql");
            JSONObject query = new JSONObject();
            query.put("query", insert[1]);
            JSONObject sess = new JSONObject();
            sess.put("session_id", sessionId);
            arr.put(act);
            arr.put(query);
            arr.put(sess);

            String encodeThis = (arr.toString() + salt);

            String newChecksum = Encoder.encode(encodeThis);

            //Create JSON object for the calculated checksum and append that to the array
            JSONObject checksumLogin = new JSONObject();
            checksumLogin.put("checksum", newChecksum);
            arr.put(checksumLogin);


            URL url = new URL("https://easel1.fulgentcorp.com/bifrost/ws.php?json=" + arr.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            Log.d("Task2 err", arr.toString());

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(URLEncoder.encode(arr.toString(),"UTF-8"));
            wr.flush();

            wr.writeBytes(URLEncoder.encode(arr.toString(),"UTF-8"));
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
                Log.d("response err", "" + sb.toString());
                return sb.toString();
            } else {
                Log.d("conn err", con.getResponseMessage());
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(Long result) {
    }
}
