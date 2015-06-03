package com.example.liviadalfiorsossai.myapplication;

import android.os.AsyncTask;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by liviadalfiorsossai on 4/6/15.
 */

    class RequestTask extends AsyncTask<String, Void, JSONObject> {


    public AsyncResponse delegate = null; //Call back interface
    JSONObject jsonObj = new JSONObject();
    public boolean response_is_OK = true;
    public RequestTask(JSONObject jsonObj, AsyncResponse asyncResponse) {
        delegate = asyncResponse; // Cadastro de callback
        this.jsonObj = jsonObj;   // Corpo da requisição encapsulado em JSON
    }

    @Override
    protected JSONObject doInBackground(String... urls) {


        try {

            long startTime = System.nanoTime();
            JSONObject jsonreturned =  RPC.downloadUrl(jsonObj);
            //estimatedTime = System.nanoTime() - startTime;
            return jsonreturned;

        } catch (IOException e) {
            System.out.println("Problema na entrada de dados.");
        }catch (ConnException e) {
            response_is_OK = false;
            System.out.println("Problema na comunicação com o servidor de jogo.");
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        delegate.processFinish(result,response_is_OK);

    }

    public interface AsyncResponse {

        void processFinish(JSONObject output, boolean response_is_OK);
    }

    }

