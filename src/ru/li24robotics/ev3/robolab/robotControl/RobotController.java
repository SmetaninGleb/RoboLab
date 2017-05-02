package ru.li24robotics.ev3.robolab.robotControl;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;

public class RobotController implements IRobotController{
	private RegulatedMotor motor_r;
	private RegulatedMotor motor_l;
	private final Double CHECK_LENGHT = 0.3;
	private final Double WHEEL_CIRCUM = 0.024;
	private final int MAX_SPEED = 360;
	
	public RobotController(Port motorPort_r, Port motorPort_l, Port sensorSonicPort_f,
			Port sensorSonicPort_r, Port sensorSonicPort_l)
	{
		motor_r = new EV3LargeRegulatedMotor(motorPort_r);
		motor_l = new EV3LargeRegulatedMotor(motorPort_l);
		motor_r.synchronizeWith(new RegulatedMotor[]{motor_l});
	}
	
    @Override
    public void forwardForChecks(int countCheck) {
    	double _allLenght = CHECK_LENGHT * (double)countCheck;
    	double _allDegrees = _allLenght / WHEEL_CIRCUM * 360;
    	int _firstTachoCount_r = motor_r.getTachoCount();
    	int _firstTachoCount_l = motor_l.getTachoCount();
    	motor_r.startSynchronization();
    	motor_r.rotate((int)_allDegrees);
    	motor_l.rotate((int)_allDegrees);
    	
    }

    @Override
    public void backForChecks(int countCheck) {
    	
    }

    @Override
    public void turnRight() {

    }

    @Override
    public void turnLeft() {

    }

    @Override
    public void turnBack() {

    }

    @Override
    public void forwardToWall() {

    }

    @Override
    public void backToWall() {

    }

    @Override
    public boolean isWallNearLeft() {
        return false;
    }

    @Override
    public boolean isWallNearForward() {
        return false;
    }

    @Override
    public boolean isWallNearRight() {
        return false;
    }

    @Override
    public boolean isWallNearBack() {
        return false;
    }

    @Override
    public int checksCountToWallAtLeft() {
        return 0;
    }

    @Override
    public int checksCountToWallAtRight() {
        return 0;
    }

    @Override
    public int checksCountToWallAtBack() {
        return 0;
    }

    @Override
    public int checksCountToWallAtForward() {
        return 0;
    }
}
