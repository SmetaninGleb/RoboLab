package ru.li24robotics.ev3.robolab.lab;

/**
 * Класс описывает стену либиринта. LabItem состоит из данных классов
 *
 * @see ru.li24robotics.ev3.robolab.lab.LabItem
 * @author Smetanin Gleb
 */
public class LabWall {
	/**
	 * Два поля хранят известно ли что нибудь о данной стене, и если известно
	 * то есть ли здесь стена, или нет
	 */
	private boolean WallIsHere;
	private boolean NothingAboutWallHere;


	/**
	 * Конструктор приводит стену к первоначальному стандартному виду,
	 * когда о стене ничего не известно
	 */
	public LabWall() {
		WallIsHere = false;
		NothingAboutWallHere = true;
	}


	/**
	 * Стандартный equals.
	 * Помни, что неизвестная стена равняется любой другой! Таков наш алгоритм.
	 *
	 * @param obj
	 */
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
