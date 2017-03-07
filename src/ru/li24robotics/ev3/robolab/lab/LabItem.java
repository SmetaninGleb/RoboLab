package ru.li24robotics.ev3.robolab.lab;

public class LabItem {
    public boolean toRight;
    public boolean toBack;
    public boolean toLeft;
    public boolean toForward;
    public boolean isCubeHere;
    //for debug!
    //TODO delete key
    private String key;

    public LabItem(String key) {
        this.key = key;
        this.toRight = true;
        this.toBack = true;
        this.toLeft = true;
        this.toForward = true;
    }

    @Override
    public boolean equals(Object obj) {
        LabItem item = (LabItem) obj;
        if (item != null) {
            if (this.isCubeHere == item.isCubeHere
                    && this.toBack == item.toBack
                    && this.toLeft == item.toLeft
                    && this.toRight == item.toRight
                    && this.toForward == item.toForward) {
                return true;
            }else{
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return key;
    }


}
