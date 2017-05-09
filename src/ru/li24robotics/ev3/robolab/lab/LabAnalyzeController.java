package ru.li24robotics.ev3.robolab.lab;


import ru.li24robotics.ev3.robolab.robotControl.IRobotController;

import java.util.ArrayList;
import java.util.Random;

import lejos.utility.Delay;


/**
 * Внимание! Считать, что направление строящегося лабиринта
 * постоянно относительно начального положения робота!
 */
public class LabAnalyzeController {
    IRobotController controller;
    ArrayList<ArrayList<LabItem>> mainField;
    int[] mainCoordinates = {0, 0, 0};
    LabItem nowItem;
    StandardItems sItems = new StandardItems();

    //Для отслеживания поворота робота при построении лабиринта по часовой стрелке
    private int turnDegreesParameter = 0;

    public LabAnalyzeController(ArrayList<ArrayList<LabItem>> mainField,
                                IRobotController controller)
    {
        this.mainField = mainField;
        this.controller = controller;
    }

    public int[] Analyze()
    {
        buildLabAround_one_first();
        LabAnalyzer.outField();
        int i = 0;
        while(!isKnowCoordinates())
        {
        	i++;
        	goNextIterationForOneCheck();
        	buildLabAround_one();
            LabAnalyzer.outField();
            if(i == 4) LabAnalyzer.out.close();
        }

        takeCoordinates();
        return mainCoordinates;
    }

    private void goNextIteration()
    {
        if(!controller.isWallNearForward())
        {
            goForwardToWall();
            return;
        }
        else if(!controller.isWallNearRight())
        {
            goRightToWall();
        }
        else if(!controller.isWallNearLeft())
        {
            goLeftToWall();
        }
        else if(!controller.isWallNearBack())
        {
            goBackToWall();
        }
        else {
            System.out.println("Я в ***ном тупике, с*ки!");
        }
    }
    
    
    
    private void goNextIterationForOneCheck()
    {
    	switch(turnDegreesParameter)
    	{
    		case 0:
    			goNextIterationForOneCheck_0Degrees();
    			break;
    		case 90:
    			goNextIterationForOneCheck_90Degrees();
    			break;
    		case 180:
    			goNextIterationForOneCheck_180Degrees();
    			break;
    		case 270:
    			goNextIterationForOneCheck_270Degrees();
    			break;
    	}
    }
    
    private void goNextIterationForOneCheck_0Degrees()
    {
    	if(!lookForward())
    	{
    		goToForward();
    	}
    	else if(!lookLeft() && !lookRight())
    	{
    		int _randPoint;
    		final Random _rand = new Random();
    		_randPoint = _rand.nextInt(1);
    		if(_randPoint == 0)
    		{
    			goToRight();
    		}
    		else {
    			goToLeft();
    		}
    	}
    	else if(!lookRight())
    	{
    		goToRight();
    	}
    	else if(!lookLeft())
    	{
    		goToLeft();
    	}
    	else if(!lookBack())
    	{
    		goToBack();
    	}
    	else {
    		System.out.println("Я в ***ном тупике, с*ки!");
    	}
    }
    private void goNextIterationForOneCheck_90Degrees()
    {
    	if(!lookRight())
    	{
    		goToRight();
    	}
    	else if(!lookForward() && !lookBack())
    	{
    		int _randPoint;
    		final Random _rand = new Random();
    		_randPoint = _rand.nextInt(1);
    		if(_randPoint == 0)
    		{
    			goToForward();
    		}
    		else {
    			goToBack();
    		}
    	}
    	else if(!lookForward())
    	{
    		goToForward();
    	}
    	else if(!lookBack())
    	{
    		goToBack();
    	}
    	else if(!lookLeft())
    	{
    		goToLeft();
    	}
    	else {
    		System.out.println("Я в ***ном тупике, с*ки!");
    	}
    }
    private void goNextIterationForOneCheck_180Degrees()
    {
    	if(!lookBack())
    	{
    		goToBack();
    	}
    	else if(!lookLeft() && !lookRight())
    	{
    		int _randPoint;
    		final Random _rand = new Random();
    		_randPoint = _rand.nextInt(1);
    		if(_randPoint == 0)
    		{
    			goToRight();
    		}
    		else {
    			goToLeft();
    		}
    	}
    	else if(!lookRight())
    	{
    		goToRight();
    	}
    	else if(!lookLeft())
    	{
    		goToLeft();
    	}
    	else if(!lookForward())
    	{
    		goToForward();
    	}
    	else {
    		System.out.println("Я в ***ном тупике, с*ки!");
    	}
    }
    private void goNextIterationForOneCheck_270Degrees()
    {
    	if(!lookLeft())
    	{
    		goToLeft();
    	}
    	else if(!lookForward() && !lookBack())
    	{
    		int _randPoint;
    		final Random _rand = new Random();
    		_randPoint = _rand.nextInt(1);
    		if(_randPoint == 0)
    		{
    			goToForward();
    		}
    		else {
    			goToBack();
    		}
    	}
    	else if(!lookForward())
    	{
    		goToForward();
    	}
    	else if(!lookBack())
    	{
    		goToBack();
    	}
    	else if(!lookRight())
    	{
    		goToRight();
    	}
    	else {
    		System.out.println("Я в ***ном тупике, с*ки!");
    	}
    }

