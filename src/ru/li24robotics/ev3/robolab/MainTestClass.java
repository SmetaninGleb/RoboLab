package ru.li24robotics.ev3.robolab;

import ru.li24robotics.ev3.robolab.lab.LabAnalyzer;
import ru.li24robotics.ev3.robolab.lab.LabFileParser;
import ru.li24robotics.ev3.robolab.lab.LabItem;

import java.io.IOException;
import java.util.ArrayList;

public class MainTestClass {
	
	public static void main(String args[]) throws IOException, ClassNotFoundException {
		LabItem startItem = new LabItem("0");
		startItem.toLeft.setNothingAboutWallHere(true);
		LabAnalyzer.InitLabAnalyzer(startItem);
		LabItem startItem2 = new LabItem("0");
		startItem2.toBack.setWallIsHere(true);
		LabAnalyzer.addItemToRight(startItem2);

		LabFileParser.InitLabFileParser();
		ArrayList<ArrayList<LabItem>> field = LabFileParser.getMainLab();


		ArrayList<int[]> potentialCors = LabAnalyzer.getRobotCoordinatesOnMainLab(field);

		for(int i = 0; i < potentialCors.size(); i ++){
			System.out.print("[" + potentialCors.get(i)[0] + " " + potentialCors.get(i)[1] + "]");
		}


		System.out.println();

		System.out.println("ok");
	}
	
}
