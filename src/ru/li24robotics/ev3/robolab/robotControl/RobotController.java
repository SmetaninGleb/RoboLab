package ru.li24robotics.ev3.robolab.robotControl;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
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
	private SensorModes gyroSensor;
	private SampleProvider gyro;
	private SampleProvider sonic_r;
	private SampleProvider sonic_f;
	private SampleProvider sonic_l;
	private final Double CHECK_LENGHT = 0.3;
	private final Double WHEEL_CIRCUM = 2 * 0.028 * Math.PI;
	private final int MAX_SPEED = 720;
	private final int MAX_ACCELERATION = 360;
	private final int MAX_ROTATE_ACCELERATION = 720;
	private final int TO_RIGHT_DEGREES = 90;
	private final int TO_LEFT_DEGREES = 90;
	private final int TO_BACK_DEGREES = 180;
	private final int MAX_ROTATE_SPEED = MAX_SPEED * 4;
	private final int MIN_ROTATE_SPEED = 1;
	
	
	public RobotController(Port motorPort_r, Port motorPort_l, Port sonicPort_r, Port sonicPort_f,
			Port sonicPort_l, Port gyroPort)
	{
		motor_r = new EV3LargeRegulatedMotor(motorPort_r);
		motor_l = new EV3LargeRegulatedMotor(motorPort_l);
		sonicSensor_r = new EV3UltrasonicSensor(sonicPort_r);
		sonicSensor_f = new EV3UltrasonicSensor(sonicPort_f);
		sonicSensor_l = new EV3UltrasonicSensor(sonicPort_l);
		gyroSensor = new EV3GyroSensor(gyroPort);
		sonic_r = sonicSensor_r.getMode("Distance");
		sonic_f = sonicSensor_f.getMode("Distance");
		sonic_l = sonicSensor_l.getMode("Distance");
		gyro = gyroSensor.getMode(2);
		
		motor_r.synchronizeWith(new RegulatedMotor[] {motor_l});
		motor_r.setSpeed(0);
		motor_l.setSpeed(0);
    	motor_r.setAcceleration(MAX_ACCELERATION);
    	motor_l.setAcceleration(MAX_ACCELERATION);
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
    	float[] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegrees = (int)_nowSample[0];
    	int _startDegrees = _nowDegrees;
    	motor_r.setSpeed(MIN_ROTATE_SPEED);
    	motor_l.setSpeed(MIN_ROTATE_SPEED);
    	motor_l.setAcceleration(MAX_ROTATE_ACCELERATION);
    	motor_r.setAcceleration(MAX_ROTATE_ACCELERATION);
    	motor_r.startSynchronization();
    	motor_r.backward();
    	motor_l.forward();
    	motor_r.endSynchronization();
    	while(Math.abs(_nowDegrees - _startDegrees) < TO_RIGHT_DEGREES)
    	{
    		gyro.fetchSample(_nowSample, 0);
    		_nowDegrees = (int)_nowSample[0];
    		int _nowSpeed = (int)((float) MAX_ROTATE_SPEED / Math.abs(TO_RIGHT_DEGREES * 0.5 - Math.abs(_nowDegrees - _startDegrees)));
    		System.out.println(_nowDegrees);
    		motor_r.setSpeed(_nowSpeed);
    		motor_l.setSpeed(_nowSpeed);
    		
    	}
    	motor_r.startSynchronization();
    	motor_r.stop();
    	motor_l.stop();
    	motor_r.endSynchronization();
    	motor_l.setSpeed(0);
    	motor_r.setSpeed(0);
    	motor_l.setAcceleration(MAX_ACCELERATION);
    	motor_r.setAcceleration(MAX_ACCELERATION);
    }

    @Override
    public void turnLeft() {
    	float [] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegress = (int)_nowSample[0];
    	int _startDegrees = _nowDegress;
    	motor_r.setSpeed(MAX_SPEED);
    	motor_l.setSpeed(MAX_SPEED);
    	motor_r.startSynchronization();
    	motor_r.forward();
    	motor_l.backward();
    	while(Math.abs(_nowDegress - _startDegrees) < TO_LEFT_DEGREES)
    	{
    		gyro.fetchSample(_nowSample, 0);
    		_nowDegress = (int)_nowSample[0];
    		
    	}
    	motor_r.stop();
    	motor_l.stop();
    	motor_r.endSynchronization();
    	motor_l.setSpeed(0);
    	motor_r.setSpeed(0);
    	
    }

    @Override
    public void turnBack() {
    	float [] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegress = (int)_nowSample[0];
    	int _startDegrees = _nowDegress;
    	motor_r.setSpeed(MAX_SPEED);
    	motor_l.setSpeed(MAX_SPEED);
    	motor_r.startSynchronization();
    	motor_r.forward();
    	motor_l.backward();
    	while(Math.abs(_nowDegress - _startDegrees) < TO_BACK_DEGREES)
    	{
    		gyro.fetchSample(_nowSample, 0);
    		_nowDegress = (int)_nowSample[0];
    	}
    	motor_r.stop();
    	motor_l.stop();
    	motor_r.endSynchronization();
    	motor_l.setSpeed(0);
    	motor_r.setSpeed(0);
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
        _count = (int)(_distance / CHECK_LENGHT);
    	return _count;
    }

    @Override
    public int checksCountToWallAtRight() {
    	int _count;
        double _distance;
        float[] _sample = new float[sonic_r.sampleSize()];
        sonic_r.fetchSample(_sample, 0);
        _distance = (double)_sample[0];
        _count = (int)(_distance / CHECK_LENGHT);
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
        _count = (int)(_distance / CHECK_LENGHT);
    	return _count;
    }
}
