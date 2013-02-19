// SimEx1.java
// Motors

import ch.aplu.nxtsim.*;

public class SimEx1
{
  public SimEx1()
  {
    NxtRobot robot = new NxtRobot();
    Motor motA = new Motor(MotorPort.A);
    Motor motB = new Motor(MotorPort.B);
    robot.addPart(motA);
    robot.addPart(motB);

    motA.forward();
    motB.forward();
    Tools.delay(2000);

    motA.stop();
    Tools.delay(2000);

    motB.stop();
    Tools.delay(2000);

    motA.backward();
    motB.forward();
    Tools.delay(2000);

    motB.backward();
    Tools.delay(2000);

    robot.exit();
  }

  public static void main(String[] args)
  {
    new SimEx1();
  }
}