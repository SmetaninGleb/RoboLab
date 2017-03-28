package ru.li24robotics.ev3.robolab.robotControl;


public interface IWatcher {
    public boolean isWallNearLeft();
    public boolean isWallNearForward();
    public boolean isWallNearRight();
    public boolean isWallNearBack();

    public int checksCountToWallAtLeft();
    public int checksCountToWallAtRight();
    public int checksCountToWallAtBack();
    public int checksCountToWallAtForward();
}
