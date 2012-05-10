// NxtContext.java

/*
This software is part of the NxtSim library.
It is Open Source Free Software, so you may
- run the code for any purpose
- study how the code works and adapt it to your needs
- integrate all or parts of the code in your own programs
- redistribute copies of the code
- improve the code and release your improvements to the public
However the use of the code is entirely your responsibility.

Author: Aegidius Pluess, www.aplu.ch
*/

package ch.aplu.nxtsim;

import ch.aplu.jgamegrid.*;
import java.util.*;

/**
 * Class to select user defined initial conditions of the
 * playground and the Nxt robot.
 */
public class NxtContext
{
  protected static String imageName =  null;
  protected static Location startLocation = new Location(250, 250);
  protected static double startDirection = -90;
  protected static boolean isNavigationBar = false;
  protected static ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
  protected static ArrayList<Location> obstacleLocations = new ArrayList<Location>();
  protected static boolean isRun = true;
  protected static int xLoc = -1;
  protected static int yLoc = -1;
  /**
   * Box obstacle.
   */
  public static Obstacle box = new Obstacle("sprites/box.gif");
  /**
   * Channel obstacle.
   */
  public static Obstacle channel = new Obstacle("sprites/channel.gif");

  private NxtContext()
  {}

  /**
   * Use the give image as background (playground size 501 x 501).
   * @param filename the image file to use as background.
   */
  public static void useBackground(String filename)
  {
    imageName = filename;
  }

  /**
   * Sets the Nxt starting position (x-y-coordinates 0..500, origin at upper left).
   * @param x the x-coordinate of the starting position
   * @param y the y-coordinate of the starting position
   */
  public static void setStartPosition(int x, int y)
  {
    startLocation = new Location(x, y);
  }

  /**
   * Sets the Nxt starting direction (zero to EAST).
   * @param direction the starting direction in degrees)
   */
  public static void setStartDirection(double direction)
  {
    startDirection = direction;
  }

 /**
  * Sets the location of the playground (pixel coordinates of the upper left vertex).
  * @param x the x-pixel-coordinate of the upper left vertex (positive to the right)
  * @param y the y-pixel-coordinate of the upper left vertex (positive to the bottom)
  */
  public static void setLocation(int x, int y)
  {
    xLoc = x;
    yLoc = y;
  }

  /**
   * Defines the give images as an obstacle. It will be shown at the given
   * position. More than one obstacle may be defined.
   * @param filename the image file of the obstacle
   * @param x the x-coordinate of the image center
   * @param y the y-coordinate of the image center
   */
  public static void useObstacle(String filename, int x, int y)
  {
    Obstacle obstacle = new Obstacle(filename);
    obstacles.add(obstacle);
    obstacleLocations.add(new Location(x, y));
  }

  /**
   * Uses the given obstacle at the center of the playground. Mainly used for
   * predefined obstacles.
   * @param obstacle the obstacle to use.
   */
  public static void useObstacle(Obstacle obstacle)
  {
    obstacles.add(obstacle);
    obstacleLocations.add(new Location(250, 250));
  }

  /**
   * Shows the navigation bar of the GameGrid.
   * @param doRun if true, runs the simulation immediatetly; otherwise the start
   * button must be hit to run the simulation
   */
  public static void showNavigationBar(boolean doRun)
  {
    isNavigationBar = true;
    isRun = doRun;
  }

  /**
   * Shows the navigation bar of the GameGrid and runs the simulation immediately.
   */
  public static void showNavigationBar()
  {
    showNavigationBar(true);
  }
}
