package ru.li24robotics.ev3.robolab.lab;


import ru.li24robotics.ev3.robolab.robotControl.RobotController;

import java.util.ArrayList;
import java.util.Random;




/**
 * Внимание! Считать, что направление строящегося лабиринта
 * постоянно относительно начального положения робота!
 */
public class LabAnalyzeController {
    private RobotController controller;
    private ArrayList<ArrayList<LabItem>> mainField;
    private int[] mainCoordinates = {0, 0, 0};
    private LabItem nowItem;
    private LabAnalyzeMoveController moveController;

    //Для отслеживания поворота робота при построении лабиринта по часовой стрелке
    private int turnDegreesParameter = 0;

    public LabAnalyzeController(ArrayList<ArrayList<LabItem>> mainField,
                                RobotController controller)
    {
        this.mainField = mainField;
        this.controller = controller;
        this.moveController = new LabAnalyzeMoveController(controller);
    }

    public int[] Analyze()
    {
        buildLabAround_one_first();
        while(!isKnowCoordinates())
        {
            goAndBuildNextIteration();
        }

        takeCoordinates();
        return mainCoordinates;
    }

    private void goAndBuildNextIteration()
    {
        switch(turnDegreesParameter)
        {
            case 0:
                goAndBuildNextIteration_0Degrees();
                break;
            case 90:
                goAndBuildNextIteration_90Degrees();
                break;
            case 180:
                goAndBuildNextIteration_180Degrees();
                break;
            case 270:
                goAndBuildNextIteration_270Degrees();
                break;
        }
    }

    private void goAndBuildNextIteration_0Degrees()
    {
        if(!lookForward() && !LabAnalyzer.wasAtForward())
        {
            goAndBuildForward();
        }
        else if(!lookLeft() && !lookRight() && !LabAnalyzer.wasAtLeft() && !LabAnalyzer.wasAtRight())
        {
            int _randPoint;
            final Random _rand = new Random();
            _randPoint = _rand.nextInt(2);
            if(_randPoint == 0)
            {
                goAndBuildRight();
            }
            else {
                goAndBuildLeft();
            }
        }
        else if(!lookRight() && !LabAnalyzer.wasAtRight())
        {
            goAndBuildRight();
        }
        else if(!lookLeft() && !LabAnalyzer.wasAtLeft())
        {
            goAndBuildLeft();
        }
        else if(!lookBack() && !LabAnalyzer.wasAtBack())
        {
            goAndBuildBack();
        }
        else {
            goAndBuildNextIterationRobotWas_0Degrees();
        }
    }

    private void goAndBuildNextIterationRobotWas_0Degrees()
    {
        if(!lookForward())
        {
            goAndBuildRobotWasForward();
        }
        else if(!lookLeft() && !lookRight())
        {
            int _randPoint;
            final Random _rand = new Random();
            _randPoint = _rand.nextInt(2);
            if(_randPoint == 0)
            {
                goAndBuildRobotWasRight();
            }
            else {
                goAndBuildRobotWasLeft();
            }
        }
        else if(!lookRight())
        {
            goAndBuildRobotWasRight();
        }
        else if(!lookLeft())
        {
            goAndBuildRobotWasLeft();
        }
        else if(!lookBack())
        {
            goAndBuildRobotWasBack();
        }
        else {
            System.out.println("Я в ***ном тупике, с*ки!");
        }
    }

    private void goAndBuildNextIteration_90Degrees()
    {
        if(!lookRight() && !LabAnalyzer.wasAtRight())
        {
            goAndBuildRight();
        }
        else if(!lookForward() && !lookBack() && !LabAnalyzer.wasAtForward() && !LabAnalyzer.wasAtBack())
        {
            int _randPoint;
            final Random _rand = new Random();
            _randPoint = _rand.nextInt(2);
            if(_randPoint == 0)
            {
                goAndBuildForward();
            }
            else {
                goAndBuildBack();
            }
        }
        else if(!lookForward() && !LabAnalyzer.wasAtForward())
        {
            goAndBuildForward();
        }
        else if(!lookBack() && !LabAnalyzer.wasAtBack())
        {
            goAndBuildBack();
        }
        else if(!lookLeft() && !LabAnalyzer.wasAtLeft())
        {
            goAndBuildLeft();
        }
        else {
            goAndBuildNextIterationRobotWas_90Degrees();
        }
    }

