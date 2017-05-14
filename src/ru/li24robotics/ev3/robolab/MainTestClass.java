package ru.li24robotics.ev3.robolab;

import java.io.IOException;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
import ru.li24robotics.ev3.robolab.cubeFinder.RouteFileParser;
import ru.li24robotics.ev3.robolab.cubeFinder.RouteFollower;
import ru.li24robotics.ev3.robolab.lab.LabAnalyzeController;
import ru.li24robotics.ev3.robolab.lab.LabAnalyzer;
import ru.li24robotics.ev3.robolab.lab.LabFileParser;
import ru.li24robotics.ev3.robolab.lab.LabItem;
import ru.li24robotics.ev3.robolab.robotControl.RobotController;

public class MainTestClass {

	public static void main(String args[])
	{
		RobotController c = new RobotController(MotorPort.A, MotorPort.D, LocalEV3.get().getPort("S1"),
				LocalEV3.get().getPort("S2"), LocalEV3.get().getPort("S4"), LocalEV3.get().getPort("S3"));
		
		LabAnalyzer.InitLabAnalyzer(new LabItem("1"));
		LabAnalyzeController lc = null;
		LabFileParser.InitLabFileParser();
		lc = new LabAnalyzeController(LabFileParser.getMainLab(), c);
		int[] ans = lc.Analyze();
		System.out.println("Analyzed!!!");
		System.out.println(ans[0] + " " + ans[1] + " " + ans[2]);
		LabAnalyzer.outField();
		RouteFollower rf = new RouteFollower(c);
		System.out.println(RouteFileParser.getRouteFromStartCors(ans).getRouteSize());
		rf.setMainRoute(RouteFileParser.getRouteFromStartCors(ans));
		rf.setStartRobotRotation_degrees(ans[2]);
		rf.followRoute();
		Delay.msDelay(10000);
		LabAnalyzer.out.close();
		
//		RegulatedMotor m = new EV3LargeRegulatedMotor(MotorPort.D);
//		m.setSpeed(100);
//		m.forward();
//		Delay.msDelay(5000);
//		
		
//		c.turnBack();
//		c.forwardForChecks(1);
//		c.turnLeft();
//		c.forwardForChecks(1);
//		c.forwardForChecks(1);
//		c.forwardForChecks(1);
//		c.turnLeft();
//		c.forwardForChecks(1);
//		c.forwardForChecks(1);
//		c.turnRight();
//		c.forwardForChecks(1);
//		c.turnLeft();
//		c.forwardForChecks(1);
//		c.turnLeft();
//		c.forwardForChecks(1);
//		c.turnBack();
//		c.forwardForChecks(1);
//		c.turnRight();
//		c.forwardForChecks(1);
//		c.turnRight();
//		c.forwardForChecks(1);
//		c.forwardForChecks(1);
//		c.turnRight();
//		c.forwardForChecks(1);
//		c.turnLeft();
//		c.forwardForChecks(1);
//		c.turnBack();
//		c.forwardForChecks(1);
//		c.turnRight();
//		c.forwardForChecks(1);
//		c.turnRight();
//		c.forwardForChecks(1);
//		c.forwardForChecks(1);
//		c.turnRight();
//		c.forwardForChecks(1);
//		c.turnBack();
//		
	}
}
