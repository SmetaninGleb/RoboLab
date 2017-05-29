package ru.li24robotics.ev3.robolab.lab;


import ru.li24robotics.ev3.robolab.robotControl.RobotController;

public class LabAnalyzeMoveController extends Thread
{
    private volatile boolean isMoveToWall;
    private volatile boolean isMoveForChecks;
    private RobotController controller;
    private volatile int checksCount;

    public LabAnalyzeMoveController(RobotController controller)
    {
        this.controller = controller;
        isMoveToWall = false;
        isMoveForChecks = false;
    }

    public void startForwardToWall()
    {
        this.isMoveToWall = true;
        this.isMoveForChecks = false;
        this.start();
    }

    public void startForwardForChecks(int checksCount)
    {
        this.isMoveForChecks = true;
        this.isMoveToWall = false;
        this.checksCount = checksCount;
        this.start();
    }

    @Override
    public void run()
    {
        if(isMoveToWall)
        {
            controller.forwardToWall();
        }
        else if(isMoveForChecks)
        {
            controller.forwardForChecks(checksCount);
        }
    }
}
