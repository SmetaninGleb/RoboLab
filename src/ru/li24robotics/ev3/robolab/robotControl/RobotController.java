package ru.li24robotics.ev3.robolab.robotControl;

import java.lang.management.MonitorInfo;
import java.util.Date;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.utility.Timer;


public class RobotController implements IRobotController {
	private RegulatedMotor motorReg_r;
	private RegulatedMotor motorReg_l;
	private UnregulatedMotor motorUnreg_r;
	private UnregulatedMotor motorUnreg_l;
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
	private final Double CHECK_LENGTH = 0.3;
	private final Double WHEEL_CIRCUM = 2 * 0.027 * Math.PI;
	private final int MAX_SPEED = 1440;
	private final int ACCELERATION = 1080;
	private final int MAX_ROTATE_ACCELERATION = 720 * 3;
	private final int TO_LEFT_DEGREES = 90;
	private final int TO_RIGHT_DEGREES = 90;
	private final int TO_BACK_DEGREES = 180;
	private final int MAX_ROTATE_SPEED_BACK = 450;
	private final int MAX_ROTATE_SPEED_RIGHT = 350;
	private final int MAX_ROTATE_SPEED_LEFT = 350;
	private final int MIN_ROTATE_SPEED = 1;
	private final int COLIBRATE_DISTANCE_ROTATION_ANGLE = 160;
	private final int COLIBRATE_DISTANCE_ROTATION_BACK_ANGLE = -160;
	private final int START_ROTATE_SPEED = 20;
	private final int DELAY_COLIBRATE_ROTATE = 500;
	private final int COLIBRATE_ROTATE_FORWARD_DEGREES = 80;
	private final int ERROR_DELAY = 0;
	private final float MIN_VALUE_SIDE_COLIBRATE = 0.04f;
	private final float FORWARD_COLIBRATE_VALUE = 0.05f;
	private final float PERFECT_SIDE_COLIBRATION_DISTACE = 0.06f;
	private final float CHECK_NEAR_WALL_DISTANCE = 0.13f;
	private final float P_REGULATOR_ROTATION_90_COEF = 12f;
	private final float D_REGULATOR_ROTATION_90_COEF = 35f;
	private final float P_REGULATOR_ROTATION_180_COEF = 9f;
	private final float D_REGULATOR_ROTATION_180_COEF = 16f;
	private final float P_REGULATOR_MOVE_COEF = 0f; // TODO настроить!!!
	private final float D_REGULATOR_MOVE_COEF = 0f; // TODO настроить!!!
	private final long ROTATION_LEFT_RIGHT_TIME_MILLIS = 1200; // впритык - секунда(1000)
	private final long ROTATION_BACK_TIME_MILLIS = 2400; // еще не проверялось
	
	public RobotController(Port motorPort_r, Port motorPort_l, Port sonicPort_r, Port sonicPort_f,
			Port sonicPort_l, Port gyroPort)
	{
		motorReg_r = new EV3LargeRegulatedMotor(motorPort_r);
		motorReg_l = new EV3LargeRegulatedMotor(motorPort_l);
		motorUnreg_r = new UnregulatedMotor(motorPort_r);
		motorUnreg_l = new UnregulatedMotor(motorPort_l);
		sonicSensor_r = new EV3UltrasonicSensor(sonicPort_r);
		sonicSensor_f = new EV3UltrasonicSensor(sonicPort_f);
		sonicSensor_l = new EV3UltrasonicSensor(sonicPort_l);
		gyroSensor = new EV3GyroSensor(gyroPort);
		sonic_r = sonicSensor_r.getMode("Distance");
		sonic_f = sonicSensor_f.getMode("Distance");
		sonic_l = sonicSensor_l.getMode("Distance");
		gyro = gyroSensor.getMode(2);
		
		motorReg_r.synchronizeWith(new RegulatedMotor[] {motorReg_l});
		motorReg_r.setSpeed(0);
		motorReg_l.setSpeed(0);
    	motorReg_r.setAcceleration(ACCELERATION);
    	motorReg_l.setAcceleration(ACCELERATION);
    	
    	motorUnreg_r.setPower(0);
    	motorUnreg_l.setPower(0);
    	
    	isWallBack = false;
	}

