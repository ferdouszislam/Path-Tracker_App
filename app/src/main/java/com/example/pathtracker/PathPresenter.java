package com.example.pathtracker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class PathPresenter {

    // view
    private PathView pathView;

    // models
    private LinearAccelerationData linearAccelerationData = new LinearAccelerationData();
    private OrientationData orientationData = new OrientationData();

    // objects to measure distance from acceleration
    PathPoint pathPoint = new PathPoint();

    // constants
    private static final double MINIMUM_CALCULABLE_DISTANCE = 0.5;

    public PathPresenter(PathView pathView) {
        this.pathView = pathView;
    }


    public void updateSensorValues(SensorEvent event){

        switch (event.sensor.getType()){

            case Sensor.TYPE_LINEAR_ACCELERATION:

                linearAccelerationData.setxValue(event.values[0]);
                linearAccelerationData.setyValue(event.values[1]);
                linearAccelerationData.setzValue(event.values[2]);

                linearAccelerationData.setTime(event.timestamp);

                break;


            case Sensor.TYPE_ROTATION_VECTOR:

                //set rotation vector (useful?)
                orientationData.setRotationVector(event.values);

                // compute rotation matrix
                final float[] rotationMatrix = new float[9];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

                // set rotation matrix (useful?)
                orientationData.setRotationMatrix(rotationMatrix);

                // compute orientation angles
                final float[] orientationAngles = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientationAngles);

                // set the orientation angles (USEFUL!)
                orientationData.setAzimuth(orientationAngles[0]);
                orientationData.setPitch(orientationAngles[1]);
                orientationData.setRoll(orientationAngles[2]);

                break;

        }

        computePath();

    }

    private void computePath() {

        // acceleration components
        double accelerationXComponent=0, accelerationYComponent=0;
        // orientation angles
        double azimuth, pitch, roll;


        // compute acceleration x-component
        roll = Math.abs(orientationData.getRoll());
        accelerationXComponent += linearAccelerationData.getxValue() * Math.cos(roll); // x-component due to x acceleration
        double zxAngle = Math.abs( Math.toRadians(90) - roll );
        if(orientationData.getRoll() >= 0)
            accelerationXComponent += linearAccelerationData.getzValue() * Math.cos(zxAngle);
        else
            accelerationXComponent -= linearAccelerationData.getzValue() * Math.cos(zxAngle);


        // compute acceleration y-component
        pitch = Math.abs(orientationData.getPitch());
        accelerationYComponent += linearAccelerationData.getyValue() * Math.cos(pitch); // y-component due to y acceleration
        double zyAngle = Math.toRadians(90) - pitch;
        accelerationYComponent += linearAccelerationData.getzValue() * Math.cos(zyAngle); // y-component due to z acceleration


        // compute distance from acceleration
        pathPoint.setupXDistance(accelerationXComponent, linearAccelerationData.getDt());
        pathPoint.setupYDistance(accelerationYComponent, linearAccelerationData.getDt());


        // compute direction of movement
        azimuth = orientationData.getAzimuth();
        if(azimuth<0)
            azimuth+=Math.toRadians(360);

        // Finally
        pathPoint.computeFinalDistanceAndAngle(azimuth);

        // Voila!!
        pathView.showPathPoint(pathPoint.toString());

        //TODO: calculate latitude, longitude from 'pathPoint.getDistance()', 'pathPoint.getAngle()'
        if(pathPoint.getDistance()<MINIMUM_CALCULABLE_DISTANCE) //smaller than 20cm, average step size ~ 76cm
            return;

        // plot in graph for now
        double x, y;
        x = pathPoint.getDistance() * Math.cos(pathPoint.getAngleWithX());
        y = pathPoint.getDistance() * Math.sin(pathPoint.getAngleWithX());
        pathView.plotPoint(x, y);
    }

    public void updateSensorAvailability(Sensor sensor){

        if(sensor==null){

            switch (sensor.getType()){

                case Sensor.TYPE_LINEAR_ACCELERATION:
                    linearAccelerationData.setAvailable(false);
                    break;

                case Sensor.TYPE_ROTATION_VECTOR:
                    orientationData.setAvailable(false);
                    break;
            }

            pathView.notifySensorUnavailable("Necessary sensors (software or, hardware) not present on phone!");

        }

    }

}
