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
            Toast.makeText(context, "Sensor is in-hand ;)", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Device does not have an accelerometer sensor.", Toast.LENGTH_LONG).show();
        }

        vibrator = (Vibrator) m_Context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Toast.makeText(m_Context, deltaX + ", " + deltaY + ", " + deltaZ, Toast.LENGTH_SHORT).show();

        final float alpha = 0.8f;

        float[] gravity = new float[3];
        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        float[] linear_acceleration = new float[3];
        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        deltaX = Math.abs(lastX - linear_acceleration[0]);
        deltaY = Math.abs(lastY - linear_acceleration[1]);
        deltaZ = Math.abs(lastZ - linear_acceleration[2]);

        /*
        int noiseLimit = 2;
        if(deltaX < noiseLimit)
            deltaX = 0;
        if(deltaY < noiseLimit)
            deltaY = 0;
        // TODO maybe it have been missing from the src code online
        if(deltaZ < noiseLimit)
            deltaZ = 0;
        */

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
