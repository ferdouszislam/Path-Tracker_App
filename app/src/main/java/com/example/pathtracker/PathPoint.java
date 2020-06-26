package com.example.pathtracker;

public class PathPoint {

    //@Exclude
    private DistanceComponent xDistance = new DistanceComponent(), yDistance = new DistanceComponent();

    private double distance, angle;

    public PathPoint() {
    }

    public void setupYDistance(double yAccelerationComponent, double dt){

        this.yDistance.setA(yAccelerationComponent);
        this.yDistance.setT(dt);
        this.yDistance.computeDistance(yAccelerationComponent, dt);

    }

    public void setupXDistance(double xAccelerationComponent, double dt){

        this.xDistance.setA(xAccelerationComponent);
        this.xDistance.setT(dt);
        this.xDistance.computeDistance(xAccelerationComponent, dt);

    }

    public void computeDistanceAndAngle(double displacementAngle){

        // r^2 = x^2 + y^2
        this.distance = Math.sqrt( xDistance.getS()*xDistance.getS() + yDistance.getS()*yDistance.getS() );

        //TODO: compute angle of resultant distance


        this.angle += displacementAngle;
    }

    public DistanceComponent getxDistance() {
        return xDistance;
    }

    public DistanceComponent getyDistance() {
        return yDistance;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public String toString() {
        return "PathPoint{" +
                "distance=" + distance +
                ", angle=" + angle +
                '}';
    }
}