    @Override
    public void forwardForChecks(int countCheck) 
    {
		Delay.msDelay(ERROR_DELAY);
    	double _allLength = CHECK_LENGTH * (double)countCheck;
    	double _allDegrees = _allLength / WHEEL_CIRCUM * 360;
    	motorReg_r.setSpeed(MAX_SPEED);
    	motorReg_l.setSpeed(MAX_SPEED);
    	motorReg_r.setAcceleration(ACCELERATION);
    	motorReg_l.setAcceleration(ACCELERATION);
		motorReg_r.resetTachoCount();
		motorReg_l.resetTachoCount();
    	motorReg_r.waitComplete();
    	motorReg_l.waitComplete();
//    	motor_r.rotate((int)_allDegrees, true);
//    	motor_l.rotate((int)_allDegrees, true);
		float [] _nowGyroSamples = new float[gyro.sampleSize()];
		float _nowDegrees = _nowGyroSamples[0];
		int _errorPRegulatorMove_r;
		int _controlPRegulatorMove_r;
		int _controlDRegulatorMove_r;
		int _controlRegulatorMove_r;
		int _oldDRegulatorMove_r = motorReg_r.getTachoCount();
		int _errorPRegulatorMove_l;
		int _controlPRegulatorMove_l;
		int _controlDRegulatorMove_l;
		int _controlRegulatorMove_l;
		int _oldDRegulatorMove_l = motorReg_l.getTachoCount();
		while (motorReg_l.getTachoCount() != _allDegrees || motorReg_r.getTachoCount() != _allDegrees || _nowDegrees != perfectRotationAngle)
		{
			_errorPRegulatorMove_r = (int)_allDegrees - motorReg_r.getTachoCount();
			_controlPRegulatorMove_r = (int)(_errorPRegulatorMove_r * P_REGULATOR_MOVE_COEF);
			_controlDRegulatorMove_r = (int)((motorReg_r.getTachoCount() - _oldDRegulatorMove_r) * D_REGULATOR_MOVE_COEF);
			_controlRegulatorMove_r = _controlDRegulatorMove_r + _controlPRegulatorMove_r;
			motorReg_r.setSpeed(MAX_SPEED + _controlRegulatorMove_r);

			_errorPRegulatorMove_l = (int)_allDegrees - motorReg_l.getTachoCount();
			_controlPRegulatorMove_l = (int)(_errorPRegulatorMove_l * P_REGULATOR_MOVE_COEF);
			_controlDRegulatorMove_l = (int)((motorReg_l.getTachoCount() - _oldDRegulatorMove_l) * D_REGULATOR_MOVE_COEF);
			_controlRegulatorMove_l = _controlDRegulatorMove_l + _controlPRegulatorMove_l;
			motorReg_l.setSpeed(MAX_SPEED + _controlRegulatorMove_l);
		}
    	motorReg_r.waitComplete();
    	motorReg_l.waitComplete();
    	isWallBack = false;
//    	colibrateDistance();
    }
    
    @Override
    public void backForChecks(int countCheck) {
    	
    }

