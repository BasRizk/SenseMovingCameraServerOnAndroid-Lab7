package com.example.and_lab.lab_7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private AccSensorMotion accSensorMotion;
    private SimpleWebServer webServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accSensorMotion = new AccSensorMotion(this);

        webServer = new SimpleWebServer(8000,getAssets());
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
}
