package ru.li24robotics.ev3.robolab.move;

import lejos.robotics.RegulatedMotor;

public class MoveRobot {
	
	private static float checkSize = 0.3f;
	private static RegulatedMotor leftMotor;
	private static RegulatedMotor rightMotor;
	private static float wheelCircumference;
	
	public static void forwardAbout(int checks){
		float moveDistance = checks * getCheckSize();
		float moveDegrees = moveDistance / wheelCircumference;
		
		leftMotor.synchronizeWith(new RegulatedMotor[] {rightMotor});
		leftMotor.startSynchronization();
		leftMotor.rotate((int) moveDegrees);
		rightMotor.rotate((int) moveDegrees);
		leftMotor.endSynchronization();
	}
	
	public static void turnRight(){
		
	}
	
	public static void turnLeft(){
		
	}
	
	public static void backAbout(int checks){
		
	}
	
	//getters and setters
	public static float getCheckSize() {
		return checkSize;
	}

	public static void setCheckSize(float checkSize) {
		MoveRobot.checkSize = checkSize;
	}

	public static RegulatedMotor getLeftMotor() {
		return leftMotor;
	}

	public static void setLeftMotor(RegulatedMotor leftMotor) {
		MoveRobot.leftMotor = leftMotor;
	}

	public static RegulatedMotor getRightMotor() {
		return rightMotor;
	}

	public static void setRightMotor(RegulatedMotor rightMotor) {
		MoveRobot.rightMotor = rightMotor;
	}

	public static float getWheelCircumference() {
		return wheelCircumference;
	}

	public static void setWheelCircumference(float wheelCircumference) {
		MoveRobot.wheelCircumference = wheelCircumference;
	}
}
