package ru.li24robotics.ev3.robolab;

import ru.li24robotics.ev3.robolab.lab.LabAnalyzer;
import ru.li24robotics.ev3.robolab.lab.LabItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class MainTestClass {
	
	public static void main(String args[]){
		LabItem startItem = new LabItem("0");
		startItem.toLeft = false;
		LabAnalyzer.InitLabAnalyzer(startItem);
		LabItem startItem2 = new LabItem("0");
		startItem2.toBack = false;
		LabAnalyzer.addItemToRight(startItem2);


		ArrayList<ArrayList<LabItem>> field = new ArrayList<>();
		for(int i = 0; i < 8; i ++){
			field.add(i, new ArrayList<>());
			for(int j = 0; j < 4; j ++){
				field.get(i).add(j, new LabItem("1"));
			}
		}
		field.get(1).get(2).toLeft = false;
		field.get(2).get(2).toBack = false;
		field.get(6).get(2).toForward = false;
		field.get(6).get(1).toLeft = false;
		field.get(4).get(1).toBack = false;
		field.get(4).get(2).toLeft = false;

		ArrayList<int[]> potentialCors = LabAnalyzer.getRobotCoordinatesOnMainLab(field);



		for(int i = 0; i < potentialCors.size(); i ++){
			System.out.print("[" + potentialCors.get(i)[0] + " " + potentialCors.get(i)[1] + "]");
		}

		System.out.println("ok");
	}
	
}
