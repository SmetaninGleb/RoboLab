package ru.li24robotics.ev3.robolab.cubeFinder;


import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable{
    private ArrayList<RouteIteration> routeList;
    private int nowIterationNumber;

    public Route(ArrayList<RouteIteration> routeList)
    {
        this.routeList = routeList;
        nowIterationNumber = 0;
    }

    public Route()
    {
        this.routeList = new ArrayList<RouteIteration>();
        nowIterationNumber = 0;
    }

    public void addRouteIterationToEnd(RouteIteration iteration)
    {
        routeList.add(iteration);
    }

    public void addRouteIterationToStart(RouteIteration iteration)
    {
        routeList.add(0, iteration);
    }

    public int getRouteSize ()
    {
        return routeList.size();
    }

    public RouteIteration nextIteration()
    {
        if(nowIterationNumber < routeList.size() - 1)
        {
            nowIterationNumber++;
            return routeList.get(nowIterationNumber - 1);
        }
        else {
            return routeList.get(nowIterationNumber);
        }
    }

    public ArrayList<RouteIteration> getRouteList()
    {
        return routeList;
    }
}
