// TurnLeft.java

import ch.aplu.nxtsim.Gear;
import ch.aplu.nxtsim.NxtRobot;

class TurnLeft
{
  TurnLeft()
  {
    NxtRobot robot = new NxtRobot();
    Gear gear = new Gear();
    robot.addPart(gear);
    gear.forward(2000);
    gear.setSpeed(30);
    long start = System.currentTimeMillis();
    gear.left(500);
    long end = System.currentTimeMillis();
    System.out.println("" + (end -start));
    gear.forward(2000);
    robot.exit();
  }
  public static void main(String[] args)
  {
    new TurnLeft();
  }
}