    @Override
    public void turnRight() {
    	Delay.msDelay(ERROR_DELAY);
    	isWallBack = isWallNearLeft();
		perfectRotationAngle -= TO_RIGHT_DEGREES;
    	float[] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegrees = (int)_nowSample[0];
    	motorReg_l.setAcceleration(MAX_ROTATE_ACCELERATION);
    	motorReg_r.setAcceleration(MAX_ROTATE_ACCELERATION);
    	int _oldDRegulator = _nowDegrees;
    	long _startRotateTime_millis = System.currentTimeMillis();
    	long _nowRotateTime_millis = _startRotateTime_millis;
		int _errorPRegulator;
		int _controlPRegulator;
		int _controlDRegulator;
    	while(_nowRotateTime_millis - _startRotateTime_millis < ROTATION_LEFT_RIGHT_TIME_MILLIS)
    	{
    		gyro.fetchSample(_nowSample, 0);
    		_nowDegrees = (int)_nowSample[0];
    		_errorPRegulator = perfectRotationAngle - _nowDegrees;
    		_controlPRegulator = (int)(_errorPRegulator * P_REGULATOR_ROTATION_90_COEF);
    		_controlDRegulator = (int)((_nowDegrees - _oldDRegulator) * (D_REGULATOR_ROTATION_90_COEF));
    		motorReg_l.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
    		motorReg_r.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
    		if(_controlPRegulator + _controlDRegulator < 0)
			{
				motorReg_r.backward();
				motorReg_l.forward();
			}
			else {
    			motorReg_r.forward();
    			motorReg_l.backward();
			}
    		_oldDRegulator = _nowDegrees;
    		_nowRotateTime_millis = System.currentTimeMillis();
    	}
    	motorReg_r.stop(true);
    	motorReg_l.stop(true);
    	motorReg_r.waitComplete();
    	motorReg_l.waitComplete();
    	motorReg_r.setAcceleration(ACCELERATION);
    	motorReg_l.setAcceleration(ACCELERATION);
    	colibrateRotate();
    }

    @Override
    public void turnLeft() {
    	Delay.msDelay(ERROR_DELAY);
    	isWallBack = isWallNearRight();
		perfectRotationAngle -= TO_LEFT_DEGREES;
    	float[] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegrees = (int)_nowSample[0];
    	motorReg_l.setAcceleration(MAX_ROTATE_ACCELERATION);
    	motorReg_r.setAcceleration(MAX_ROTATE_ACCELERATION);
    	int _oldDRegulator = _nowDegrees;
    	long _startRotateTime_millis = System.currentTimeMillis();
    	long _nowRotateTime_millis = _startRotateTime_millis;
		int _errorPRegulator;
		int _controlPRegulator;
		int _controlDRegulator;
		while(_nowRotateTime_millis - _startRotateTime_millis < ROTATION_LEFT_RIGHT_TIME_MILLIS)
    	{
    		gyro.fetchSample(_nowSample, 0);
    		_nowDegrees = (int)_nowSample[0];
    		_errorPRegulator = perfectRotationAngle - _nowDegrees;
    		_controlPRegulator = (int)(_errorPRegulator * P_REGULATOR_ROTATION_90_COEF);
    		_controlDRegulator = (int)((_nowDegrees - _oldDRegulator) * (D_REGULATOR_ROTATION_90_COEF));
    		motorReg_l.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
    		motorReg_r.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
    		if(_controlPRegulator + _controlDRegulator < 0)
			{
				motorReg_r.forward();
				motorReg_l.backward();
			}
			else {
    			motorReg_r.backward();
    			motorReg_l.forward();
			}
    		_oldDRegulator = _nowDegrees;
    		_nowRotateTime_millis = System.currentTimeMillis();
    	}
    	motorReg_r.stop(true);
    	motorReg_l.stop(true);
    	motorReg_r.waitComplete();
    	motorReg_l.waitComplete();
    	motorReg_r.setAcceleration(ACCELERATION);
    	motorReg_l.setAcceleration(ACCELERATION);
    	colibrateRotate();
    }

