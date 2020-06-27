package com.example.pathtracker;

public class OrientationData {

    /* TODO: complete the explanation

    holds raw & calculated(by API) data from rotation vector sensor(software sensor)

    explanation of azimuth, pitch, roll

    azimuth:

    pitch: range is [-90,90]. 0 to 90 is for angle between positive side of y-axis and ground
            , -90 to 0 for angle between positive side of y-axis and ground.

    roll:

     */

    private double azimuth, pitch, roll;
    private boolean available = true;

    //@Exclude
    private float[] rotationVector = new float[4];
    //@Exclude
    private float[] rotationMatrix = new float[9];

    public OrientationData() {
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) { this.azimuth = azimuth; }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getRoll() {
        return roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    public float[] getRotationVector() {
        return rotationVector;
    }

    public void setRotationVector(float[] rotationVector) {
        this.rotationVector = rotationVector;
    }

    public float[] getRotationMatrix() {
        return rotationMatrix;
    }

    public void setRotationMatrix(float[] rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "{" +
                "azimuth=" + Math.floor(Math.toDegrees(azimuth)*100) / 100.00d +
                ", pitch=" + Math.floor(Math.toDegrees(pitch)*100) / 100.00d +
                ", roll=" + Math.floor(Math.toDegrees(roll)*100) / 100.00d +
                '}';
    }
}
