package ru.li24robotics.ev3.robolab;

import java.io.IOException;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
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
		try {
			LabFileParser.InitLabFileParser();
			lc = new LabAnalyzeController(LabFileParser.getMainLab(), c);
		} 
		catch(IOException e) {
			
		}
		catch (ClassNotFoundException e) {
			
		}
		int[] ans = lc.Analyze();
		System.out.println("Analyzed!!!");
		System.out.println(ans[0] + " " + ans[1] + " " + ans[2]);
		Delay.msDelay(5000);
		LabAnalyzer.out.close();
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
