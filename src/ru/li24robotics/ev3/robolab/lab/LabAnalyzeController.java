package ru.li24robotics.ev3.robolab.lab;


import ru.li24robotics.ev3.robolab.robotControl.IRobotController;

import java.util.ArrayList;

/**
 * Внимание! Считать, что направление лабиринта
 * постоянно относительно начального положения робота!
 */
public class LabAnalyzeController {
    IRobotController controller;
    ArrayList<ArrayList<LabItem>> mainField;
    int[] mainCoordinates = {0, 0};
    LabItem nowItem;
    StandardItems sItems = new StandardItems();

    //Для отслеживания поворота робота при построении лабиринта
    private int turnDegreesParametr = 0;

    public LabAnalyzeController(ArrayList<ArrayList<LabItem>> mainField, IRobotController controller)
    {
        this.mainField = mainField;
        this.controller = controller;
    }

    public int[] Analyze()
    {

        return mainCoordinates;
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

    private void buildLabAtRigth()
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

    private int lookForward_count()
    {
        int _checkCount = 0;
        switch (turnDegreesParametr)
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
        switch (turnDegreesParametr)
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
        switch (turnDegreesParametr)
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
        switch (turnDegreesParametr)
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

    private boolean isWeKnowCoordinates() {
        if (LabAnalyzer.getRobotCoordinatesOnMainLab(mainField).size() == 1) {
            return true;
        } else {
            return false;
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