    private void goAndBuildNextIterationRobotWas_90Degrees()
    {
        if(!lookRight())
        {
            goAndBuildRobotWasRight();
        }
        else if(!lookForward() && !lookBack())
        {
            int _randPoint;
            final Random _rand = new Random();
            _randPoint = _rand.nextInt(2);
            if(_randPoint == 0)
            {
                goAndBuildRobotWasForward();
            }
            else {
                goAndBuildRobotWasBack();
            }
        }
        else if(!lookForward())
        {
            goAndBuildRobotWasForward();
        }
        else if(!lookBack())
        {
            goAndBuildRobotWasBack();
        }
        else if(!lookLeft())
        {
            goAndBuildRobotWasLeft();
        }
        else {
            System.out.println("Я в ***ном тупике, с*ки!");
        }
    }

    private void goAndBuildNextIteration_180Degrees()
    {
        if(!lookBack() && !LabAnalyzer.wasAtBack())
        {
            goAndBuildBack();
        }
        else if(!lookLeft() && !lookRight() && !LabAnalyzer.wasAtLeft() && !LabAnalyzer.wasAtRight())
        {
            int _randPoint;
            final Random _rand = new Random();
            _randPoint = _rand.nextInt(2);
            if(_randPoint == 0)
            {
                goAndBuildRight();
            }
            else {
                goAndBuildLeft();
            }
        }
        else if(!lookRight() && !LabAnalyzer.wasAtRight())
        {
            goAndBuildRight();
        }
        else if(!lookLeft() && !LabAnalyzer.wasAtLeft())
        {
            goAndBuildLeft();
        }
        else if(!lookForward() && !LabAnalyzer.wasAtForward())
        {
            goAndBuildForward();
        }
        else {

            goAndBuildNextIterationRobotWas_180Degrees();
        }
    }

    private void goAndBuildNextIterationRobotWas_180Degrees()
    {
        if(!lookBack())
        {
            goAndBuildRobotWasBack();
        }
        else if(!lookLeft() && !lookRight())
        {
            int _randPoint;
            final Random _rand = new Random();
            _randPoint = _rand.nextInt(2);
            if(_randPoint == 0)
            {
                goAndBuildRobotWasRight();
            }
            else {
                goAndBuildRobotWasLeft();
            }
        }
        else if(!lookRight())
        {
            goAndBuildRobotWasRight();
        }
        else if(!lookLeft())
        {
            goAndBuildRobotWasLeft();
        }
        else if(!lookForward())
        {
            goAndBuildRobotWasForward();
        }
        else {
            System.out.println("Я в ***ном тупике, с*ки!");
        }
    }

    private void goAndBuildNextIteration_270Degrees()
    {
        if(!lookLeft() && !LabAnalyzer.wasAtLeft())
        {
            goAndBuildLeft();
        }
        else if(!lookForward() && !lookBack() && !LabAnalyzer.wasAtForward() && !LabAnalyzer.wasAtBack())
        {
            int _randPoint;
            final Random _rand = new Random();
            _randPoint = _rand.nextInt(2);
            if(_randPoint == 0)
            {
                goAndBuildForward();
            }
            else {
                goAndBuildBack();
            }
        }
        else if(!lookForward() && !LabAnalyzer.wasAtForward())
        {
            goAndBuildForward();
        }
        else if(!lookBack() && !LabAnalyzer.wasAtBack())
        {
            goAndBuildBack();
        }
        else if(!lookRight() && !LabAnalyzer.wasAtRight())
        {
            goAndBuildRight();
        }
        else {
            goAndBuildNextIterationRobotWas_270Degrees();
        }
    }

