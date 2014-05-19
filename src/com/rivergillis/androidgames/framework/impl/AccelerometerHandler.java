package com.rivergillis.androidgames.framework.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * This class handles changes to the devices accelerometer
 * @author jgillis
 *
 */
public class AccelerometerHandler implements SensorEventListener {
    float accelX;
    float accelY;	// contains the devices configuration
    float accelZ;

    public AccelerometerHandler(Context context) {
        SensorManager manager = (SensorManager) context			// get a sensormanager from the context
                .getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {		// if we have accelerometers
            Sensor accelerometer = manager.getSensorList(
                    Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer,		// set up the listeners for the accelerometer
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }

    // Fetch the values from the SensorEvent and store them in member variables
    public void onSensorChanged(SensorEvent event) {
        accelX = event.values[0];
        accelY = event.values[1];
        accelZ = event.values[2];
    }

    // return member variables
    public float getAccelX() {
        return accelX;
    }

    public float getAccelY() {
        return accelY;
    }

    public float getAccelZ() {
        return accelZ;
    }
}
