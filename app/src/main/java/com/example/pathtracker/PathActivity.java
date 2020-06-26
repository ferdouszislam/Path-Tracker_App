package com.example.pathtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PathActivity extends AppCompatActivity implements PathView, SensorEventListener {

    // debug text view
    TextView debugText;

    // log tag
    String TAG = "debug-pathActivity";

    // presenter
    PathPresenter pathPresenter;

    // sensor config variables
    private SensorManager sensorManager;
    private Sensor linearAccelerometer, rotationVector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize UI
        setupUI();

        // initialize presenter
        pathPresenter = new PathPresenter(this);

        // initialize sensor configs
        setUpSensors();
    }

    private void setupUI() {

        debugText = findViewById(R.id.debugTextView);

    }

    private void setUpSensors() {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        linearAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        pathPresenter.updateSensorAvailability(linearAccelerometer);
        pathPresenter.updateSensorAvailability(rotationVector);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // start sensors
        sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop sensors (?)
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        pathPresenter.updateSensorValues(event);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        //TODO: find out more...

    }

    @Override
    public void notifySensorUnavailable(String message) {

        Log.d(TAG, "notifySensorUnavailable: " + message);

        debugText.setText(message);

    }

    @Override
    public void showPathPoint(String message) {

        Log.d(TAG, "showOrientationAngles: "+message);

        debugText.setText(message);

    }
}