    private void goAndBuildNextIterationRobotWas_270Degrees()
    {
        if(!lookLeft())
        {
            goAndBuildRobotWasLeft();
        }
        else if(!lookForward() && !lookBack())
        {
            int _randPoint;
            final Random _rand = new Random();
            _randPoint = _rand.nextInt(2);
            if(_randPoint == 0)
            {
                goAndBuildRobotWasForward();
            }
            else {
                goAndBuildRobotWasBack();
            }
        }
        else if(!lookForward())
        {
            goAndBuildRobotWasForward();
        }
        else if(!lookBack())
        {
            goAndBuildRobotWasBack();
        }
        else if(!lookRight())
        {
            goAndBuildRobotWasRight();
        }
        else {
            System.out.println("Я в ***ном тупике, с*ки!");
        }
    }

    private void goAndBuildForward()
    {
        int _checkCount = LabAnalyzer.countUnknownCheckForward();
        goForwardForChecks(_checkCount);
        controller.resetMoveCount();
        int _movedCheckCount = 0;
        while(moveController.isAlive())
        {
            if(controller.getCheckMoved() != _movedCheckCount)
            {
                buildLabAround_one();
                _movedCheckCount = controller.getCheckMoved();
            }
        }
    }
    private void goAndBuildRight()
    {
        int _checkCount = LabAnalyzer.countUnknownCheckRight();
        goRightForChecks(_checkCount);
        controller.resetMoveCount();
        int _movedCheckCount = 0;
        while(moveController.isAlive())
        {
            if(controller.getCheckMoved() != _movedCheckCount)
            {
                buildLabAround_one();
                _movedCheckCount = controller.getCheckMoved();
            }
        }
    }
    private void goAndBuildLeft()
    {
        int _checkCount = LabAnalyzer.countUnknownCheckLeft();
        goLeftForChecks(_checkCount);
        controller.resetMoveCount();
        int _movedCheckCount = 0;
        while(moveController.isAlive())
        {
            if(controller.getCheckMoved() != _movedCheckCount)
            {
                buildLabAround_one();
                _movedCheckCount = controller.getCheckMoved();
            }
        }
    }
    private void goAndBuildBack()
    {
        int _checkCount = LabAnalyzer.countUnknownCheckBack();
        goBackForChecks(_checkCount);
        controller.resetMoveCount();
        int _movedCheckCount = 0;
        while(moveController.isAlive())
        {
            if(controller.getCheckMoved() != _movedCheckCount)
            {
                buildLabAround_one();
                _movedCheckCount = controller.getCheckMoved();
            }
        }
    }
    private void goAndBuildRobotWasForward()
    {
        goForwardToWall();
        controller.resetMoveCount();
        int _movedCheckCount = 0;
        while(moveController.isAlive())
        {
            if(controller.getCheckMoved() != _movedCheckCount)
            {
                buildLabAround_one();
                _movedCheckCount = controller.getCheckMoved();
                LabAnalyzer.putRobotToForward();
            }
        }
    }
    private void goAndBuildRobotWasRight()
    {
        goRightToWall();
        controller.resetMoveCount();
        int _movedCheckCount = 0;
        while(moveController.isAlive())
        {
            if(controller.getCheckMoved() != _movedCheckCount)
            {
                buildLabAround_one();
                _movedCheckCount = controller.getCheckMoved();
                LabAnalyzer.putRobotToRight();
            }
        }
    }
    private void goAndBuildRobotWasLeft()
    {
        goLeftToWall();
        controller.resetMoveCount();
        int _movedCheckCount = 0;
        while(moveController.isAlive())
        {
            if(controller.getCheckMoved() != _movedCheckCount)
            {
                buildLabAround_one();
                _movedCheckCount = controller.getCheckMoved();
                LabAnalyzer.putRobotToLeft();
            }
        }
    }
    private void goAndBuildRobotWasBack()
    {
        goBackToWall();
        controller.resetMoveCount();
        int _movedCheckCount = 0;
        while(moveController.isAlive())
        {
            if(controller.getCheckMoved() != _movedCheckCount)
            {
                buildLabAround_one();
                _movedCheckCount = controller.getCheckMoved();
                LabAnalyzer.putRobotToBack();
            }
        }
    }
    private void goForwardForChecks(int countCheck)
    {
        switch (turnDegreesParameter)
        {
            case 90:
                turnLeft();
                break;
            case 180:
                turnBack();
                break;
            case 270:
                turnRight();
                break;
        }
        for(int i = 0; i < countCheck; i++)
        {
            LabAnalyzer.putRobotToForward();
        }
        moveController.startForwardForChecks(countCheck);
    }

