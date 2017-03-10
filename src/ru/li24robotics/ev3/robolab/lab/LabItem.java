package ru.li24robotics.ev3.robolab.lab;

public class LabItem {
    public LabWall toRight;
    public LabWall toBack;
    public LabWall toLeft;
    public LabWall toForward;
    public boolean isCubeHere;
    //for debug!
    //TODO delete key
    private String key;

    public LabItem(String key) {
        this.key = key;
        this.toBack = new LabWall();
        this.toForward = new LabWall();
        this.toRight = new LabWall();
        this.toLeft = new LabWall();
    }


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
        		&& this.toLeft.equals(item.toForward)
        		&& this.toRight.equals(item.toBack)){
        	return true;
        }else{
        	return false;
        }
    }

    @Override
    public String toString() {
        return key;
    }


}
