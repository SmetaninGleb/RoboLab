package ru.li24robotics.ev3.robolab;


import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import ru.li24robotics.ev3.robolab.cubeFinder.RouteFileParser;
import ru.li24robotics.ev3.robolab.cubeFinder.RouteFollower;
import ru.li24robotics.ev3.robolab.lab.LabAnalyzeController;
import ru.li24robotics.ev3.robolab.lab.LabAnalyzer;
import ru.li24robotics.ev3.robolab.lab.LabFileParser;
import ru.li24robotics.ev3.robolab.lab.LabItem;
import ru.li24robotics.ev3.robolab.robotControl.RobotController;

public class RobotMainAlgorithmController
{
    private static RobotController robotController;
    private static LabAnalyzeController labAnalyzeController;
    private static RouteFollower routeFollower;
    private static int[] analyzedCoordinates;

    public static void main(String args[])
    {
        Initialization();        
//        for (int i = 0; i < 10; i--)
//        {
//        	robotController.turnRight();
//        	robotController.turnLeft();
//        }
        while(!Button.ENTER.isDown())
        {
            LCD.clear();
            robotController.showStatus();
            Delay.msDelay(200);
        }
        LCD.clear();
//        analyzedCoordinates = labAnalyzeController.Analyze();
//        LCD.drawString("ANALYZED!!!" , 0, 0);
//        LCD.drawString(analyzedCoordinates[0] + " " + analyzedCoordinates[1] + " " + analyzedCoordinates[2], 0, 1);
        analyzedCoordinates = new int[] {0, 0, 0};
        routeFollower.setMainRoute(RouteFileParser.getRouteFromStartCors(analyzedCoordinates));
        routeFollower.setStartRobotRotation_degrees(analyzedCoordinates[2]);
        routeFollower.followRoute();
    }

    private static void Initialization()
    {
        robotController = new RobotController(MotorPort.A, MotorPort.D, LocalEV3.get().getPort("S1"),
                LocalEV3.get().getPort("S2"), LocalEV3.get().getPort("S4"), LocalEV3.get().getPort("S3"));
        LabAnalyzer.InitLabAnalyzer(new LabItem("1"));
        LabFileParser.InitLabFileParser();
        labAnalyzeController = new LabAnalyzeController(LabFileParser.getMainLab(), robotController);
        routeFollower = new RouteFollower(robotController);
    }

}
