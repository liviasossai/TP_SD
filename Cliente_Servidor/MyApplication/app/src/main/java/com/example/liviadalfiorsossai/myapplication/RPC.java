package com.example.liviadalfiorsossai.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Connection;

/**
 * Created by Lucas on 16/04/2015.
 */
public class RPC {

    private static final String DEBUG_TAG = "HttpExample";
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    private static String myurl = "http://192.168.0.105:7000";//Add endereco da URL
    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    public static JSONObject downloadUrl(JSONObject jsonObj) throws IOException {
=======
>>>>>>> Stashed changes
    //private static HttpURLConnection conn = null;
    //private static String myurl = "http://192.168.1.110:3000";//Add endereco da URL
    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    public static String geturl() throws IOException, ConnException {
        return getServerURLToConnect();
    }
    public static JSONObject downloadUrl(JSONObject jsonObj) throws IOException, ConnException {
<<<<<<< Updated upstream
=======
>>>>>>> origin/master
>>>>>>> Stashed changes
        InputStream is = null;
        HttpURLConnection conn = null;
        System.setProperty("http.keepAlive", "false"); // must be set
        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            //conn.setDoInput(true);
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
            else
            {
                informFailToIdentityServer();
                throw new ConnException("Problema no servidor de jogo.");
            }

        } catch (JSONException e) {
            System.out.printf("Problema na criacao do JSON");
            e.printStackTrace();
        } catch (SocketTimeoutException se) {
            informFailToIdentityServer();
            throw new ConnException("Conexão com servidor do jogo demorando mais que o normal. Tente novamente.");
        }
        catch (ConnectException se) {
            informFailToIdentityServer();
            throw new ConnException("Conexão com servidor do jogo não foi possível. Tente novamente.");
        } finally {  // Makes sure that the InputStream is closed after the app is finished using it.
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    private static void informFailToIdentityServer() throws ConnException {
        try {
            String myurl = "http://192.168.25.16:7000";
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Host", "android.schoolportal.gr");
            conn.setUseCaches(false);
            //conn.setRequestProperty("Texto",textViewToSend.getText().toString());
            //conn.setReadTimeout(10000 /* milliseconds */);
            //conn.setConnectTimeout(15000 /* milliseconds */);
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("FUNCAO", "no_server");
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
            if(HttpResult !=HttpURLConnection.HTTP_OK) {
                throw new ConnException("Servidor identidade não retornou Sucesso.");
            }

        } catch (JSONException e) {
            System.out.printf("Problema na criacao do JSON");
            e.printStackTrace();
        } catch (SocketTimeoutException se) {
            throw new ConnException("Conexão com servidor identidade demorando mais que o normal");
        }
        catch (ConnectException se) {
            throw new ConnException("Conexão com servidor identidade não foi possível");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (ConnException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
<<<<<<< HEAD
}
=======

    private static String getServerURLToConnect() throws IOException, ConnException {
        InputStream is = null;
        HttpURLConnection conn = null;
        System.setProperty("http.keepAlive", "false"); // must be set
        try {
            String myurl = "http://192.168.25.16:7000";
            URL url = new URL(myurl);
            //System.setProperty("http.keepAlive", "false");
            conn = (HttpURLConnection) url.openConnection();
            final String charset = "UTF-8";
            //conn.setDoInput(true);
            conn.setDoOutput(true);
            //conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Host", "android.schoolportal.gr");
            conn.setUseCaches(false);
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
            //conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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
                // Convert the InputStream into a JSONObject   contentResultAsString = jobject.get("ip")
                JSONObject jobject =  new JSONObject(sb.toString());
                String ip = (String) jobject.get("ip");
                String porta = (jobject.get("port")).toString();

                return "http://" + ip+ ":" + porta;
            }
            else
            {
                throw new ConnException("Servidor identidade não retornou Sucesso.");
            }
        } catch (JSONException e) {
            System.out.printf("Problema na criacao do JSON");
            e.printStackTrace();
        } catch (SocketTimeoutException se) {
            //informFailToIdentityServer();
            throw new ConnException("Conexão com servidor identidade demorando mais que o normal");
        }
        catch (ConnectException se) {
            //informFailToIdentityServer();
            throw new ConnException("Conexão com servidor identidade não foi possível");
        }
        catch (ClassCastException se) {
            //informFailToIdentityServer();
            throw new ConnException("Servidor identidade retornou dados do servidor do jogo vazios.");
        }
        catch (EOFException eo)
        {
            throw new ConnException("Conexão com servidor identidade não foi possível");
        }
        catch (Exception eo)
        {
            throw new ConnException("Conexão com servidor identidade não foi possível");
        }
        finally {  // Makes sure that the InputStream is closed after the app is finished using it.
            //conn.disconnect();
            if (is != null) {
                is.close();
            }
        }
        return null;
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> origin/master
>>>>>>> Stashed changes