    /* (non-Javadoc)
     * @see ru.li24robotics.ev3.robolab.robotControl.IMoveRobot#turnBack()
     */
    @Override
    public void turnBack() {
    	Delay.msDelay(ERROR_DELAY);
    	isWallBack = isWallNearForward();
		perfectRotationAngle -= TO_BACK_DEGREES;
    	float[] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegrees = (int)_nowSample[0];
    	motorReg_l.setAcceleration(MAX_ROTATE_ACCELERATION);
    	motorReg_r.setAcceleration(MAX_ROTATE_ACCELERATION);
    	int _oldDRegulator = _nowDegrees;
    	long _startRotateTime_millis = System.currentTimeMillis();
    	long _nowRotateTime_millis = _startRotateTime_millis;
		int _errorPRegulator;
		int _controlPRegulator;
		int _controlDRegulator;
    	while(_nowRotateTime_millis - _startRotateTime_millis < ROTATION_BACK_TIME_MILLIS)
    	{
    		gyro.fetchSample(_nowSample, 0);
    		_nowDegrees = (int)_nowSample[0];
    		_errorPRegulator = perfectRotationAngle - _nowDegrees;
    		_controlPRegulator = (int)(_errorPRegulator * P_REGULATOR_ROTATION_180_COEF);
    		_controlDRegulator = (int)((_nowDegrees - _oldDRegulator) * (D_REGULATOR_ROTATION_180_COEF));
//			motorReg_r.startSynchronization();
//    		motorReg_l.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
//    		motorReg_r.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
//			motorReg_r.endSynchronization();
    		motorUnreg_r.setPower(Math.abs(_controlPRegulator + _controlDRegulator));
    		motorUnreg_l.setPower(Math.abs(_controlPRegulator + _controlDRegulator));
    		if(_controlPRegulator + _controlDRegulator < 0)
			{
//    			motorReg_r.startSynchronization();
//				motorReg_r.backward();
//				motorReg_l.forward();
//    			motorReg_r.endSynchronization();
    			motorUnreg_r.backward();
    			motorUnreg_l.forward();
			}
			else {
//    			motorReg_r.startSynchronization();
//    			motorReg_r.forward();
//    			motorReg_l.backward();
//    			motorReg_r.endSynchronization();
    			motorUnreg_r.forward();
    			motorUnreg_l.backward();
			}
    		_oldDRegulator = _nowDegrees;
    		_nowRotateTime_millis = System.currentTimeMillis();
    		Delay.msDelay(10);
    	}
    	motorReg_r.stop(true);
    	motorReg_l.stop(true);
    	motorReg_r.waitComplete();
    	motorReg_l.waitComplete();
    	motorReg_r.setAcceleration(ACCELERATION);
    	motorReg_l.setAcceleration(ACCELERATION);
    	//colibrateRotate();
    }
    
    private void colibrateRotate()
    {
    	Delay.msDelay(ERROR_DELAY);
    	if(isWallBack)
    	{
    		motorReg_r.setSpeed(MAX_SPEED);
    		motorReg_l.setSpeed(MAX_SPEED);
    		motorReg_l.setAcceleration(ACCELERATION);
    		motorReg_r.setAcceleration(ACCELERATION);
        	Delay.msDelay(ERROR_DELAY);
        	motorReg_r.waitComplete();
        	motorReg_l.waitComplete();
//    		motor_r.startSynchronization();
    		motorReg_r.backward();
    		motorReg_l.backward();
//    		motor_r.endSynchronization();
    		Delay.msDelay(DELAY_COLIBRATE_ROTATE);
//    		motor_r.startSynchronization();
    		motorReg_r.stop(true);
    		motorReg_l.stop(true);
//    		motor_r.endSynchronization();
    		motorReg_r.waitComplete();
        	motorReg_l.waitComplete();
    		((EV3GyroSensor)gyroSensor).reset();
    		perfectRotationAngle = 0;
//    		
//    		motor_r.setSpeed(MAX_SPEED);
//    		motor_l.setSpeed(MAX_SPEED);
//    		motor_r.startSynchronization();
    		motorReg_r.rotate(COLIBRATE_ROTATE_FORWARD_DEGREES, true);
    		motorReg_l.rotate(COLIBRATE_ROTATE_FORWARD_DEGREES, true);
//    		motor_r.endSynchronization();
    		motorReg_r.waitComplete();
        	motorReg_l.waitComplete();
       
//    		motor_r.setSpeed(0);
//    		motor_l.setSpeed(0);
    		
    	}
    	colibrateDistance();
    }
    
    private void colibrateDistance()
    {
    	Delay.msDelay(ERROR_DELAY);
    	if(checkRightColibrate())
    	{
    		Sound.beep();
    		colibrateOverRightSide();
    	}
    	else if(checkLeftColibrate())
    	{
    		Sound.beep();
    		colibrateOverLeftSide();
    	}
    	//colibrateForward();
    }
    
