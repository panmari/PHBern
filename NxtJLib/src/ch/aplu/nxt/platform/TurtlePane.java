// TurtlePane.java, Java SE version
// Platform (Java SE, ME) dependent code
// Should be visible in package only. Not included in Javadoc

/*
This software is part of the NxtJLib library.
It is Open Source Free Software, so you may
- run the code for any purpose
- study how the code works and adapt it to your needs
- integrate all or parts of the code in your own programs
- redistribute copies of the code
- improve the code and release your improvements to the public
However the use of the code is entirely your responsibility.
 */
package ch.aplu.nxt.platform;

import ch.aplu.nxt.*;
import ch.aplu.util.*;

public class TurtlePane extends Console implements ExitListener
{
  private Console console = null;
  private NxtRobot robot;

  public TurtlePane(NxtRobot robot)
  {
    this.robot = robot;
    setTitle("TurtleRobot's Console");
    println("List of turtle commands:");
    addExitListener(this);
  }

  public void notifyExit()
  {
    robot.exit();
  }
}
 
