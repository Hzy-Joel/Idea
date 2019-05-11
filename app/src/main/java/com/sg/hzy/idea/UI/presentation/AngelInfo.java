package com.sg.hzy.idea.UI.presentation;

public class AngelInfo {
    private double mAngel;
    private double mRadius;
    private int mX;
    private int mY;

    public AngelInfo(double angel, double radius) {
        mAngel = angel;
        mRadius = radius;
    }

    public void set(double angel,double radius){
        mAngel = angel;
        mRadius = radius;
    }

    public double getAngel() {
        return mAngel;
    }

    public void setAngel(double angel) {
        mAngel = angel;
    }

    public double getRadius() {
        return mRadius;
    }

    public void setRadius(double radius) {
        mRadius = radius;
    }
}