    private void colibrateForward()
    {
    	if(checksCountToWallAtForward() > 0)
    	{
    		return;
    	}
    	
    	motorReg_l.forward();
    	motorReg_r.forward();
    	
    	Delay.msDelay(1000);
    	
    	motorReg_r.stop(true);
    	motorReg_l.stop(true);
//    	Delay.msDelay(ERROR_DELAY);
//    	if(!isWallNearForward())
//    	{
//    		return;
//    	}
//    	float [] _nowSample = new float[sonic_f.sampleSize()];
//    	sonic_f.fetchSample(_nowSample, 0);
//    	if(_nowSample[0] < FORWARD_COLIBRATE_VALUE)
//    	{
//    		motor_r.setSpeed(MAX_SPEED);
//        	motor_l.setSpeed(MAX_SPEED);
////        	motor_r.startSynchronization();
//        	motor_r.backward();
//        	motor_l.backward();
////        	motor_r.endSynchronization();
//    		while(_nowSample[0] < FORWARD_COLIBRATE_VALUE)
//    		{
//    			sonic_f.fetchSample(_nowSample, 0);
//    		}
////    		motor_r.startSynchronization();
//        	motor_r.stop(true);
//        	motor_l.stop(true);
////        	motor_r.endSynchronization();
//        	motor_r.waitComplete();
//        	motor_l.waitComplete();
////        	motor_l.setSpeed(0);
////        	motor_r.setSpeed(0);
//    	}
//    	else if(_nowSample[0] > FORWARD_COLIBRATE_VALUE)
//    	{
//    		motor_r.setSpeed(MAX_SPEED);
//        	motor_l.setSpeed(MAX_SPEED);
////        	motor_r.startSynchronization();
//        	motor_r.forward();
//        	motor_l.forward();
////        	motor_r.endSynchronization();
//    		while(_nowSample[0] > FORWARD_COLIBRATE_VALUE)
//    		{
//    			sonic_f.fetchSample(_nowSample, 0);    			
//    		}
////    		motor_r.startSynchronization();
//        	motor_r.stop(true);
//        	motor_l.stop(true);
////        	motor_r.endSynchronization();
//        	motor_r.waitComplete();
//        	motor_l.waitComplete();
////    		motor_l.setSpeed(0);
////        	motor_r.setSpeed(0);        	
//    	}
//    	_nowSample = null;
    }
    
    private boolean checkRightColibrate()
    {    	
    	if(isWallNearForward())
    	{
    		return false;
    	}
    	float [] _nowSample_r = new float[sonic_r.sampleSize()];
    	float [] _nowSample_l = new float[sonic_l.sampleSize()];
    	sonic_r.fetchSample(_nowSample_r, 0);
    	sonic_l.fetchSample(_nowSample_l, 0);
    	if((_nowSample_r[0] < MIN_VALUE_SIDE_COLIBRATE && isWallNearRight()) || 
    			(isWallNearLeft() && _nowSample_l[0] > 2 * PERFECT_SIDE_COLIBRATION_DISTACE -  MIN_VALUE_SIDE_COLIBRATE))
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
    	if(isWallNearForward())
    	{
    		return false;
    	}
    	float [] _nowSample_r = new float[sonic_r.sampleSize()];
    	float [] _nowSample_l = new float[sonic_l.sampleSize()];
    	sonic_r.fetchSample(_nowSample_r, 0);
    	sonic_l.fetchSample(_nowSample_l, 0);
//    	System.out.println(_nowSample_l[0] + "l");
//    	System.out.println(_nowSample_r[0] + "r");

    	if((_nowSample_l[0] < MIN_VALUE_SIDE_COLIBRATE && isWallNearLeft()) || 
    			(isWallNearRight() && _nowSample_r[0] > 2 * PERFECT_SIDE_COLIBRATION_DISTACE -  MIN_VALUE_SIDE_COLIBRATE))
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
    	motorReg_r.setSpeed(MAX_SPEED);
    	motorReg_r.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE, true);
    	motorReg_r.waitComplete();
    	motorReg_l.setSpeed(MAX_SPEED);
    	motorReg_l.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE, true);
    	motorReg_l.waitComplete();
    	int _backToStart = COLIBRATE_DISTANCE_ROTATION_BACK_ANGLE;
    	Delay.msDelay(ERROR_DELAY);
