package com.example.and_lab.lab_7;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.widget.Toast;

public class AccSensorMotion implements SensorEventListener {

    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private float vibrateThreshold = 0;
    private Vibrator vibrator;
    private Context m_Context;

    protected AccSensorMotion(Context context) {
        m_Context = context;
        sensorManager = (SensorManager) m_Context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            Toast.makeText(context, "Device does not have an accelerometer sensor.", Toast.LENGTH_LONG).show();
        }

        vibrator = (Vibrator) m_Context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[0]);
        deltaZ = Math.abs(lastZ - event.values[0]);

        int noiseLimit = 2;
        if(deltaX < noiseLimit)
            deltaX = 0;
        if(deltaY < noiseLimit)
            deltaY = 0;
        // TODO maybe it have been missing from the src code online
        if(deltaZ < noiseLimit)
            deltaZ = 0;

        if( (deltaZ > vibrateThreshold) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
            vibrator.vibrate(50);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing for now
    }

    protected void onResumeAccSensorMotion() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPauseAccSensorMotion() {
        sensorManager.unregisterListener(this);
    }



}