    private void goRightForChecks(int countCheck)
    {
        switch(turnDegreesParameter)
        {
            case 0:
                turnRight();
                break;
            case 180:
                turnLeft();
                break;
            case 270:
                turnBack();
                break;
        }
        for(int i = 0; i < countCheck; i++)
        {
            LabAnalyzer.putRobotToRight();
        }
        moveController.startForwardForChecks(countCheck);
    }

    private void goLeftForChecks(int countCheck)
    {
        switch(turnDegreesParameter)
        {
            case 0:
                turnLeft();
                break;
            case 90:
                turnBack();
                break;
            case 180:
                turnRight();
                break;
        }
        for(int i = 0; i < countCheck; i++)
        {
            LabAnalyzer.putRobotToLeft();
        }
        moveController.startForwardForChecks(countCheck);
    }

    private void goBackForChecks(int countCheck)
    {
        switch(turnDegreesParameter)
        {
            case 0:
                turnBack();
                break;
            case 90:
                turnRight();
                break;
            case 270:
                turnLeft();
                break;
        }
        for(int i = 0; i < countCheck; i++)
        {
            LabAnalyzer.putRobotToBack();
        }
        moveController.startForwardForChecks(countCheck);
    }

    private void goForwardToWall()
    {
        switch (turnDegreesParameter)
        {
            case 90:
                turnLeft();
                break;
            case 180:
                turnBack();
                break;
            case 270:
                turnRight();
                break;
        }
        moveController.startForwardToWall();
    }

    private void goRightToWall()
    {
        switch(turnDegreesParameter)
        {
            case 0:
                turnRight();
                break;
            case 180:
                turnLeft();
                break;
            case 270:
                turnBack();
                break;
        }
        moveController.startForwardToWall();
    }

    private void goLeftToWall()
    {
        switch(turnDegreesParameter)
        {
            case 0:
                turnLeft();
                break;
            case 90:
                turnBack();
                break;
            case 180:
                turnRight();
                break;
        }
        moveController.startForwardToWall();
    }

    private void goBackToWall()
    {
        switch(turnDegreesParameter)
        {
            case 0:
                turnBack();
                break;
            case 90:
                turnRight();
                break;
            case 270:
                turnLeft();
                break;
        }
        moveController.startForwardToWall();
    }

    private void turnRight()
    {
        turnDegreesParameter += 90;
        turnDegreesParameter %= 360;
        controller.turnRight();
    }

    private void turnLeft()
    {
        turnDegreesParameter += 270;
        turnDegreesParameter %= 360;
        controller.turnLeft();
    }

    private void turnBack()
    {
        turnDegreesParameter += 180;
        turnDegreesParameter %= 360;
        controller.turnBack();
    }

    private void buildLabAround_one_first()
    {
    	buildOneAtForward();
    	buildOneAtRight();
    	buildOneAtLeft();    	
    }
    
