package ru.li24robotics.ev3.robolab.robotControl;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import javax.print.attribute.standard.PDLOverrideSupported;


public class RobotController implements IRobotController {
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
	private final float P_REGULATOR_ROTATION_90DEG_COEF = 12f;
	private final float D_REGULATOR_ROTATION_90DEG_COEF = 35f;
    private final float P_REGULATOR_ROTATION_180DEG_COEF = 0f; //TODO настроить!!!
    private final float D_REGULATOR_ROTATION_180DEG_COEF = 0f; //TODO настроить!!!
	private final float P_REGULATOR_MOVE_COEF = 0f; //TODO настроить!!!
	private final float D_REGULATOR_MOVE_COEF = 0f; //TODO настроить!!!
	private final long ROTATION_LEFT_RIGHT_TIME_MILLIS = 1200; // впритык - секунда(1000)
	private final long ROTATION_BACK_TIME_MILLIS = 2400; // еще не проверялось
	
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
    	
    	isWallBack = false;
	}

    @Override
    public void forwardForChecks(int countCheck) 
    {
		// For errors with time
        Delay.msDelay(ERROR_DELAY);

        // Calculate motor's rotating
    	double _allLength = CHECK_LENGTH * (double)countCheck;
    	double _allDegrees = _allLength / WHEEL_CIRCUM * 360;

        // Make ready motors for start
        isWallBack = false;
        motor_r.setSpeed(MAX_SPEED);
    	motor_l.setSpeed(MAX_SPEED);
    	motor_r.setAcceleration(ACCELERATION);
    	motor_l.setAcceleration(ACCELERATION);
    	motor_r.waitComplete();
    	motor_l.waitComplete();

        // Init regulator's variables
        float [] _nowGyroSamples = new float[gyro.sampleSize()];
        gyro.fetchSample(_nowGyroSamples, 0);
        float _nowDegrees = _nowGyroSamples[0];
        int _errorRotation;
        int _controlRotationPRegulator;
        int _controlRotationDRegulator;
        int _controlRegulatorSum;
        int _oldRotationDRegulator = (int)_nowDegrees;

        // Start motors
    	motor_r.rotate((int)_allDegrees, true);
    	motor_l.rotate((int)_allDegrees, true);

        // Start regulating
        while(motor_r.isMoving() || motor_l.isMoving())
        {
            gyro.fetchSample(_nowGyroSamples, 0);
            _nowDegrees = _nowGyroSamples[0];
            _errorRotation = (int)(perfectRotationAngle - _nowDegrees);
            _controlRotationPRegulator = (int)(_errorRotation * P_REGULATOR_MOVE_COEF);
            _controlRotationDRegulator = (int)((_nowDegrees - _oldRotationDRegulator) * D_REGULATOR_MOVE_COEF);
            _controlRegulatorSum = _controlRotationDRegulator + _controlRotationPRegulator;
            motor_r.setSpeed(MAX_SPEED + _controlRegulatorSum);
            motor_l.setSpeed(MAX_SPEED - _controlRegulatorSum);
        }

        // Make motors standart state
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    }
    
    @Override
    public void backForChecks(int countCheck)
    {
    	
    }

    @Override
    public void turnRight()
    {
        // For errors with time
    	Delay.msDelay(ERROR_DELAY);

        // Do field's changes
    	isWallBack = isWallNearLeft();
		perfectRotationAngle -= TO_RIGHT_DEGREES;

        // Make ready motors for start
        motor_l.setAcceleration(MAX_ROTATE_ACCELERATION);
        motor_r.setAcceleration(MAX_ROTATE_ACCELERATION);

        // Init variables for rotating and regulating
    	float[] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegrees = (int)_nowSample[0];
    	int _oldDRegulator = _nowDegrees;
    	long _startRotateTime_millis = System.currentTimeMillis();
    	long _nowRotateTime_millis = _startRotateTime_millis;
		int _errorPRegulator;
		int _controlPRegulator;
		int _controlDRegulator;

        // Start rotating with regulators
    	while(_nowRotateTime_millis - _startRotateTime_millis < ROTATION_LEFT_RIGHT_TIME_MILLIS)
    	{
    		gyro.fetchSample(_nowSample, 0);
    		_nowDegrees = (int)_nowSample[0];
    		_errorPRegulator = perfectRotationAngle - _nowDegrees;
    		_controlPRegulator = (int)(_errorPRegulator * P_REGULATOR_ROTATION_90DEG_COEF);
    		_controlDRegulator = (int)((_nowDegrees - _oldDRegulator) * (D_REGULATOR_ROTATION_90DEG_COEF));
    		motor_l.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
    		motor_r.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
    		if(_controlPRegulator + _controlDRegulator < 0)
			{
				motor_r.backward();
				motor_l.forward();
			}
			else {
    			motor_r.forward();
    			motor_l.backward();
			}
    		_oldDRegulator = _nowDegrees;
    		_nowRotateTime_millis = System.currentTimeMillis();
    	}

    	// To standart states
    	motor_r.stop(true);
    	motor_l.stop(true);
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    	motor_r.setAcceleration(ACCELERATION);
    	motor_l.setAcceleration(ACCELERATION);

        // Start colibration
        colibrateRotate();
    }

    @Override
    public void turnLeft()
    {
        // For errors with time
    	Delay.msDelay(ERROR_DELAY);

        // Do field's changes
    	isWallBack = isWallNearRight();
		perfectRotationAngle -= TO_LEFT_DEGREES;

        // Make ready motors for start
        motor_l.setAcceleration(MAX_ROTATE_ACCELERATION);
        motor_r.setAcceleration(MAX_ROTATE_ACCELERATION);

        // Init variables for rotating and regulating
        float[] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegrees = (int)_nowSample[0];
    	int _oldDRegulator = _nowDegrees;
    	long _startRotateTime_millis = System.currentTimeMillis();
    	long _nowRotateTime_millis = _startRotateTime_millis;
		int _errorPRegulator;
		int _controlPRegulator;
		int _controlDRegulator;

        // Start rotating with regulators
        while(_nowRotateTime_millis - _startRotateTime_millis < ROTATION_LEFT_RIGHT_TIME_MILLIS)
    	{
    		gyro.fetchSample(_nowSample, 0);
    		_nowDegrees = (int)_nowSample[0];
    		_errorPRegulator = perfectRotationAngle - _nowDegrees;
    		_controlPRegulator = (int)(_errorPRegulator * P_REGULATOR_ROTATION_90DEG_COEF);
    		_controlDRegulator = (int)((_nowDegrees - _oldDRegulator) * (D_REGULATOR_ROTATION_90DEG_COEF));
    		motor_l.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
    		motor_r.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
    		if(_controlPRegulator + _controlDRegulator < 0)
			{
				motor_r.forward();
				motor_l.backward();
			}
			else {
    			motor_r.backward();
    			motor_l.forward();
			}
    		_oldDRegulator = _nowDegrees;
    		_nowRotateTime_millis = System.currentTimeMillis();
    	}

        // To standart states
    	motor_r.stop(true);
    	motor_l.stop(true);
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    	motor_r.setAcceleration(ACCELERATION);
    	motor_l.setAcceleration(ACCELERATION);

        // Start colibration
    	colibrateRotate();
    }

    @Override
    public void turnBack()
    {
        // For errors with time
        Delay.msDelay(ERROR_DELAY);

        // Do field's changes
        isWallBack = isWallNearForward();
		perfectRotationAngle -= TO_BACK_DEGREES;

        // Make ready motors for start
        motor_l.setAcceleration(MAX_ROTATE_ACCELERATION);
        motor_r.setAcceleration(MAX_ROTATE_ACCELERATION);

        // Init variables for rotating and regulating
    	float[] _nowSample = new float[gyro.sampleSize()];
    	gyro.fetchSample(_nowSample, 0);
    	int _nowDegrees = (int)_nowSample[0];
    	int _oldDRegulator = _nowDegrees;
    	long _startRotateTime_millis = System.currentTimeMillis();
    	long _nowRotateTime_millis = _startRotateTime_millis;
		int _errorPRegulator;
		int _controlPRegulator;
		int _controlDRegulator;

        // Start rotating with regulators
        while(_nowRotateTime_millis - _startRotateTime_millis < ROTATION_BACK_TIME_MILLIS)
    	{
    		gyro.fetchSample(_nowSample, 0);
    		_nowDegrees = (int)_nowSample[0];
    		_errorPRegulator = perfectRotationAngle - _nowDegrees;
    		_controlPRegulator = (int)(_errorPRegulator * P_REGULATOR_ROTATION_180DEG_COEF);
    		_controlDRegulator = (int)((_nowDegrees - _oldDRegulator) * (D_REGULATOR_ROTATION_180DEG_COEF));
    		motor_l.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
    		motor_r.setSpeed(Math.abs(_controlPRegulator + _controlDRegulator));
    		if(_controlPRegulator + _controlDRegulator < 0)
			{
				motor_r.backward();
				motor_l.forward();
			}
			else {
    			motor_r.forward();
    			motor_l.backward();
			}
    		_oldDRegulator = _nowDegrees;
    		_nowRotateTime_millis = System.currentTimeMillis();
    	}

        // To standart states
    	motor_r.stop(true);
    	motor_l.stop(true);
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    	motor_r.setAcceleration(ACCELERATION);
    	motor_l.setAcceleration(ACCELERATION);

        // Start colibration
    	colibrateRotate();
    }
    
    private void colibrateRotate()
    {
    	Delay.msDelay(ERROR_DELAY);
    	if(isWallBack)
    	{
    		motor_r.setSpeed(MAX_SPEED);
    		motor_l.setSpeed(MAX_SPEED);
    		motor_l.setAcceleration(ACCELERATION);
    		motor_r.setAcceleration(ACCELERATION);
        	Delay.msDelay(ERROR_DELAY);
        	motor_r.waitComplete();
        	motor_l.waitComplete();
    		motor_r.backward();
    		motor_l.backward();
    		Delay.msDelay(DELAY_COLIBRATE_ROTATE);
    		motor_r.stop(true);
    		motor_l.stop(true);
    		motor_r.waitComplete();
        	motor_l.waitComplete();
    		((EV3GyroSensor)gyroSensor).reset();
    		perfectRotationAngle = 0;
    		motor_r.rotate(COLIBRATE_ROTATE_FORWARD_DEGREES, true);
    		motor_l.rotate(COLIBRATE_ROTATE_FORWARD_DEGREES, true);
    		motor_r.waitComplete();
        	motor_l.waitComplete();
    	}
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
    	
    	motor_l.forward();
    	motor_r.forward();
    	
    	Delay.msDelay(1000);
    	
    	motor_r.stop(true);
    	motor_l.stop(true);
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
    		return true;
    	}
    	else {
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
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    private void colibrateOverRightSide()
    {
    	motor_r.setSpeed(MAX_SPEED);
    	motor_r.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE, true);
    	motor_r.waitComplete();
    	motor_l.setSpeed(MAX_SPEED);
    	motor_l.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE, true);
    	motor_l.waitComplete();
    	int _backToStart = COLIBRATE_DISTANCE_ROTATION_BACK_ANGLE;
    	Delay.msDelay(ERROR_DELAY);
    	motor_r.rotate(_backToStart, true);
    	motor_l.rotate(_backToStart, true);
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    	
    }
    
    private void colibrateOverLeftSide()
    {
    	motor_l.setSpeed(MAX_SPEED);
    	motor_l.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE, true);
    	motor_l.waitComplete();
    	motor_r.setSpeed(MAX_SPEED);
    	motor_r.rotate(COLIBRATE_DISTANCE_ROTATION_ANGLE, true);
    	motor_r.waitComplete();
    	int _backToStart = COLIBRATE_DISTANCE_ROTATION_BACK_ANGLE;
    	motor_r.rotate(_backToStart, true);
    	motor_l.rotate(_backToStart, true);
    	motor_r.waitComplete();
    	motor_l.waitComplete();
    }
    
    @Override
    public void forwardToWall()
    {
        // For errors with time
        Delay.msDelay(ERROR_DELAY);

        // Make ready motors for start
        isWallBack = false;
        motor_r.setSpeed(MAX_SPEED);
        motor_l.setSpeed(MAX_SPEED);
        motor_r.setAcceleration(ACCELERATION);
        motor_l.setAcceleration(ACCELERATION);
        motor_r.waitComplete();
        motor_l.waitComplete();

        // Init regulator's variables
        float [] _nowGyroSamples = new float[gyro.sampleSize()];
        gyro.fetchSample(_nowGyroSamples, 0);
        float _nowDegrees = _nowGyroSamples[0];
        int _errorRotation;
        int _controlRotationPRegulator;
        int _controlRotationDRegulator;
        int _controlRegulatorSum;
        int _oldRotationDRegulator = (int)_nowDegrees;

        // Start motors
        motor_r.forward();
        motor_l.forward();

        // Start regulating
        while(motor_r.isMoving() || motor_l.isMoving())
        {
            gyro.fetchSample(_nowGyroSamples, 0);
            _nowDegrees = _nowGyroSamples[0];
            _errorRotation = (int)(perfectRotationAngle - _nowDegrees);
            _controlRotationPRegulator = (int)(_errorRotation * P_REGULATOR_MOVE_COEF);
            _controlRotationDRegulator = (int)((_nowDegrees - _oldRotationDRegulator) * D_REGULATOR_MOVE_COEF);
            _controlRegulatorSum = _controlRotationDRegulator + _controlRotationPRegulator;
            motor_r.setSpeed(MAX_SPEED + _controlRegulatorSum);
            motor_l.setSpeed(MAX_SPEED - _controlRegulatorSum);

            // Stop moving
            if(isWallNearForward())
            {
                motor_r.stop(true);
                motor_l.stop(true);
            }
        }

        // Make motors standart state
        motor_r.waitComplete();
        motor_l.waitComplete();
        motor_r.setSpeed(MAX_SPEED);
        motor_l.setSpeed(MAX_SPEED);
    }

    @Override
    public void backToWall() {

    }

    @Override
    public boolean isWallNearLeft()
    {
    	float _distance;
    	float[] _nowSample = new float[sonic_l.sampleSize()];
    	sonic_l.fetchSample(_nowSample, 0);
    	_distance = _nowSample[0];
        return _distance < CHECK_NEAR_WALL_DISTANCE;
    }

    @Override
    public boolean isWallNearForward()
    {
    	float _distance;
    	float[] _nowSample = new float[sonic_f.sampleSize()];
    	sonic_f.fetchSample(_nowSample, 0);
    	_distance = _nowSample[0];
        return _distance < CHECK_NEAR_WALL_DISTANCE;
    }

    @Override
    public boolean isWallNearRight()
    {
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
    public int checksCountToWallAtLeft()
    {
        int _count;
        double _distance;
        float[] _sample = new float[sonic_l.sampleSize()];
        sonic_l.fetchSample(_sample, 0);
        _distance = (double)_sample[0];
        _count = (int)(_distance / CHECK_LENGTH);
    	return _count;
    }

    @Override
    public int checksCountToWallAtRight()
    {
    	int _count;
        double _distance;
        float[] _sample = new float[sonic_r.sampleSize()];
        sonic_r.fetchSample(_sample, 0);
        _distance = (double)_sample[0];
        _count = (int)(_distance / CHECK_LENGTH);
    	return _count;
    }

    @Override
    public int checksCountToWallAtBack()
    {
        int _count;
        turnRight();
        _count = checksCountToWallAtRight();
        turnLeft();
    	return _count;
    }

    @Override
    public int checksCountToWallAtForward()
    {
    	int _count;
        double _distance;
        float[] _sample = new float[sonic_f.sampleSize()];
        sonic_f.fetchSample(_sample, 0);
        _distance = (double)_sample[0];
        _count = (int)(_distance / CHECK_LENGTH);
    	return _count;
    }

    public void resetMoveCount()
    {
        motor_r.resetTachoCount();
        motor_l.resetTachoCount();
    }

    public int getCheckMoved()
    {
        double _count = motor_r.getTachoCount() /  CHECK_LENGTH;
        return (int)_count;
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