//    	motor_r.startSynchronization();
    	motorReg_r.rotate(_backToStart, true);
    	motorReg_l.rotate(_backToStart, true);
//    	motor_r.endSynchronization();
    	motorReg_r.waitComplete();
    	motorReg_l.waitComplete();
//    	motor_r.setSpeed(0);
//    	motor_l.setSpeed(0);
    	
    }
    
    private void colibrateOverLeftSide()
    {
    	motorReg_l.setSpeed(MAX_SPEED);
    	motorReg_l.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE, true);
    	motorReg_l.waitComplete();
    	motorReg_r.setSpeed(MAX_SPEED);
    	motorReg_r.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE, true);
    	motorReg_r.waitComplete();
    	int _backToStart = COLIBRATE_DISTANCE_ROTATION_BACK_ANGLE;
//    	motor_r.startSynchronization();
    	motorReg_r.rotate(_backToStart, true);
    	motorReg_l.rotate(_backToStart, true);
//    	motor_r.endSynchronization();
    	motorReg_r.waitComplete();
    	motorReg_l.waitComplete();
//    	motor_r.setSpeed(0);
//    	motor_l.setSpeed(0);
    }
    
    @Override
    public void forwardToWall() {

    }

    @Override
    public void backToWall() {

    }

    @Override
    public boolean isWallNearLeft() {
    	float _distance;
    	float[] _nowSample = new float[sonic_l.sampleSize()];
    	sonic_l.fetchSample(_nowSample, 0);
    	_distance = _nowSample[0];
        return _distance < CHECK_NEAR_WALL_DISTANCE;
    }

    @Override
    public boolean isWallNearForward() {
    	float _distance;
    	float[] _nowSample = new float[sonic_f.sampleSize()];
    	sonic_f.fetchSample(_nowSample, 0);
    	_distance = _nowSample[0];
        return _distance < CHECK_NEAR_WALL_DISTANCE;
    }

    @Override
    public boolean isWallNearRight() {
    	float _distance;
    	float[] _nowSample = new float[sonic_r.sampleSize()];
    	sonic_r.fetchSample(_nowSample, 0);
    	_distance = _nowSample[0];
        return _distance < CHECK_NEAR_WALL_DISTANCE;
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
        _count = (int)(_distance / CHECK_LENGTH);
    	return _count;
    }

    @Override
    public int checksCountToWallAtRight() {
    	int _count;
        double _distance;
        float[] _sample = new float[sonic_r.sampleSize()];
        sonic_r.fetchSample(_sample, 0);
        _distance = (double)_sample[0];
        _count = (int)(_distance / CHECK_LENGTH);
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
        _count = (int)(_distance / CHECK_LENGTH);
    	return _count;
    }

    public void showStatus()
	{
		float[] _nowSampleGyro = new float[gyro.sampleSize()];
		float[] _nowSampleSonic_r = new float[sonic_r.sampleSize()];
		float[] _nowSampleSonic_f = new float[sonic_f.sampleSize()];
		float[] _nowSampleSonic_l = new float[sonic_l.sampleSize()];
		gyro.fetchSample(_nowSampleGyro, 0);
		sonic_l.fetchSample(_nowSampleSonic_l, 0);
		sonic_f.fetchSample(_nowSampleSonic_f, 0);
		sonic_r.fetchSample(_nowSampleSonic_r, 0);
		LCD.drawString("GYRO: " + _nowSampleGyro[0], 0, 0);
		LCD.drawString("SONIC_R: " + _nowSampleSonic_r[0], 0, 1);
		LCD.drawString("SONIC_F: " + _nowSampleSonic_f[0], 0, 2);
		LCD.drawString("SONIC_L: " + _nowSampleSonic_l[0], 0, 3);
	}
}
