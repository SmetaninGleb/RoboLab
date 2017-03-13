package ru.li24robotics.ev3.robolab;

import ru.li24robotics.ev3.robolab.lab.LabAnalyzer;
import ru.li24robotics.ev3.robolab.lab.LabItem;

import java.util.ArrayList;

public class MainTestClass {
	
	public static void main(String args[]){
		LabItem startItem = new LabItem("0");
		startItem.toLeft.setNothingAboutWallHere(true);
		LabAnalyzer.InitLabAnalyzer(startItem);
		LabItem startItem2 = new LabItem("0");
		startItem2.toLeft.setWallIsHere(true);
		LabAnalyzer.addItemToBack(startItem2);

		ArrayList<ArrayList<LabItem>> field = new ArrayList<>();
		for(int i = 0; i < 8; i ++){
			field.add(i, new ArrayList<LabItem>());
			for(int j = 0; j < 4; j ++){
				field.get(i).add(j, new LabItem("1"));
			}
		}
		field.get(1).get(2).toLeft.setWallIsHere(true);
		field.get(0).get(2).toRight.setWallIsHere(true);

		field.get(2).get(2).toBack.setWallIsHere(true);
		field.get(2).get(1).toForward.setWallIsHere(true);

		field.get(6).get(2).toForward.setWallIsHere(true);
		field.get(6).get(3).toBack.setWallIsHere(true);

		field.get(6).get(1).toLeft.setWallIsHere(true);
		field.get(5).get(1).toRight.setWallIsHere(true);

		field.get(4).get(1).toBack.setWallIsHere(true);
		field.get(4).get(0).toForward.setWallIsHere(true);

		field.get(4).get(2).toLeft.setWallIsHere(true);
		field.get(3).get(2).toRight.setWallIsHere(true);

		ArrayList<int[]> potentialCors = LabAnalyzer.getRobotCoordinatesOnMainLab(field);

		for(int i = 0; i < potentialCors.size(); i ++){
			System.out.print("[" + potentialCors.get(i)[0] + " " + potentialCors.get(i)[1] + "]");
		}
		
		System.out.println();

		System.out.println("ok");
	}
	
}
