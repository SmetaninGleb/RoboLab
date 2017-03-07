package ru.li24robotics.ev3.robolab;

import ru.li24robotics.ev3.robolab.lab.LabAnalyzer;
import ru.li24robotics.ev3.robolab.lab.LabItem;

public class MainTestClass {
	
	public static void main(String args[]){
		LabItem startItem = new LabItem("0");
		LabAnalyzer.InitLabAnalyzer(startItem);

		
		LabAnalyzer.outField();
		
		System.out.println("ok");
	}
	
}
