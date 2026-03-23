package me.regularben.util;

import java.util.Arrays;

public class Location {
    double[] local = new double[2];
    private double x;
    private double y;
    public Location(double x, double y) {
        this.x = x;
        this.y = y;
        setLocalArray();
    }

    public double[] getLocal() {
        return local;
    }
    public String getLocalS() {
        return Arrays.toString(local);
    }
    private void setLocalArray(){
        local[0] = x;
        local[1] = y;
    }

    public void setLocal(double x, double y) {
        this.x = x;
        this.y = y;
        setLocalArray();
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }
}
