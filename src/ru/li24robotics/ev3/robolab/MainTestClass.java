package ru.li24robotics.ev3.robolab;

import ru.li24robotics.ev3.robolab.lab.LabAnalyzer;
import ru.li24robotics.ev3.robolab.lab.LabFileParser;
import ru.li24robotics.ev3.robolab.lab.LabItem;
import ru.li24robotics.ev3.robolab.robotControl.RobotController;

import java.io.IOException;
import java.util.ArrayList;

import org.freedesktop.DBus.Local;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class MainTestClass {

	public static void main(String args[])
	{
		RobotController controller = new RobotController(MotorPort.A, MotorPort.D, LocalEV3.get().getPort("S1"),
				LocalEV3.get().getPort("S2"), LocalEV3.get().getPort("S4"), LocalEV3.get().getPort("S3"));
		controller.forwardForChecks(1);
		controller.turnLeft();
		controller.forwardForChecks(1);
		controller.turnLeft();
		Delay.msDelay(3000);
		controller.forwardForChecks(1);
		controller.forwardForChecks(1);
		controller.turnRight();
		controller.forwardForChecks(1);
		controller.forwardForChecks(1);
		controller.turnBack();
		controller.forwardForChecks(1);
		controller.forwardForChecks(1);
		controller.turnRight();
		controller.forwardForChecks(1);
		controller.forwardForChecks(1);
		controller.turnLeft();
		controller.forwardForChecks(1);
		controller.forwardForChecks(1);
		
	}
}
