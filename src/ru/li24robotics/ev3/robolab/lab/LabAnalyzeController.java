package ru.li24robotics.ev3.robolab.lab;


import ru.li24robotics.ev3.robolab.robotControl.IRobotController;

import java.util.ArrayList;

public class LabAnalyzeController {
    IRobotController controller;
    ArrayList<ArrayList<LabItem>> mainField;

    public LabAnalyzeController(ArrayList<ArrayList<LabItem>> mainField, IRobotController controller){
        this.mainField = mainField;
        this.controller = controller;
    }

    public int[] Analyze(LabItem startLabItem){
        LabAnalyzer.InitLabAnalyzer(startLabItem);
        int[] mainCoordinates = {0,0};


        return mainCoordinates;
    }
}
