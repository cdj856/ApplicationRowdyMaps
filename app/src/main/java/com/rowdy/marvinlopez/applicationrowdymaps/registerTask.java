package com.rowdy.marvinlopez.applicationrowdymaps;

import java.net.*;
import java.io.*;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.*;
import java.security.*;
import java.io.StringWriter;

//import org.apache.http.client.*;



/**
 * Created by jonathan on 11/14/2016.
 */

public class registerTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String...insert) {
            Log.d("task err", "" + insert[0]);
            try {
                //Setting connection and method
                URL url = new URL("https://easel1.fulgentcorp.com/bifrost/ws.php?json=");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONObject json = new JSONObject();
                json.put("someKey", "someValue");

                //Creating JSON array of JSON objects
                JSONArray arrLogin = new JSONArray();
                JSONObject actLog = new JSONObject();
                actLog.put("action", "login");
                JSONObject login = new JSONObject();
                login.put("login", "fa2016_madmappers");
                JSONObject pass = new JSONObject();
                pass.put("password", "28e501ff6289a7b00015bd19cf92b3342d7eba1af47864374d3dbc1b9e4ec919");
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

                //Hash JSON array to get checksum
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(arrLogin.toString().getBytes());

                byte byteData[] = md.digest();

                StringBuffer sbuf = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                    sbuf.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }

                String newChecksum = sbuf.toString();

                //Create JSON object for the calculated checksum and append that to the array
                JSONObject checksumLogin = new JSONObject();
                checksumLogin.put("checksum", newChecksum);
                arrLogin.put(checksumLogin);

                String test = "[{\"action\":\"login\"},{\"login\":\"fa2016_madmappers\"},{\"password\":\"tPP3M2cBQsMUsVjq\"},{\"app_code\":\"fpcwvmCthCnAbvs5\"},{\"session_type\":\"id_salt\"},{\"checksum\":\"844c323475c7244b8734cb3f28a7cd6f33b64fcc15b330c98db257705c5ec62a\"}]";
                //28e501ff6289a7b00015bd19cf92b3342d7eba1af47864374d3dbc1b9e4ec919
                String thing = JSONObject.quote(arrLogin.toString());
                String thing2 = thing.substring(1, thing.length()-1);
                Log.d("Task err2", test);

                /*
                byte[] outputBytes = "{'value': 7.5}".getBytes("UTF-8");
                OutputStream os = httpcon.getOutputStream();
                os.write(outputBytes);
                os.close();
                */
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(test);
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




                /* Keep for run_sql when i get this shit finally working
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
                arr.put(checksum);*/


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

