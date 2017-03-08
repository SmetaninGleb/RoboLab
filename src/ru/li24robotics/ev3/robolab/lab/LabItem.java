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


    public boolean equalsNotRotate(LabItem item) {
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

    public boolean equalsRightRotate(LabItem item){
        if(item != null){
            if(this.isCubeHere == item.isCubeHere
                    && this.toForward == item.toRight
                    && this.toRight == item.toBack
                    && this.toBack == item.toLeft
                    && this.toLeft == item.toForward){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

    public boolean equalsOverRotated(LabItem item){
        if(item != null){
            if(this.isCubeHere == item.isCubeHere
                    && this.toForward == item.toBack
                    && this.toRight == item.toLeft
                    && this.toBack == item.toForward
                    && this.toLeft == item.toRight){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

    public boolean equalsLeftRotated(LabItem item) {
        if(item != null){
            if(this.isCubeHere == item.isCubeHere
                    && this.toForward == item.toLeft
                    && this.toRight == item.toForward
                    && this.toBack == item.toRight
                    && this.toLeft == item.toBack){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

    @Override
    public String toString() {
        return key;
    }


}
