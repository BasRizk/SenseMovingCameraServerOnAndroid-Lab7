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
    private boolean isRunning = false;
    private DataOutputStream outToServer;
    private URL url;
    private String msg;

    public HttpClient(int portNumber, String msg) {

        this.portNumber = portNumber;
        this.msg = msg;
        try {
            url = new URL("http://10.0.2.2:" + portNumber + "/" + msg + "/");
            isRunning = true;
            Log.d("HTTP","Connection should intialized");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d("HTTP","Connection should Opened");

            try {
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                Log.d("HTTP","Connection config to sent are set");

                //OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                //writeStream(out);

                urlConnection.setDoOutput(true);
                //urlConnection.setRequestMethod("POST");
                //OutputStream os = urlConnection.getOutputStream();
                //OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                //osw.write(msg);
                //osw.flush();
                //osw.close();
                //os.close();  //don't forget to close the OutputStream
                urlConnection.connect();

            } finally {
                urlConnection.disconnect();
            }
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
