package ru.li24robotics.ev3.robolab.robotControl;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class RobotController implements IRobotController{
	private RegulatedMotor motor_r;
	private RegulatedMotor motor_l;
	private SensorModes sonicSensor_r;
	private SensorModes sonicSensor_f;
	private SensorModes sonicSensor_l;
	private SampleProvider sonic_r;
	private SampleProvider sonic_f;
	private SampleProvider sonic_l;
	private final Double CHECK_LENGHT = 0.3;
	private final Double WHEEL_CIRCUM = 2 * 0.028 * Math.PI;
	private final int MAX_SPEED = 720;
	private final int ACCELERAION = 360;
	
	
	public RobotController(Port motorPort_r, Port motorPort_l, Port sonicPort_r, Port sonicPort_f, Port sonicPort_l)
	{
		motor_r = new EV3LargeRegulatedMotor(motorPort_r);
		motor_l = new EV3LargeRegulatedMotor(motorPort_l);
		sonicSensor_r = new EV3UltrasonicSensor(sonicPort_r);
		sonicSensor_f = new EV3UltrasonicSensor(sonicPort_f);
		sonicSensor_l = new EV3UltrasonicSensor(sonicPort_l);
		sonic_r = sonicSensor_r.getMode("Distance");
		sonic_f = sonicSensor_f.getMode("Distance");
		sonic_l = sonicSensor_l.getMode("Distance");
		
		motor_r.synchronizeWith(new RegulatedMotor[] {motor_l});
		motor_r.setSpeed(0);
		motor_l.setSpeed(0);
    	motor_r.setAcceleration(ACCELERAION);
    	motor_l.setAcceleration(ACCELERAION);
	}
	
    @Override
    public void forwardForChecks(int countCheck) 
    {
    	double _allLenght = CHECK_LENGHT * (double)countCheck;
    	double _allDegrees = _allLenght / WHEEL_CIRCUM * 360;
    	motor_r.setSpeed(MAX_SPEED);
    	motor_l.setSpeed(MAX_SPEED); 
    	motor_r.startSynchronization();
    	motor_r.rotate((int)_allDegrees);
    	motor_l.rotate((int)_allDegrees);
    	motor_r.endSynchronization();
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    	motor_r.setSpeed(0);
    	motor_l.setSpeed(0);
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
        return checksCountToWallAtLeft() == 0;
    }

    @Override
    public boolean isWallNearForward() {
        return checksCountToWallAtForward() == 0;
    }

    @Override
    public boolean isWallNearRight() {
        return checksCountToWallAtRight() == 0;
    }

    @Override
    public boolean isWallNearBack() {
        return checksCountToWallAtBack() == 0;
    }

    @Override
    public int checksCountToWallAtLeft() {
        int _count;
        double _distance;
        float[] _sample = new float[sonic_l.sampleSize()];
        sonic_l.fetchSample(_sample, 0);
        _distance = (double)_sample[0];
        //Переводим дистанцию в метры
        _count = (int)(_distance * 100 / CHECK_LENGHT);
    	return _count;
    }

    @Override
    public int checksCountToWallAtRight() {
    	int _count;
        double _distance;
        float[] _sample = new float[sonic_r.sampleSize()];
        sonic_r.fetchSample(_sample, 0);
        _distance = (double)_sample[0];
        //Переводим дистанцию в метры
        _count = (int)(_distance * 100 / CHECK_LENGHT);
    	return _count;
    }

    @Override
    public int checksCountToWallAtBack() {
        int _count;
        turnRight();
        _count = checksCountToWallAtRight();
        turnLeft();
    	return _count;
    }

    @Override
    public int checksCountToWallAtForward() {
    	int _count;
        double _distance;
        float[] _sample = new float[sonic_f.sampleSize()];
        sonic_f.fetchSample(_sample, 0);
        _distance = (double)_sample[0];
        //Переводим дистанцию в метры
        _count = (int)(_distance * 100 / CHECK_LENGHT);
    	return _count;
    }
}
