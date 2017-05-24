package ru.li24robotics.ev3.robolab.cubeFinder;

import java.util.ArrayList;

import ru.li24robotics.ev3.robolab.robotControl.RobotController;

/**
 * 
 *	Внимание! Считать направление поворота отновительно начального лабиринта, а не робота!
 *	За поворот робота отвечает поле robotRotation_degrees!!!
 *
 */

public class RouteFollower 
{
	private RobotController controller;
	private int robotRotation_degrees;
	private Route mainRoute;
	
	public RouteFollower(RobotController controller, int startRobotRotation_degrees, Route mainRoute)
	{
		this.controller = controller;
		this.robotRotation_degrees = startRobotRotation_degrees;
		this.mainRoute = mainRoute;
	}
	
	public RouteFollower(RobotController controller, int startRobotRotation_degrees)
	{
		this.controller = controller;
		this.robotRotation_degrees = startRobotRotation_degrees;
	}
	
	public RouteFollower(RobotController controller)
	{
		this.controller = controller;
	}
	
	public void setMainRoute(Route mainRoute)
	{
		this.mainRoute = mainRoute;
	}
	
	public void setStartRobotRotation_degrees(int startRotation)
	{
		this.robotRotation_degrees = startRotation;
	}
	
	public void followRoute()
	{
		if(mainRoute == null)
		{
			//System.err.println("No main route in RouteFollower!!!");
			return;
		}
		
		ArrayList<RouteIteration> _nowRouteList = mainRoute.getRouteList();
		for(int i = 0; i < mainRoute.getRouteSize(); i++)
		{
			//System.out.println(robotRotation_degrees);
			//System.out.println(_nowRouteList.get(i).getType());
			if(_nowRouteList.get(i).getType().equals("ToRight"))
			{
				toRight((int)_nowRouteList.get(i).getValue());
			}
			if(_nowRouteList.get(i).getType().equals("Forward"))
			{
				toForward((int)_nowRouteList.get(i).getValue());
			}
			if(_nowRouteList.get(i).getType().equals("ToLeft"))
			{
				toLeft((int)_nowRouteList.get(i).getValue());
			}
			if(_nowRouteList.get(i).getType().equals("Backward"))
			{
				toBack((int)_nowRouteList.get(i).getValue());
			}
		}
	}
	
	private void toRight(int checksCount)
	{
		switch(robotRotation_degrees)
		{
			case 0:
				turnRobotRight();
				controller.forwardForChecks(checksCount);
				break;
			case 90:
				controller.forwardForChecks(checksCount);
				break;
			case 180:
				turnRobotLeft();
				controller.forwardForChecks(checksCount);
				break;
			case 270:
				turnRobotBack();
				controller.forwardForChecks(checksCount);
				break;
		}
	}
	
	private void toLeft(int checksCount)
	{
		switch(robotRotation_degrees)
		{
			case 0:
				turnRobotLeft();
				controller.forwardForChecks(checksCount);
				break;
			case 90:
				turnRobotBack();
				controller.forwardForChecks(checksCount);
				break;
			case 180:
				turnRobotRight();
				controller.forwardForChecks(checksCount);
				break;
			case 270:
				controller.forwardForChecks(checksCount);
				break;
		}
	}
	
	private void toForward(int checksCount)
	{
		switch(robotRotation_degrees)
		{
			case 0:
				controller.forwardForChecks(checksCount);
				break;
			case 90:
				turnRobotLeft();
				controller.forwardForChecks(checksCount);
				break;
			case 180:
				turnRobotBack();
				controller.forwardForChecks(checksCount);
				break;
			case 270:
				turnRobotRight();
				controller.forwardForChecks(checksCount);
				break;
		}
	}
	
	private void toBack(int checksCount)
	{
		switch(robotRotation_degrees)
		{
			case 0:
				turnRobotBack();
				controller.forwardForChecks(checksCount);
				break;
			case 90:
				turnRobotRight();
				controller.forwardForChecks(checksCount);
				break;
			case 180:
				controller.forwardForChecks(checksCount);
				break;
			case 270:
				turnRobotLeft();
				controller.forwardForChecks(checksCount);
				break;
		}
	}
	
	private void turnRobotRight()
	{
		//System.out.println("Turning right");
		controller.turnRight();
		robotRotation_degrees += 90;
		robotRotation_degrees %= 360;
	}
	
	private void turnRobotLeft()
	{
		//System.out.println("Turning left");
		controller.turnLeft();
		robotRotation_degrees += 270;
		robotRotation_degrees %= 360;
	}
	
	private void turnRobotBack()
	{
		//System.out.println("Turning back");
		controller.turnBack();
		robotRotation_degrees += 180;
		robotRotation_degrees %= 360;
	}
	
}
