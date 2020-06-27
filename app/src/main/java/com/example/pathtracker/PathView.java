package com.example.pathtracker;

public interface PathView {

    void notifySensorUnavailable(String message);

    void showPathPoint(String message);

    void plotLocation(double latitude, double longitude);

    void plotPoint(double x, double y);

}
