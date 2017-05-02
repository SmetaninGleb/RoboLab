package ru.li24robotics.ev3.robolab;

import ru.li24robotics.ev3.robolab.lab.LabAnalyzer;
import ru.li24robotics.ev3.robolab.lab.LabFileParser;
import ru.li24robotics.ev3.robolab.lab.LabItem;

import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class MainTestClass {

	public static void main(String args[]) throws IOException, ClassNotFoundException {
		RegulatedMotor m = new EV3LargeRegulatedMotor(MotorPort.A);
		m.setAcceleration(360);
		m.forward();
		while(m.getTachoCount() < 1000)
		{
			System.out.println(m.getRotationSpeed());
			System.out.println(m.getTachoCount());
			if(m.getRotationSpeed() == 360) {
				m.setSpeed(360);
			}
			
		}
		m.setSpeed(0);
	}
}
