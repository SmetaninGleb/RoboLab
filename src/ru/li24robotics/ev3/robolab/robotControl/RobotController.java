package ru.li24robotics.ev3.robolab.robotControl;


public class RobotController implements IRobotController{
    @Override
    public void forwardForChecks(int countCheck) {

    }

    @Override
    public void backForChecks(int countCheck) {

    }

    @Override
    public void turnRight() {

    }

    @Override
    public void turnLeft() {

    }

    @Override
    public void turnBack() {

    }

    @Override
    public void forwardToWall() {

    }

    @Override
    public void backToWall() {

    }

    @Override
    public boolean isWallNearLeft() {
        return false;
    }

    @Override
    public boolean isWallNearForward() {
        return false;
    }

    @Override
    public boolean isWallNearRight() {
        return false;
    }

    @Override
    public boolean isWallNearBack() {
        return false;
    }

    @Override
    public int checksCountToWallAtLeft() {
        return 0;
    }

    @Override
    public int checksCountToWallAtRight() {
        return 0;
    }

    @Override
    public int checksCountToWallAtBack() {
        return 0;
    }

    @Override
    public int checksCountToWallAtForward() {
        return 0;
    }
}
