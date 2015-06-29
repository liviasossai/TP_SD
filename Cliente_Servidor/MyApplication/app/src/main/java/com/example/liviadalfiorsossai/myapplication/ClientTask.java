package com.example.liviadalfiorsossai.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientTask {

    int port;
    ServerSocket listener;
    Socket socket;

    public ClientTask(int porta){
        port = porta;
    }


    public void startCli() {

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    listener = new ServerSocket(port);
                } catch(Exception e){e.printStackTrace();}

                    while (true) {
                        try {
                            socket = listener.accept();
                        }catch (Exception e){e.printStackTrace();}

                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            System.out.println(in.readLine());
                        }catch(Exception e){e.printStackTrace();}
                        }
                    }

        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();

    }



}

