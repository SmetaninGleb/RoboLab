package ru.li24robotics.ev3.robolab;

import ru.li24robotics.ev3.robolab.lab.LabAnalyzer;
import ru.li24robotics.ev3.robolab.lab.LabItem;

public class MainTestClass {
	
	public static void main(String args[]){
		LabAnalyzer.InitLabAnalyzer("None");
		LabAnalyzer.addItemToForward(new LabItem("1"));
		LabAnalyzer.addItemToBack(new LabItem("1"));
		LabAnalyzer.addItemToRight(new LabItem("1"));
		LabAnalyzer.addItemToLeft(new LabItem("1"));
		LabAnalyzer.putRobotToLeft();
		LabAnalyzer.addItemToForward(new LabItem("1"));
		LabAnalyzer.addItemToBack(new LabItem("1"));
		LabAnalyzer.addItemToLeft(new LabItem("1"));
		
		LabAnalyzer.outField();
		
		System.out.println("ok");
	}
	
}
