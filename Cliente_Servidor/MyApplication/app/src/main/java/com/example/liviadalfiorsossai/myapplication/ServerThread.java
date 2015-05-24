package com.example.liviadalfiorsossai.myapplication;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Lucas on 30/04/2015.
 */
public class ServerThread implements Runnable {
    // DEFAULT application IP
    public static String SERVERIP = "10.0.2.15";
    // DESIGNATE A PORT
    public static final int SERVERPORT = 12388;

    private Handler handler = new Handler();
    private ServerSocket serverSocket;
    private Context mContext;
    //IP Obtido, para comunicar com servidor
    private static String urlConnection = "http://192.168.1.110:3000";
    //Get para obter o IP a comunicar
    public static String getUrlConnection()
    {
        return urlConnection;
    }

    public void run() {
        //final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);
        try {
            SERVERIP = getIp();
            if (SERVERIP != null) {
                try {
                    ServerSocket serverSocket = new ServerSocket(12388);
                    System.out.println("Waiting for clients to connect...");
                    Log.d("T", "Teste1");
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        Log.d("T", "Teste2");
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String line;
                        //StringBuilder sb = new StringBuilder();
                        line = in.readLine();
                        //while ((line = in.readLine()) != null) {
                            //sb.append(line);
                            //in.
                        //}
                       urlConnection = line;
                    }
                } catch (IOException e) {
                    System.err.println("Unable to process client request");
                    e.printStackTrace();
                }
            }
        }catch (Exception E){}}
    // GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {return inetAddress.getHostAddress().toString();}
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }
    private String getIp(){
        String ipAddress = null;
        Enumeration<NetworkInterface> net = null;
        try {
            net = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while(net.hasMoreElements()){
            NetworkInterface element = net.nextElement();
            Enumeration<InetAddress> addresses = element.getInetAddresses();
            while (addresses.hasMoreElements()){
                InetAddress ip = addresses.nextElement();
                if (ip instanceof Inet4Address){
                    if (ip.isSiteLocalAddress()){
                        ipAddress = ip.getHostAddress();
                    }
                }
            }
        }
        return ipAddress;
    }

    InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
}
