package ru.li24robotics.ev3.robolab.lab;


import java.util.ArrayList;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class LabAnalyzer {
	static Port sensorport;
	static SensorModes sensor;
	static SampleProvider sensorDistance;
	static RegulatedMotor rotationMotor;
	static ArrayList<ArrayList<LabItem>> analyzeField;
	static float[] sensorSamples;
	
	public static void StartLabAnalyzer(String sensorPortName, Port motorPort){
		sensorport = LocalEV3.get().getPort(sensorPortName);
		sensor = new EV3UltrasonicSensor(sensorport);
		sensorDistance = sensor.getMode("Distance");
		rotationMotor = new EV3MediumRegulatedMotor(motorPort);
		sensorSamples = new float [sensorDistance.sampleSize()];
		
		analyzeField = new ArrayList<ArrayList<LabItem>>();
		analyzeField.add(new ArrayList<LabItem>());
		analyzeField.get(0).add(new LabItem());
	}
	
	public static void Analyzing(){
		
	}
	
	

}
