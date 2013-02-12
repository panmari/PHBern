// SimEx16.java
// Search gap with ultrasonic sensor
// Set targets by left click, start by right click
// Algorithm for searching track: 
// Look for 8 consecutive gaps (beam area free of targets)

import ch.aplu.nxtsim.*;
import java.awt.Color;
import java.awt.Point;
import ch.aplu.jgamegrid.*;
import ch.aplu.util.*;

public class SimEx16 implements GGMouseListener
{
  private final Point[] mesh =
  {
    new Point(50, 0), new Point(25, 42), new Point(-25, 42),
    new Point(-50, 0), new Point(-25, -42), new Point(25, -42)
  };
  private UltrasonicSensor us;
  private TurtleRobot robot = new TurtleRobot();
  private GameGrid gg = robot.getGameGrid();

  public SimEx16()
  {
    gg.setTitle("LeftClick to Set Targets - RigthClick to Start");
    us = new UltrasonicSensor(SensorPort.S3);
    us.setBeamAreaColor(Color.green);
    robot.addPart(us);

    Target target = new Target("sprites/target_red.gif", mesh);
    robot.addTarget(target, 250, 150);
    gg.addMouseListener(this, GGMouse.lPress | GGMouse.rPress);
    Monitor.putSleep();
    gg.setTitle("Searching track now...");
    runRobotProgram();
  }

  private void runRobotProgram()
  {
    searchTrack();
    gg.setTitle("Track found. Moving now...");
    robot.forward();
  }

  private void searchTrack()
  {
    boolean found = false;
    int nbGap = 0;
    int inc = 3;
    while (!found)
    {
      int value = us.getDistance();
      if (value == -1)
      {
        nbGap++;
        if (nbGap == 8)
        {
          found = true;
          us.eraseBeamArea();
          robot.left(4 * inc);
        }
      }
      else
        nbGap = 0;
      Tools.delay(200);
      robot.right(inc);
    }
  }

  public boolean mouseEvent(GGMouse mouse)
  {
    if (mouse.getEvent() == GGMouse.lPress)
    {
      Location location = gg.toLocationInGrid(mouse.getX(), mouse.getY());
      Target target = new Target("sprites/target_red.gif", mesh);
      robot.addTarget(target, location.x, location.y);
    }
    if (mouse.getEvent() == GGMouse.rPress)
      Monitor.wakeUp();
    return true;
  }

  public static void main(String[] args)
  {
    new SimEx16();
  }

}
