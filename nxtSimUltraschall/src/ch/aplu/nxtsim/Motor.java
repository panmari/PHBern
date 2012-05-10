// Motor.java

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

enum MotorState {FORWARD, BACKWARD, STOPPED};

/**
 * Class that represents one of the NXT motors.
 */
public class Motor extends Part
{
  private static final Location pos = new Location(0, 0);
  private MotorState state = MotorState.STOPPED;
  private int speed = SharedConstants.defaultSpeed;
  private MotorPort port;
  private boolean isMoving = false;

   /**
   * Creates a motor instance that is plugged into given port.
   * @param port the port where the motor is plugged-in (MotorPort.A, MotorPort.B, MotorPort.C)
   */
  public Motor(MotorPort port)
  {
    super(port == MotorPort.A ?
      "sprites/leftmotor.gif" :
      (port == MotorPort.B ? "sprites/rightmotor.gif" :
        "sprites/rightmotor.gif"), pos);
    this.port = port;
  }

  protected void cleanup()
  {
    state = MotorState.STOPPED;
    isMoving = false;
  }

  /**
   * Starts the forward rotation.
   * Method returns immediately, while the rotation continues.
   * @return the object reference to allow method chaining
   */
  public Motor forward()
  {
    state = MotorState.FORWARD;
    if (speed != 0)
      isMoving = true;
    return this;
  }

  /**
   * Starts the backward rotation.
   * Method returns immediately, while the rotation continues.
   * @return the object reference to allow method chaining
   */
  public Motor backward()
  {
    state = MotorState.BACKWARD;
    if (speed != 0)
      isMoving = true;
    return this;
  }

  /**
   * Stops the rotation.
   * @return the object reference to allow method chaining
   */
  public Motor stop()
  {
    state = MotorState.STOPPED;
    isMoving = false;
    return this;
  }

  protected MotorState getState()
  {
    return state;
  }

  /**
   * Sets the speed to the given value (arbitrary units).
   * @param speed the speed 0..100
   * @return the object reference to allow method chaining
   */
  public Motor setSpeed(int speed)
  {
    this.speed = speed;
    if (speed != 0 && state != MotorState.STOPPED)
      isMoving = true;
    return this;
  }

  /**
   * Returns the current speed (arbitrary units).
   * @return the speed 0..100
   */
  public int getSpeed()
  {
    return speed;
  }

  /**
   * Returns the port of the motor.
   * @return the motor port
   */
  public MotorPort getPort()
  {
    return port;
  }

  /**
   * Checks if motor is rotating.
   * Calls Thread.sleep(1) to prevent CPU overload in close polling loops.
   * @return true, if motor is rotating; otherwise false
   */
  public boolean isMoving()
  {
    delay(1);
    return isMoving;
  }
}
