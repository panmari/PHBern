import ch.aplu.nxtsim.Gear;
import ch.aplu.nxtsim.NxtRobot;
import ch.aplu.nxtsim.SensorPort;
import ch.aplu.nxtsim.TouchSensor;
import ch.aplu.nxtsim.TurtleRobot;
import ch.aplu.nxtsim.UltrasonicSensor;

public class BumpBot
{
  private static NxtRobot robot;  // Static, because used in _init()
  private Gear gear;
  private UltrasonicSensor us;
  private TouchSensor ts;
  private final int nearThreshold = 24; //the US used by us doesn't return lower values than 24
  private final int targetThreshold = 200;
  
  public BumpBot()
  {
    robot = new TurtleRobot();
    gear = new Gear();
    robot.addPart(gear);
    gear.setSpeed(10);
    us = new UltrasonicSensor(SensorPort.S2);
    robot.addPart(us);
    runRobotProgram();
  }

  private void runRobotProgram()
  {
    while (true)
    {
      int distance = us.getDistance();
      System.out.println("" + distance);
      if (distance <= nearThreshold)
      {
        gear.forward(1000); //get a bit closer
        gear.setSpeed(70); //use tail to budge obstacle
        gear.right(1000);
        gear.stop();
        return;
      }
      
    
      if (distance >= 255) //nothing in sight
        searchTarget();
      else
        gear.forward();
    }
  }

  private void searchTarget()
  {
    while (true)
    {
      gear.right(100);
      int distance = us.getDistance();
      System.out.println("" + distance);
      //robot.playTone(1000, 10);
      if (distance < targetThreshold)
      {
        gear.forward();
        return;
      }
    }
  }

  public static void main(String[] args)
  {
    new BumpBot();
  }
}