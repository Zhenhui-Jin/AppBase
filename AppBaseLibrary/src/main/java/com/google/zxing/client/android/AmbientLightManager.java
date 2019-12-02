package com.google.zxing.client.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;

import com.google.zxing.client.android.camera.CameraManager;

/**
 * Detects ambient light and switches on the front light when very dark, and off again when sufficiently light.
 *
 * @author Sean Owen
 * @author Nikolaus Huber
 */
final class AmbientLightManager implements SensorEventListener {

    private static final float TOO_DARK_LUX = 45.0f;
    private static final float BRIGHT_ENOUGH_LUX = 450.0f;

    private final ZXingCallback callback;
    private CameraManager cameraManager;
    private Sensor lightSensor;

    AmbientLightManager(@NonNull ZXingCallback callback) {
        this.callback = callback;
    }

    void start(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
//        if (FrontLightMode.readPref(sharedPrefs) == FrontLightMode.AUTO) {
//            SensorManager sensorManager = (SensorManager) callback.getActivity().getSystemService(Context.SENSOR_SERVICE);
//            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//            if (lightSensor != null) {
//                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            }
//        }
    }

    void stop() {
        if (lightSensor != null) {
            SensorManager sensorManager = (SensorManager) callback.getActivity().getSystemService(Context.SENSOR_SERVICE);
            sensorManager.unregisterListener(this);
            cameraManager = null;
            lightSensor = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float ambientLightLux = sensorEvent.values[0];
        if (cameraManager != null) {
            if (ambientLightLux <= TOO_DARK_LUX) {
                cameraManager.setTorch(true);
            } else if (ambientLightLux >= BRIGHT_ENOUGH_LUX) {
                cameraManager.setTorch(false);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

}
