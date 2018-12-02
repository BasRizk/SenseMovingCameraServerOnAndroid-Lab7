package com.example.and_lab.lab_7;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClient implements Runnable {

    private int portNumber;
    private URL url;
    private String msg;

    public HttpClient(int portNumber, String msg) {

        this.portNumber = portNumber;
        this.msg = msg;
        try {
            url = new URL("http://10.0.2.2:" + this.portNumber + "/" + msg + "/");
            Log.d("HTTP","Connection should initialized");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            //urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            //urlConnection.setRequestMethod("POST");
            //OutputStream os = urlConnection.getOutputStream();
            //OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            //osw.write(msg);
            //osw.flush();
            //osw.close();
            //os.close();  //don't forget to close the OutputStream
            urlConnection.connect();
            urlConnection.getResponseCode();
            urlConnection.getResponseMessage();

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeStream(OutputStream out) {
        try {
            Log.d("HTTP","Msgs are about to be sent");
            out.write(msg.getBytes());
            out.flush();
            Log.d("HTTP","Msg should be flushed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
