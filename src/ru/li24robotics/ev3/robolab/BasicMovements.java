package ru.li24robotics.ev3.robolab;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class BasicMovements {

	public static void main(String[] args) {
		RegulatedMotor m = new EV3LargeRegulatedMotor(MotorPort.A); 
		m.rotate(360);
//		LCD.drawString("Hello, world!", 0, 0);
	      //Ждём 5000 миллисекунд (5 сек.).
//	    Delay.msDelay(5000);
	}

}
