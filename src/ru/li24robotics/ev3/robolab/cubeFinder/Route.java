package ru.li24robotics.ev3.robolab.cubeFinder;


import java.util.ArrayList;

public class Route {
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

    public void addRouteIteration(RouteIteration iteration)
    {
        routeList.add(iteration);
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
