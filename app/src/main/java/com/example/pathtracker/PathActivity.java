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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

public class PathActivity extends AppCompatActivity implements PathView, SensorEventListener {

    // debug text view
    private TextView debugText;

    // graph for path creation
    private GraphView graphView;
    private LineGraphSeries<DataPoint> graphSeries;
    private PointsGraphSeries<DataPoint> graphPoints;
    private double prevX = 0, prevY = 0;
    private static final double GRAPH_MAX = 10, GRAPH_MIN = -10;

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
        setContentView(R.layout.activity_path);

        // initialize UI
        setupUI();

        // initialize presenter
        pathPresenter = new PathPresenter(this);

        // initialize sensor configs
        setUpSensors();
    }

    private void setupUI() {

        debugText = findViewById(R.id.debugTextView);

        graphView = findViewById(R.id.graph);
        // setup graph UI
        graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalableY(true);
        graphView.getViewport().setScrollableY(true);

        // initialize graph properties
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(GRAPH_MIN);
        graphView.getViewport().setMaxX(GRAPH_MAX);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(GRAPH_MIN);
        graphView.getViewport().setMaxY(GRAPH_MAX);

        // enable maximum scrolling
        graphView.addSeries(new PointsGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(GRAPH_MIN, GRAPH_MIN)}));
        graphView.addSeries(new PointsGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(GRAPH_MAX, GRAPH_MAX)}));

        graphSeries = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0,0)
        });

        graphPoints = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0,0)
        });

        graphView.addSeries(graphSeries);
        graphView.addSeries(graphPoints);

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

        // stop sensors (here?)
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


    @Override
    public void plotPoint(double x, double y) {

        if(x==prevX && y==prevY) ; // do nothing

        else if(x>prevX){
            // append the graph

            graphSeries.appendData(new DataPoint(x, y), false, 500);
            graphPoints.appendData(new DataPoint(x, y), false, 500);
        }

        else if(x<prevX){
            // graph can't be drawn backwards horizontally

            graphSeries = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(x, y),
                    new DataPoint(prevX, prevY)
            });
            graphPoints = new PointsGraphSeries<>(new DataPoint[]{
                    new DataPoint(x, y),
                    new DataPoint(prevX, prevY),
            });

            graphView.addSeries(graphSeries);
            graphView.addSeries(graphPoints);

            //make x,y the last point on graph
            graphSeries = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(x, y),
            });
            graphPoints = new PointsGraphSeries<>(new DataPoint[]{
                    new DataPoint(x, y),
            });
            graphView.addSeries(graphSeries);
            graphView.addSeries(graphPoints);

        }

        prevX = x;
        prevY = y;
    }

    @Override
    public void plotLocation(double latitude, double longitude) {

        // TODO: implement

    }
}