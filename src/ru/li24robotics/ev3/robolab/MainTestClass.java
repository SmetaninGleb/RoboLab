package ru.li24robotics.ev3.robolab;

import ru.li24robotics.ev3.robolab.lab.LabAnalyzer;
import ru.li24robotics.ev3.robolab.lab.LabFileParser;
import ru.li24robotics.ev3.robolab.lab.LabItem;
import ru.li24robotics.ev3.robolab.robotControl.RobotController;

import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class MainTestClass {

	public static void main(String args[])
	{
		RobotController c = new RobotController(MotorPort.A, MotorPort.B); 
		c.forwardForChecks(4);
//		EV3LargeRegulatedMotor m = new EV3LargeRegulatedMotor(MotorPort.A);
//		m.setSpeed(2000);
//		m.forward();
//		m.setAcceleration(360);
//		while(true)
//		{
//			System.out.println(m.getRotationSpeed());
//			if(m.getRotationSpeed() >= 720)
//			{
//				m.setSpeed(0);
//			}
//		}
	}
}
