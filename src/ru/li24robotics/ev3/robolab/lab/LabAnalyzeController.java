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

    private void lookAround(){
        lookForward();
        lookBack();
    }
    private void lookForward(){
        LabItem labItemMiddle = new LabItem("1");
        labItemMiddle.toForward.setWallIsHere(false);
        labItemMiddle.toBack.setWallIsHere(false);
        LabItem labItemEnd = new LabItem("1");
        labItemEnd.toBack.setWallIsHere(false);
        labItemEnd.toForward.setWallIsHere(true);
        for(int i = 0; i < controller.checksCountToWallAtForward() - 1; i++){
            LabAnalyzer.addItemToForward(labItemMiddle);
        }
        LabAnalyzer.addItemToForward(labItemEnd);
    }

    private void lookBack(){
        LabItem labItemMiddle = new LabItem("1");
        labItemMiddle.toForward.setWallIsHere(false);
        labItemMiddle.toBack.setWallIsHere(false);
        LabItem labItemEnd = new LabItem("1");
        labItemEnd.toBack.setWallIsHere(true);
        labItemEnd.toForward.setWallIsHere(false);
        for(int i = 0; i < controller.checksCountToWallAtForward() - 1; i++){
            LabAnalyzer.addItemToBack(labItemMiddle);
        }
        LabAnalyzer.addItemToBack(labItemEnd);
    }
    //TODO realize for right and left!!!
}
