package ru.li24robotics.ev3.robolab.cubeFinder;


import java.util.ArrayList;

public class RouteIteration {
    private String type;
    private double value;

    public RouteIteration(String type, double value)
    {
        this.type = type;
        this.value = value;
    }

    public String getType()
    {
        return type;
    }

    public double getValue()
    {
        return value;
    }

}
