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
	private int perfectRotationAngle = 0;
	private boolean isWallBack;
	private final Double CHECK_LENGHT = 0.3;
	private final Double WHEEL_CIRCUM = 2 * 0.027 * Math.PI;
	private final int MAX_SPEED = 720;
	private final int ACCELERATION = 360;
	private final int MAX_ROTATE_ACCELERATION = 720;
	private final int TO_RIGHT_DEGREES = 90;
	private final int TO_LEFT_DEGREES = 90;
	private final int TO_BACK_DEGREES = 180;
	private final int MAX_ROTATE_SPEED = MAX_SPEED * 3;
	private final int MIN_ROTATE_SPEED = 1;
	private final int COLIBRATE_DISTANCE_ROTATION_ANGLE = 160;
	private final int COLIBRATE_DISTANCE_ROTATION_BACK_ANGLE = -160;
	private final int DELAY_COLIBRATE_ROTATE = 1000;
	private final int COLIBRATE_ROTATE_FORWARD_DEGREES = 80;
	private final float MIN_VALUE_SIDE_COLIBRATE = 0.04f;
	private final float FORWARD_COLIBRATE_VALUE = 0.05f;
	
	
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
    	motor_r.setAcceleration(ACCELERATION);
    	motor_l.setAcceleration(ACCELERATION);
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
    	colibrateDistance();
    }
    
    @Override
    public void backForChecks(int countCheck) {
    	
    }

    @Override
    public void turnRight() {
    	isWallBack = isWallNearLeft();
    	float[] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegrees = (int)_nowSample[0];
    	int _startDegrees = perfectRotationAngle;
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
    		motor_r.setSpeed(_nowSpeed);
    		motor_l.setSpeed(_nowSpeed);
    		
    	}    	
    	motor_r.startSynchronization();
    	motor_r.stop(true);
    	motor_l.stop(true);
    	motor_r.endSynchronization();
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    	motor_l.setSpeed(0);
    	motor_r.setSpeed(0);
    	motor_l.setAcceleration(ACCELERATION);
    	motor_r.setAcceleration(ACCELERATION);
    	perfectRotationAngle -= TO_RIGHT_DEGREES;
    	_nowSample = null;
    	colibrateRotate();
    }

    @Override
    public void turnLeft() {
    	isWallBack = isWallNearRight();
    	float[] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegrees = (int)_nowSample[0];
    	int _startDegrees = perfectRotationAngle;
    	motor_r.setSpeed(MIN_ROTATE_SPEED);
    	motor_l.setSpeed(MIN_ROTATE_SPEED);
    	motor_l.setAcceleration(MAX_ROTATE_ACCELERATION);
    	motor_r.setAcceleration(MAX_ROTATE_ACCELERATION);
    	motor_r.startSynchronization();
    	motor_r.forward();
    	motor_l.backward();
    	motor_r.endSynchronization();
    	while(Math.abs(_nowDegrees - _startDegrees) < TO_LEFT_DEGREES)
    	{
    		gyro.fetchSample(_nowSample, 0);
    		_nowDegrees = (int)_nowSample[0];
    		int _nowSpeed = (int)((float) MAX_ROTATE_SPEED / Math.abs(TO_LEFT_DEGREES * 0.5 - Math.abs(_nowDegrees - _startDegrees)));
    		motor_r.setSpeed(_nowSpeed);
    		motor_l.setSpeed(_nowSpeed);
    		
    	}
    	motor_r.startSynchronization();
    	motor_r.stop(true);
    	motor_l.stop(true);
    	motor_r.endSynchronization();
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    	motor_l.setSpeed(0);
    	motor_r.setSpeed(0);
    	motor_l.setAcceleration(ACCELERATION);
    	motor_r.setAcceleration(ACCELERATION);
    	perfectRotationAngle += TO_LEFT_DEGREES;
    	_nowSample = null;
    	colibrateRotate();
    }

    @Override
    public void turnBack() {
    	turnRight();
    	turnRight();
    }
    
    private void colibrateRotate()
    {
    	if(isWallBack)
    	{
    		motor_r.setSpeed(MAX_SPEED);
    		motor_l.setSpeed(MAX_SPEED);
    		motor_r.startSynchronization();
    		motor_r.backward();
    		motor_l.backward();
    		motor_r.endSynchronization();
    		Delay.msDelay(DELAY_COLIBRATE_ROTATE);
    		motor_r.startSynchronization();
    		motor_r.stop(true);
    		motor_l.stop(true);
    		motor_r.endSynchronization();
    		motor_r.waitComplete();
        	motor_l.waitComplete();
    		((EV3GyroSensor)gyroSensor).reset();
    		perfectRotationAngle = 0;
    		
    		motor_r.setSpeed(MAX_SPEED/10);
    		motor_l.setSpeed(MAX_SPEED/10);
    		motor_r.startSynchronization();
    		motor_r.rotate(COLIBRATE_ROTATE_FORWARD_DEGREES);
    		motor_l.rotate(COLIBRATE_ROTATE_FORWARD_DEGREES);
    		motor_r.endSynchronization();
    		motor_r.waitComplete();
        	motor_l.waitComplete();
    		motor_r.setSpeed(0);
    		motor_l.setSpeed(0);
    		
    	}
    }
    
    private void colibrateDistance()
    {
    	if(checkRightColibrate())
    	{
    		colibrateOverRightSide();
    	}
    	if(checkLeftColibrate())
    	{
    		colibrateOverLeftSide();
    	}
    	colibrateForward();
    }
    
    private void colibrateForward()
    {
    	if(!isWallNearForward())
    	{
    		return;
    	}
    	float [] _nowSample = new float[sonic_f.sampleSize()];
    	sonic_f.fetchSample(_nowSample, 0);
    	if(_nowSample[0] < FORWARD_COLIBRATE_VALUE)
    	{
    		motor_r.setSpeed(MAX_SPEED);
        	motor_l.setSpeed(MAX_SPEED);
        	motor_r.startSynchronization();
        	motor_r.backward();
        	motor_l.backward();
        	motor_r.endSynchronization();
    		while(_nowSample[0] < FORWARD_COLIBRATE_VALUE)
    		{
    			sonic_f.fetchSample(_nowSample, 0);
    		}
    		motor_r.startSynchronization();
        	motor_r.stop(true);
        	motor_l.stop(true);
        	motor_r.endSynchronization();
        	motor_l.setSpeed(0);
        	motor_r.setSpeed(0);
    	}
    	else if(_nowSample[0] > FORWARD_COLIBRATE_VALUE)
    	{
    		motor_r.setSpeed(MAX_SPEED);
        	motor_l.setSpeed(MAX_SPEED);
        	motor_r.startSynchronization();
        	motor_r.forward();
        	motor_l.forward();
        	motor_r.endSynchronization();
    		while(_nowSample[0] > FORWARD_COLIBRATE_VALUE)
    		{
    			sonic_f.fetchSample(_nowSample, 0);    			
    		}
    		motor_r.startSynchronization();
        	motor_r.stop(true);
        	motor_l.stop(true);
        	motor_r.endSynchronization();
    		motor_l.setSpeed(0);
        	motor_r.setSpeed(0);        	
    	}
    	_nowSample = null;
    }
    
    private boolean checkRightColibrate()
    {
    	if(!isWallNearRight() || isWallNearForward())
    	{
    		return false;
    	}
    	float [] _nowSample_r = new float[sonic_r.sampleSize()];
    	float [] _nowSample_l = new float[sonic_l.sampleSize()];
    	sonic_r.fetchSample(_nowSample_r, 0);
    	sonic_l.fetchSample(_nowSample_l, 0);
    	System.out.println(_nowSample_r[0] + " r");
    	if(_nowSample_r[0] < MIN_VALUE_SIDE_COLIBRATE || (isWallNearLeft() && _nowSample_l[0] > MIN_VALUE_SIDE_COLIBRATE + 00.5))
    	{
    		_nowSample_r = null;
    		return true;
    	}
    	else {
    		_nowSample_r = null;
    		return false;
    	}
    }
    
    private boolean checkLeftColibrate()
    {
    	if(!isWallNearLeft() || isWallNearForward())
    	{
    		return false;
    	}
    	float [] _nowSample_r = new float[sonic_r.sampleSize()];
    	float [] _nowSample_l = new float[sonic_l.sampleSize()];
    	sonic_r.fetchSample(_nowSample_r, 0);
    	sonic_l.fetchSample(_nowSample_l, 0);
    	System.out.println(_nowSample_l[0] + " l");
    	if(_nowSample_l[0] < MIN_VALUE_SIDE_COLIBRATE || (isWallNearLeft() && _nowSample_r[0] > MIN_VALUE_SIDE_COLIBRATE + 00.5))
    	{
    		_nowSample_l = null;
    		return true;
    	}
    	else {
    		_nowSample_l = null;
    		return false;
    	}
    }
    
    private void colibrateOverRightSide()
    {
    	motor_r.setSpeed(MAX_SPEED);
    	motor_r.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE);
    	motor_r.setSpeed(0);
    	motor_l.setSpeed(MAX_SPEED);
    	motor_l.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE);
    	motor_l.setSpeed(0);
    	motor_r.setSpeed(MAX_SPEED);
    	motor_l.setSpeed(MAX_SPEED); 
    	motor_r.startSynchronization();
    	int _backToStart = COLIBRATE_DISTANCE_ROTATION_BACK_ANGLE;
    	System.out.println(_backToStart);
    	motor_r.rotate(_backToStart);
    	motor_l.rotate(_backToStart);
    	motor_r.endSynchronization();
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    	motor_r.setSpeed(0);
    	motor_l.setSpeed(0);
    	
    }
    
    private void colibrateOverLeftSide()
    {
    	motor_l.setSpeed(MAX_SPEED);
    	motor_l.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE);
    	motor_l.setSpeed(0);
    	motor_r.setSpeed(MAX_SPEED);
    	motor_r.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE);
    	motor_r.setSpeed(0);
    	motor_l.setSpeed(MAX_SPEED);
    	motor_r.setSpeed(MAX_SPEED); 
    	motor_r.startSynchronization();
    	int _backToStart = COLIBRATE_DISTANCE_ROTATION_BACK_ANGLE;
    	System.out.println(_backToStart);
    	motor_r.rotate(_backToStart);
    	motor_l.rotate(_backToStart);
    	motor_r.endSynchronization();
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    	motor_r.setSpeed(0);
    	motor_l.setSpeed(0);
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
    	return isWallBack;
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
