import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.nxtsim.Gear;
import ch.aplu.nxtsim.NxtContext;
import ch.aplu.nxtsim.NxtRobot;
import ch.aplu.nxtsim.Obstacle;
import ch.aplu.nxtsim.SensorPort;
import ch.aplu.nxtsim.Target;
import ch.aplu.nxtsim.TouchSensor;
import ch.aplu.nxtsim.TurtleRobot;
import ch.aplu.nxtsim.UltrasonicSensor;

public class BumpBot
{
  private static NxtRobot robot;  // Static, because used in _init()
  private Gear gear;
  private UltrasonicSensor us;
  private TouchSensor ts;
  /**
   * Distance, when something is close enough to be bumped over
   */
  private final int nearThreshold = 50;
  /**
   * Value, when distance is close enough to drive towards. Has to be set smaller than 255 
   * on a real robot.
   */
  private final int targetThreshold = 500;
  
  public BumpBot()
  {
    robot = new TurtleRobot();
    gear = new Gear();
    robot.addPart(gear);
    gear.setSpeed(10);
    us = new UltrasonicSensor(SensorPort.S1);
    us.setBeamAreaColor(Color.green);  // May be commented out
    us.setProximityCircleColor(Color.lightGray); // May be commented out
    us.setMeshTriangleColor(Color.red);
    robot.addPart(us);
    runRobotProgram();
  }

  private void runRobotProgram()
  {
    while (true)
    {
      int distance = us.getDistance();
      System.out.println("" + distance);
      if (distance <= nearThreshold && distance != -1)
      {
        gear.forward(1000); //get a bit closer
        gear.setSpeed(70); //use tail to budge obstacle
        gear.right(900); 
        removeClosestObstacle();
        //get away from obstacle
        gear.setSpeed(10);
        gear.forward(1000);
      }
      
    
      if (distance >= 255 || distance == -1) //nothing in sight
        searchTarget();
      else
        gear.forward();
    }
  }

  /**
   * Only used in simulation to remove budged over obstacles
   */
  private void removeClosestObstacle() {
	  Location roboLoc = robot.getNxt().getLocation();
	  ArrayList<Actor> targets = robot.getGameGrid().getActors(Target.class);
	  for (Actor a: targets) {
		  int dist = a.getLocation().getDistanceTo(roboLoc);
		  if (dist < 50)
			  a.removeSelf();
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
  
  // ------------------ Environment --------------------------
 static
  {
    Point[] mesh =
    {
      new Point(14, 0), new Point(7, 12), new Point(-7, 12),
      new Point(-14, 0), new Point(-7, -12), new Point(7, -12)
    };
    BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
    Target t = new Target(dummy, mesh);
    NxtContext.useTarget(dummy, mesh, 100, 100);
    NxtContext.useTarget(dummy, mesh, 300, 300);
    NxtContext.useTarget(dummy, mesh, 400, 150);
    NxtContext.showStatusBar(30);
  }
}