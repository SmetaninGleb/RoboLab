package ru.li24robotics.ev3.robolab.lab;


import java.io.Serializable;

/**
 * Класс описывает ячейку либаринта, состоящую из четырех сторон, где могут
 * быть стены
 *
 * @see ru.li24robotics.ev3.robolab.lab.LabWall
 * @author Smetanin Gleb
 */
public class LabItem implements Serializable{
    /**
     * Поля хранят четыре стены соответственно и ключ, нужный для отслеживания
     * работы алгоритма
     *
     * @see ru.li24robotics.ev3.robolab.lab.LabWall
     */
    public LabWall toRight;
    public LabWall toBack;
    public LabWall toLeft;
    public LabWall toForward;
    public boolean isCubeHere;
    //for debug!
    //TODO delete key
    private String key;

    /**
     * Конструктор принимает ключ и создает стены
     * @param key
     */
    public LabItem(String key) {
        this.key = key;
        this.toBack = new LabWall();
        this.toForward = new LabWall();
        this.toRight = new LabWall();
        this.toLeft = new LabWall();
    }


    /**
     * Четыре метода equals сравнивают с поправкой на возможные повороты робота
     * в главном лабиринте. (Да, некрасивая реализация, но таков мой красивый алгоритм!)
     * @param item
     * @return
     */
    public boolean equalsNotRotate(LabItem item) {
        if(this.isCubeHere != item.isCubeHere){
        	return false;
        }
        if(this.toBack.equals(item.toBack)
        		&& this.toForward.equals(item.toForward)
        		&& this.toLeft.equals(item.toLeft)
        		&& this.toRight.equals(item.toRight)){
        	return true;
        }else{
        	return false;
        }
    }

    public boolean equalsRightRotate(LabItem item){
    	if(this.isCubeHere != item.isCubeHere){
        	return false;
        }
        if(this.toBack.equals(item.toLeft)
        		&& this.toForward.equals(item.toRight)
        		&& this.toLeft.equals(item.toForward)
        		&& this.toRight.equals(item.toBack)){
        	return true;
        }else{
        	return false;
        }
    }

    public boolean equalsOverRotated(LabItem item){
    	if(this.isCubeHere != item.isCubeHere){
        	return false;
        }
        if(this.toBack.equals(item.toForward)
        		&& this.toForward.equals(item.toBack)
        		&& this.toLeft.equals(item.toRight)
        		&& this.toRight.equals(item.toLeft)){
        	return true;
        }else{
        	return false;
        }
    }

    public boolean equalsLeftRotated(LabItem item) {
    	if(this.isCubeHere != item.isCubeHere){
        	return false;
        }
        if(this.toBack.equals(item.toRight)
        		&& this.toForward.equals(item.toLeft)
        		&& this.toLeft.equals(item.toBack)
        		&& this.toRight.equals(item.toForward)){
        	return true;
        }else{
        	return false;
        }
    }

    /**
     * toString реализован для вывода сканируемого лабиринта
     * @return
     */
    @Override
    public String toString() {
        return key;
    }


}
