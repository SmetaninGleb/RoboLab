package ru.li24robotics.ev3.robolab.lab;

public class LabItem {
	public boolean toRight;
	public boolean toDown;
	public boolean toLeft;
	public boolean toUp;
	public boolean isCubeHere;
	//for debug!
	//TODO delete key
	private String key;
	
	public LabItem(String key) {
		this.key = key;
		this.toRight = true;
		this.toDown = true;
		this.toLeft = true;
		this.toUp = true;
	}

	@Override
	public String toString() {
		return key;
	}
	
	
}