    private void goForwardToWall()
    {
        int _checkCountToWall = lookForward_count();
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
        controller.forwardToWall();
        for(int i = 0; i < _checkCountToWall; i++)
        {
            LabAnalyzer.putRobotToForward();        }
    }

    private void goRightToWall()
    {
        int _checkCountToWall = lookRight_count();
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
        controller.forwardToWall();
        for(int i = 0; i < _checkCountToWall; i++)
        {
            LabAnalyzer.putRobotToRight();
        }
    }

    private void goLeftToWall()
    {
        int _checkCountToWall = lookLeft_count();
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
        controller.forwardToWall();
        for(int i = 0; i < _checkCountToWall; i++)
        {
            LabAnalyzer.putRobotToLeft();
        }
    }

    private void goBackToWall()
    {
        int _checkCountToWall = lookBack_count();
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
        controller.forwardToWall();
        for(int i = 0; i < _checkCountToWall; i++)
        {
            LabAnalyzer.putRobotToBack();
        }
    }
    
    private void goToForward()
    {
    	switch(turnDegreesParameter)
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
    	controller.forwardForChecks(1);
    	LabAnalyzer.putRobotToForward();
    }
    
    private void goToRight()
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
    	controller.forwardForChecks(1);
    	LabAnalyzer.putRobotToRight();
    }
    
    private void goToLeft()
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
    	controller.forwardForChecks(1);
    	LabAnalyzer.putRobotToLeft();
    }
    
    private void goToBack()
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
    	controller.forwardForChecks(1);
    	LabAnalyzer.putRobotToBack();
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

    private void buildLabAround()
    {
        buildLabAtForward();
        buildLabAtRight();
        buildLabAtBack();
        buildLabAtLeft();
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

    private void buildLabAtForward()
    {
        int _checkCount = lookForward_count();
        nowItem = sItems.f_mid;
        for(int i = 0; i < _checkCount - 1; i++)
        {
            putItemToForward();
        }
        nowItem = sItems.f_end;
        putItemToForward();
    }

    private void buildLabAtRight()
    {
        int _checkCount = lookRight_count();
        nowItem = sItems.r_mid;
        for(int i = 0; i < _checkCount - 1; i++)
        {
            putItemToRight();
        }
        nowItem = sItems.r_end;
        putItemToRight();
    }

    private void buildLabAtLeft()
    {
        int _checkCount = lookLeft_count();
        nowItem = sItems.l_mid;
        for(int i = 0; i < _checkCount - 1; i++)
        {
            putItemToLeft();
        }
        nowItem = sItems.l_end;
        putItemToLeft();
    }

    private void buildLabAtBack()
    {
        int _checkCount = lookBack_count();
        nowItem = sItems.b_mid;
        for(int i = 0; i < _checkCount - 1; i++)
        {
            putItemToBack();
        }
        nowItem = sItems.b_end;
        putItemToBack();
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

    private int lookForward_count()
    {
        int _checkCount = 0;
        switch (turnDegreesParameter)
        {
            case 0:
                _checkCount = controller.checksCountToWallAtForward();
                break;
            case 90:
                _checkCount = controller.checksCountToWallAtRight();
                break;
            case 180:
                _checkCount = controller.checksCountToWallAtBack();
                break;
            case 270:
                _checkCount = controller.checksCountToWallAtLeft();
                break;
         }
        return _checkCount;
    }

    private int lookRight_count()
    {
        int _checkCount = 0;
        switch (turnDegreesParameter)
        {
            case 0:
                _checkCount = controller.checksCountToWallAtRight();
                break;
            case 90:
                _checkCount = controller.checksCountToWallAtBack();
                break;
            case 180:
                _checkCount = controller.checksCountToWallAtLeft();
                break;
            case 270:
                _checkCount = controller.checksCountToWallAtForward();
                break;
        }
        return _checkCount;
    }

    private int lookBack_count()
    {
        int _checkCount = 0;
        switch (turnDegreesParameter)
        {
            case 0:
                _checkCount = controller.checksCountToWallAtBack();
                break;
            case 90:
                _checkCount = controller.checksCountToWallAtLeft();
                break;
            case 180:
                _checkCount = controller.checksCountToWallAtForward();
                break;
            case 270:
                _checkCount = controller.checksCountToWallAtRight();
                break;
        }
        return _checkCount;
    }

    private int lookLeft_count()
    {
        int _checkCount = 0;
        switch (turnDegreesParameter)
        {
            case 0:
                _checkCount = controller.checksCountToWallAtLeft();
                break;
            case 90:
                _checkCount = controller.checksCountToWallAtForward();
                break;
            case 180:
                _checkCount = controller.checksCountToWallAtRight();
                break;
            case 270:
                _checkCount = controller.checksCountToWallAtBack();
                break;
        }
        return _checkCount;
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
    
    private void putItemToForward()
    {
        LabAnalyzer.addItemToForward(nowItem);
    }

    private void putItemToRight()
    {
        LabAnalyzer.addItemToRight(nowItem);
    }

    private void putItemToBack()
    {
        LabAnalyzer.addItemToBack(nowItem);
    }

    private void putItemToLeft()
    {
        LabAnalyzer.addItemToLeft(nowItem);
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

    class StandardItems
    {
        LabItem f_mid;
        LabItem f_end;
        LabItem b_mid;
        LabItem b_end;
        LabItem r_mid;
        LabItem r_end;
        LabItem l_mid;
        LabItem l_end;

        public StandardItems()
        {
            f_mid = new LabItem("1");
            f_end = new LabItem("1");
            r_mid = new LabItem("1");
            r_end = new LabItem("1");
            l_mid = new LabItem("1");
            l_end = new LabItem("1");
            b_mid = new LabItem("1");
            b_end = new LabItem("1");
            build();
        }

        private void build()
        {
            buildF();
            buildR();
            buildB();
            buildL();
        }

        private void buildF()
        {
            f_mid.toForward.setWallIsHere(false);
            f_mid.toBack.setWallIsHere(false);
            f_end.toBack.setWallIsHere(false);
            f_end.toForward.setWallIsHere(true);
        }
        private void buildR()
        {
            r_mid.toRight.setWallIsHere(false);
            r_mid.toLeft.setWallIsHere(false);
            r_end.toLeft.setWallIsHere(false);
            r_end.toRight.setWallIsHere(true);
        }
        private void buildL()
        {
            l_mid.toLeft.setWallIsHere(false);
            l_mid.toRight.setWallIsHere(false);
            l_end.toRight.setWallIsHere(false);
            l_end.toLeft.setWallIsHere(true);
        }
        private void buildB()
        {
            b_mid.toForward.setWallIsHere(false);
            b_mid.toBack.setWallIsHere(false);
            b_end.toForward.setWallIsHere(false);
            b_end.toBack.setWallIsHere(true);
        }
    }
}
