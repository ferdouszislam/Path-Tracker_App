package com.example.pathtracker;

public interface PathView {

    void notifySensorUnavailable(String message);

    void showPathPoint(String message);

    void plotPoint(double distance, double angle);

}
