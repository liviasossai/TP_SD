package com.example.liviadalfiorsossai.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lucas on 16/04/2015.
 */
public class RPC {

    private static final String DEBUG_TAG = "HttpExample";
    //private static String myurl = "http://192.168.1.110:3000";//Add endereco da URL
    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    public static String geturl() throws IOException {
        return getServerURLToConnect();
    }
    public static JSONObject downloadUrl(JSONObject jsonObj) throws IOException {
        InputStream is = null;

        try {
            String myurl = getServerURLToConnect();
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Host", "android.schoolportal.gr");
            conn.setUseCaches (false);
            //conn.setRequestProperty("Texto",textViewToSend.getText().toString());
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);


            //Building Json parameter as Buffer
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            out.write(jsonObj.toString());
            out.close();
            // Starts the query
            conn.connect();

            String contentResultAsString = null;
            int HttpResult = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + HttpResult);
            StringBuilder sb = new StringBuilder();
            if(HttpResult ==HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                // Convert the InputStream into a JSONObject
                return (new JSONObject(sb.toString()));
            }
            return null;

        } catch (JSONException e) {
            System.out.printf("Problema na criacao do JSON");
            e.printStackTrace();
        } finally {  // Makes sure that the InputStream is closed after the app is finished using it.
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private static String getServerURLToConnect() throws IOException {
        InputStream is = null;

        try {
            String myurl = "http://192.168.25.16:7000";
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Host", "android.schoolportal.gr");
            conn.setUseCaches (false);
            //conn.setRequestProperty("Texto",textViewToSend.getText().toString());
            //conn.setReadTimeout(10000 /* milliseconds */);
            //conn.setConnectTimeout(15000 /* milliseconds */);
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("FUNCAO", "consulta");
            //Building Json parameter as Buffer
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            out.write(jsonObj.toString());
            out.close();
            // Starts the query
            conn.connect();

            String contentResultAsString = null;
            int HttpResult = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + HttpResult);
            StringBuilder sb = new StringBuilder();
            if(HttpResult ==HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                // Convert the InputStream into a JSONObject
                JSONObject jobject =  new JSONObject(sb.toString());
                String ip = (String) jobject.get("ip");
                String porta = (String) jobject.get("port");
                return "http://" + ip+ ":" + porta;
            }
            return null;

        } catch (JSONException e) {
            System.out.printf("Problema na criacao do JSON");
            e.printStackTrace();
        } finally {  // Makes sure that the InputStream is closed after the app is finished using it.
            if (is != null) {
                is.close();
            }
        }
        return null;
    }
}
