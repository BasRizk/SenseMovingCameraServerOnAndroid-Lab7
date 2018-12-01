package com.example.and_lab.lab_7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AccSensorMotion accSensorMotion;
    private SimpleWebServer webServer;
    private TCPClient emulatorClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accSensorMotion = new AccSensorMotion(this);

        webServer = new SimpleWebServer(8000,getAssets(), this);
        webServer.start();
        emulatorClient = new TCPClient(8080);
        emulatorClient.start();
//       while(true){
//           if(webServer.isPictureTaken()) {
//               try {
//                   emulatorClient.sendMessage("picture Taken!");
//                   webServer.setPictureTaken(false);
//               } catch (IOException e) {
//                   e.printStackTrace();
//               }
//           }
//       }

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
        try {
            emulatorClient.sendMessage(msg + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
    }
}
