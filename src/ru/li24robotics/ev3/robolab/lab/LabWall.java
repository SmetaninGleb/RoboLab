package ru.li24robotics.ev3.robolab.lab;

public class LabWall {
	private boolean WallIsHere;
	private boolean NothingAboutWallHere;
	
	
	
	public LabWall() {
		WallIsHere = false;
		NothingAboutWallHere = true;
	}



	@Override
	public boolean equals(Object obj) {
		LabWall wall = (LabWall) obj;
		if(this.NothingAboutWallHere == true){
			return true;
		}
		
		if(this.WallIsHere == wall.WallIsHere){
			return true;
		}else{
			return false;
		}
	}



	public boolean isWallIsHere() {
		return WallIsHere;
	}



	public void setWallIsHere(boolean wallIsHere) {
		WallIsHere = wallIsHere;
		NothingAboutWallHere = false;
	}



	public boolean isNothingAboutWallHere() {
		return NothingAboutWallHere;
	}



	public void setNothingAboutWallHere(boolean nothingAboutWallHere) {
		NothingAboutWallHere = nothingAboutWallHere;		
	}
	
	
}
