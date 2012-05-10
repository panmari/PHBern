// TouchSensor.java

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
import java.awt.*;

/**
 * Class that represents a touch sensor.
 */
public class TouchSensor extends Part
{
  private class SensorThread extends Thread
  {
    public void run()
    {
      while (isRunning)
      {
        synchronized (monitor)
        {
          try
          {
            monitor.wait();
          }
          catch (InterruptedException ex)
          {
          }
        }
        if (!isPressNotified)
        {
          isPressNotified = true;
          touchListener.pressed(port);
        }
        if (!isReleaseNotified)
        {
          isReleaseNotified = true;
          touchListener.released(port);
        }
      }
      touchListener = null;
    }
  }
  // -------------- End of inner class ------------------
  //
  private static final Location pos1 = new Location(6, 10);
  private static final Location pos2 = new Location(6, -10);
  private static final Location pos3 = new Location(6, 0);
  private static final Point startPoint = new Point(5, -4);
  private static final Point endPoint = new Point(5, 4);
  private int nbObstacles = 0;
  private TouchListener touchListener = null;
  private SensorPort port;
  private Actor collisionActor =  null;
  private final SensorThread st = new SensorThread();
  private volatile boolean isRunning = false;
  private volatile boolean isPressNotified = true;
  private volatile boolean isReleaseNotified = true;
  protected final Object monitor = new Object();

  /**
   * Creates a sensor instance connected to the given port.
   * @param port the port where the sensor is plugged-in
   */
  public TouchSensor(SensorPort port)
  {
    super("sprites/touchsensor.gif", port == SensorPort.S1
      ? pos1 : (port == SensorPort.S2 ? pos2 : pos3));
    this.port = port;
    setCollisionLine(startPoint, endPoint);
  }

  protected void cleanup()
  {
    isRunning = false;
  }

  /**
   * Register the given TouchListener to detect press or release events.
   * Starts a internal sensor thread that polls the sensor level and runs the
   * sensor callbacks.
   * @param listener the LightListener to register; null, to terminate any running
   * sensor thread
   */
  public void addTouchListener(TouchListener listener)
  {
    if (listener != null)
    {
      if (touchListener == null)
      {
        isRunning = true;
        st.start();
      }
      touchListener = listener;
    }
    else
      isRunning = false;
  }

  /**
   * For internal use only (overrides Actor.act()).
   */
  public void act()
  {
    // Add new obstacles as collision actor
    int nb = NxtContext.obstacles.size();
    if (nb > nbObstacles)
    {
      for (int i = nb - 1; i >= nbObstacles; i--)
        addCollisionActor(NxtContext.obstacles.get(i));
      nbObstacles = nb;
    }

    if (touchListener != null)
    {
      if (collisionActor == null && isPressed())
      {
        isPressNotified = false;
        synchronized (monitor)
        {
          monitor.notify();
        }
      }
      if (collisionActor != null && !isPressed())
      {
        isReleaseNotified = false;
        synchronized (monitor)
        {
          monitor.notify();
        }
      }
    }
  }

  protected Actor getCollisionActor()
  {
    return collisionActor;
  }

  /**
   * Polls the touch sensor and returns true, if there is a collision
   * with any of the collision obstacles.
   * Calls Thread.sleep(1) to prevent CPU overload in close polling loops.
   * @return true, if the sensor is pressed; otherwise false
   */
  public boolean isPressed()
  {
    Tools.delay(1);
    for (Actor a : NxtContext.obstacles)
    {
      if (gameGrid.isActorColliding(a, this))
      {
        collisionActor = a;
        return true;
      }
    }
    collisionActor = null;
    return false;
  }
}
