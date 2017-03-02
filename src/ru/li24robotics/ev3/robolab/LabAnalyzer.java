package ru.li24robotics.ev3.robolab;


import java.util.ArrayDeque;
import java.util.ArrayList;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import ru.li24robotics.ev3.robolab.lab.LabItem;

public class LabAnalyzer {
	Port sensorport;
	SensorModes sensor;
	SampleProvider sensorDistance;
	RegulatedMotor rotationMotor;
	ArrayList<ArrayList<LabItem>> analyzeField;
	float[] sensorSamples;
	
	public LabAnalyzer(String sensorPortName, Port motorPort){
		sensorport = LocalEV3.get().getPort(sensorPortName);
		sensor = new EV3UltrasonicSensor(sensorport);
		sensorDistance = sensor.getMode("Distance");
		rotationMotor = new EV3MediumRegulatedMotor(motorPort);
		sensorSamples = new float [sensorDistance.sampleSize()];
		
		analyzeField = new ArrayList<ArrayList<LabItem>>();
		analyzeField.add(new ArrayList<LabItem>());
		analyzeField.get(0).add(new LabItem());
	}
	
	public void StartAnalyze(){
		int degreesNow = 0;
		float lastdistance = 0f;
		float limitOnWall = 30f;
		float distance = 0f;
		float normaldistance;
		boolean isIncreaseOnWall = true;
		
		while(degreesNow < 360){
			rotationMotor.rotate(1);
			degreesNow ++;
			sensorDistance.fetchSample(sensorSamples, 0);
			distance = sensorSamples[0];
			
			if(degreesNow == 1){
				lastdistance = distance;
				rotationMotor.rotate(1);
				degreesNow ++;
				sensorDistance.fetchSample(sensorSamples, 0);
				distance = sensorSamples[0];
				
				if(distance > lastdistance){
					isIncreaseOnWall = true;
				}else{
					isIncreaseOnWall = false;
				}
				
				continue;
			}
			
			
			if(Math.abs(distance - lastdistance) < limitOnWall){
				if(distance > lastdistance){
					if(isIncreaseOnWall == false){
						normaldistance = distance;
					}
					isIncreaseOnWall = true;
				}else{
					if(isIncreaseOnWall == true){
						normaldistance = distance;
					}
					isIncreaseOnWall =  false;
				}
			}
			
			
			
			
		}
	}
	
	

}