    private void buildLabAround_one()
    {
    	buildOneAtForward();
    	buildOneAtRight();
    	buildOneAtBack();
    	buildOneAtLeft();
    }
    
    private void buildOneAtForward()
    {
    	LabItem _now = new LabItem("1");
    	_now.toForward.setWallIsHere(lookForward());
    	LabAnalyzer.addItemToCurrent(_now);
    	if(!lookForward())
    	{
    		LabItem _nowForward = new LabItem("1");
    		_nowForward.toBack.setWallIsHere(false);
    		LabAnalyzer.addItemToForward(_nowForward);
    	}
    }
    
    private void buildOneAtRight()
    {
    	LabItem _now = new LabItem("2");
    	_now.toRight.setWallIsHere(lookRight());
    	LabAnalyzer.addItemToCurrent(_now);
    	if(!lookRight())
    	{
    		LabItem _nowRight = new LabItem("1");
    		_nowRight.toLeft.setWallIsHere(false);
    		LabAnalyzer.addItemToRight(_nowRight);
    	}
    }
    
    private void buildOneAtLeft()
    {
    	LabItem _now = new LabItem("3");
    	_now.toLeft.setWallIsHere(lookLeft());
    	LabAnalyzer.addItemToCurrent(_now);
    	if(!lookLeft())
    	{
    		LabItem _nowLeft= new LabItem("1");
    		_nowLeft.toRight.setWallIsHere(false);
    		LabAnalyzer.addItemToLeft(_nowLeft);
    	}
    }
    
    private void buildOneAtBack()
    {
    	LabItem _now = new LabItem("4");
    	_now.toBack.setWallIsHere(lookBack());
    	LabAnalyzer.addItemToCurrent(_now);
    	if(!lookBack())
    	{
    		LabItem _nowBack = new LabItem("1");
    		_nowBack.toForward.setWallIsHere(false);
    		LabAnalyzer.addItemToBack(_nowBack);
    	}
    }

    private boolean lookForward()
    {
    	switch(turnDegreesParameter)
    	{
    		case 0:
    			return controller.isWallNearForward();
    		case 90:
    			return controller.isWallNearLeft();
    		case 180:
    			return controller.isWallNearBack();
    		case 270:
    			return controller.isWallNearRight();
    	}
    	return false;
    }
    
    private boolean lookRight()
    {
    	switch(turnDegreesParameter)
    	{
    		case 0:
    			return controller.isWallNearRight();
    		case 90:
    			return controller.isWallNearForward();
    		case 180:
    			return controller.isWallNearLeft();
    		case 270:
    			return controller.isWallNearBack();
    	}
    	return false;
    }

    private boolean lookLeft()
    {
    	switch(turnDegreesParameter)
    	{
    		case 0:
    			return controller.isWallNearLeft();
    		case 90:
    			return controller.isWallNearBack();
    		case 180:
    			return controller.isWallNearRight();
    		case 270:
    			return controller.isWallNearForward();
    	}
    	return false;
    }
    
    private boolean lookBack()
    {
    	switch(turnDegreesParameter)
    	{
    		case 0:
    			return controller.isWallNearBack();
    		case 90:
    			return controller.isWallNearRight();
    		case 180:
    			return controller.isWallNearForward();
    		case 270:
    			return controller.isWallNearLeft();
    	}
    	return false;
    }

    private boolean isKnowCoordinates()
    {
        if (LabAnalyzer.getRobotCoordinatesOnMainLab(mainField).size() == 1)
        {
            return true;
        }
        else {
            return false;
        }
    }

    private void takeCoordinates()
    {
        if(isKnowCoordinates()) {
            mainCoordinates = LabAnalyzer.getRobotCoordinatesOnMainLab(mainField).get(0);
            mainCoordinates[2] = (mainCoordinates[2] + turnDegreesParameter) % 360;
        }
    }
}
