package ru.li24robotics.ev3.robolab.lab;


import ru.li24robotics.ev3.robolab.robotControl.IRobotController;

import java.util.ArrayList;

public class LabAnalyzeController {
    IRobotController controller;
    ArrayList<ArrayList<LabItem>> mainField;
    int[] mainCoordinates = {0,0};
    LabItem labItemLeftMiddle;
    LabItem labItemLeftEnd;
    LabItem labItemRightMiddle;
    LabItem labItemRightEnd;
    LabItem labItemForwardMiddle;
    LabItem labItemForwardEnd;
    LabItem labItemBackMiddle;
    LabItem labItemBackEnd;


    public LabAnalyzeController(ArrayList<ArrayList<LabItem>> mainField, IRobotController controller){
        this.mainField = mainField;
        this.controller = controller;
    }

    public int[] Analyze(LabItem startLabItem){
        LabAnalyzer.InitLabAnalyzer(startLabItem);
        lookAroundFirst();
        if(isWeKnowCoordinates()){
            mainCoordinates = LabAnalyzer.getRobotCoordinatesOnMainLab(mainField).get(1);
            return mainCoordinates;
        }



        return mainCoordinates;
    }

    private void lookAroundFirst(){
        lookForwardFirst();
        lookBackFirst();
        lookRightFirst();
        lookLeftFirst();
    }
    private void lookForwardFirst(){
        labItemForwardMiddle = new LabItem("1");
        labItemForwardMiddle.toForward.setWallIsHere(false);
        labItemForwardMiddle.toBack.setWallIsHere(false);
        LabItem labItemForwardEnd = new LabItem("1");
        labItemForwardEnd.toBack.setWallIsHere(false);
        labItemForwardEnd.toForward.setWallIsHere(true);

        for(int i = 0; i < controller.checksCountToWallAtForward() - 1; i++){
            LabAnalyzer.addItemToForward(labItemForwardMiddle);
        }
        LabAnalyzer.addItemToForward(labItemForwardEnd);
    }
    private void lookBackFirst(){
        labItemBackMiddle = new LabItem("1");
        labItemBackMiddle.toForward.setWallIsHere(false);
        labItemBackMiddle.toBack.setWallIsHere(false);
        LabItem labItemBackEnd = new LabItem("1");
        labItemBackEnd.toBack.setWallIsHere(true);
        labItemBackEnd.toForward.setWallIsHere(false);

        for(int i = 0; i < controller.checksCountToWallAtBack() - 1; i++){
            LabAnalyzer.addItemToBack(labItemBackMiddle);
        }
        LabAnalyzer.addItemToBack(labItemBackEnd);
    }
    private void lookRightFirst(){
        labItemRightMiddle = new LabItem("1");
        labItemRightMiddle.toRight.setWallIsHere(false);
        labItemRightMiddle.toLeft.setWallIsHere(false);
        LabItem labItemRightEnd = new LabItem("1");
        labItemRightEnd.toRight.setWallIsHere(true);
        labItemRightEnd.toLeft.setWallIsHere(false);

        for(int i = 0; i < controller.checksCountToWallAtRight() - 1; i++){
            LabAnalyzer.addItemToRight(labItemRightMiddle);
        }
        LabAnalyzer.addItemToRight(labItemRightEnd);
    }
    private void lookLeftFirst(){
        labItemLeftMiddle = new LabItem("1");
        labItemLeftMiddle.toLeft.setWallIsHere(false);
        labItemLeftMiddle.toRight.setWallIsHere(false);
        labItemLeftEnd = new LabItem("1");
        labItemLeftEnd.toLeft.setWallIsHere(true);
        labItemLeftEnd.toRight.setWallIsHere(false);

        for(int i = 0; i < controller.checksCountToWallAtLeft() - 1; i++){
            LabAnalyzer.addItemToLeft(labItemLeftMiddle);
        }
        LabAnalyzer.addItemToLeft(labItemLeftEnd);
    }

    private boolean isWeKnowCoordinates(){
        if(LabAnalyzer.getRobotCoordinatesOnMainLab(mainField).size() == 1){
            return true;
        }else{
            return false;
        }
    }

    private void moveForwardForChecks(int checksCount){
        controller.forwardForChecks(checksCount);
        for(int i = 0; i < checksCount; i ++){
            LabAnalyzer.putRobotToForwad();
        }
    }
    private void moveBackForChecks(int checksCount){
        controller.turnBack();
        controller.forwardForChecks(checksCount);
        for(int i = 0; i < checksCount; i ++){
            LabAnalyzer.putRobotToBack();
        }
    }
    private void moveRightForChecks(int checksCount){
        controller.turnRight();
        controller.forwardForChecks(checksCount);
        for(int i = 0; i < checksCount; i ++){
            LabAnalyzer.putRobotToRight();
        }
    }
    private void moveLeftForChecks(int checksCount){
        controller.turnLeft();
        controller.forwardForChecks(checksCount);
        for(int i = 0; i < checksCount; i ++){
            LabAnalyzer.putRobotToForwad();
        }
    }
}
