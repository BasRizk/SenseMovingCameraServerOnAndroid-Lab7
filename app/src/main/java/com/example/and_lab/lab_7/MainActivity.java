package com.example.and_lab.lab_7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private AccSensorMotion accSensorMotion;
    private SimpleWebServer webServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accSensorMotion = new AccSensorMotion(this);

        webServer = new SimpleWebServer(8000,getAssets(), this);
        webServer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        accSensorMotion.onResumeAccSensorMotion();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausing the sensor detection by the application
        accSensorMotion.onPauseAccSensorMotion();
    }

    public void onLean(String msg) {
        Thread httpClient = new Thread(new HttpClient(8080, msg));
        httpClient.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webServer.camHandler.setPictureTaken(true);
        if(webServer.camHandler.isPictureTaken()){

            Thread httpClient = new Thread(new HttpClient(8080, "Picture taken!"));
            httpClient.start();

            webServer.camHandler.setPictureTaken(false);
        }
    }
}
