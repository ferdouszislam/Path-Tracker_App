package com.example.pathtracker;

public class DistanceComponent {

    /*

    get distance from acceleration using classical kinematics formulas

    only "PathPoint" class has a object as a member variable of this class

     */

    private double s=0, u=0, a=0, t=0;

    public DistanceComponent() {
    }

    public DistanceComponent(double s, double u, double a, double t) {
        this.s = s;
        this.u = u;
        this.a = a;
        this.t = t;
    }

    public void computeDistance(double a, double t){

        this.a = a;
        this.t = t;

        s = u*t + 0.5*a*t*t; // s = ut + 1/2 at^2

        this.u = this.u + a*t;

    }

    public double getS() {
        return s;
    }

    public double getA() {
        return a;
    }

    public double getT() {
        return t;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setT(double t) {
        this.t = t;
    }
}
