package com.example.pathtracker;

public class PathPoint {

    /*

    holds distance and angle of movement
    used in graph for creating path
    will be used for plotting latitude, longitudes on map

     */

    //@Exclude
    private DistanceComponent xDistance = new DistanceComponent(), yDistance = new DistanceComponent();

    // distance: resultant distance (straight line from current to destination point), angle: angle with north (find out the fancy name)
    private double distance, angle;
    //@Exclude
    private double angleWithX; // needed only for graph (not latLng)

    public PathPoint() {
    }

    public void setupYDistance(double yAccelerationComponent, double dt){

        this.yDistance.setA(yAccelerationComponent); //not necessary
        this.yDistance.setT(dt); // not necessary
        this.yDistance.computeDistance(yAccelerationComponent, dt);

    }

    public void setupXDistance(double xAccelerationComponent, double dt){

        this.xDistance.setA(xAccelerationComponent); // not necessary
        this.xDistance.setT(dt); // not necessary
        this.xDistance.computeDistance(xAccelerationComponent, dt);

    }

    public void computeFinalDistanceAndAngle(double azimuth){

        // resultant distance measurement
        // r^2 = x^2 + y^2
        this.distance = Math.sqrt( xDistance.getS()*xDistance.getS() + yDistance.getS()*yDistance.getS() );

        // angle measurement reference: [lines 42-73]
        // https://www.khanacademy.org/math/precalculus/x9e81a4f98389efdf:vectors/x9e81a4f98389efdf:component-form/a/vector-magnitude-and-direction-review

        // tan(theta) = y/x

        if(xDistance.getS()==0){
            // divide by zero issue

            if(yDistance.getS()>=0)
                this.angleWithX = Math.toRadians(90);
            else
                this.angleWithX = Math.toRadians(270);

        }

        else{

            this.angleWithX = Math.atan(yDistance.getS() / xDistance.getS());

            if(xDistance.getS()>0 && yDistance.getS()>=0) ; // quadrant-1 do nothing

            else if(xDistance.getS()<0 && yDistance.getS()>=0){
                // quadrant-2
                this.angleWithX+=Math.toRadians(180);
            }

            else if(xDistance.getS()<0 && yDistance.getS()<0){
                // quadrant-3
                this.angleWithX+=Math.toRadians(180);
            }

            else if(xDistance.getS()>0 && yDistance.getS()<0){
                // quadrant-4
                this.angleWithX+=Math.toRadians(360);
            }

        }

        // take in account the azimuth (angular displacement of framework)
        this.angleWithX -= azimuth;
        if(this.angleWithX<0)
            this.angleWithX+=Math.toRadians(360);

        // compute angle with y-axis, i.e. North
        this.angle = Math.toRadians(90) - this.angleWithX;
        if(this.angle<0)
            this.angle+=Math.toRadians(360);

    }

    public DistanceComponent getxDistance() {
        return xDistance;
    }

    public DistanceComponent getyDistance() {
        return yDistance;
    }

    public double getDistance() {
        return distance;
    }

    public double getAngle() {
        return angle;
    }

    public double getAngleWithX() {
        return angleWithX;
    }

    @Override
    public String toString() {
        return "{" +
                "distance=" + showTwoDecimals(distance) +
                ", angle=" + Math.floor(Math.toDegrees(angle)) +
                ", angleWithX=" + Math.floor( Math.toDegrees(angleWithX) ) +
                '}';
    }

    private double showTwoDecimals(double n){
        return Math.floor(n*100) / 100.00d;
    }

}
