package ru.li24robotics.ev3.robolab.move;


public interface IMoveRobot {
    public void forwardForChecks(int countCheck);
    public void backForChecks(int countCheck);
    public void turnRight();
    public void turnLeft();
    public void turnBack();
    public void forwardToWall();
    public void backToWall();
}
