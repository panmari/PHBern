import ch.aplu.nxtsim.Gear;
import ch.aplu.nxtsim.NxtContext;
import ch.aplu.nxtsim.NxtRobot;
import ch.aplu.nxtsim.SensorPort;
import ch.aplu.nxtsim.TouchSensor;

public class TouchSensorEx1
{
  public TouchSensorEx1()
  {
    NxtRobot robot = new NxtRobot();
    Gear gear = new Gear();
    robot.addPart(gear);
    TouchSensor ts = new TouchSensor(SensorPort.S3);  //creates TouchSensor
    robot.addPart(ts);  //fixes the TouchSensor to the Robot
    gear.setSpeed(30);
    gear.forward();

    while (true)
    {
      if (ts.isPressed())  //if the TouchSensor is pressed do
      {
        gear.backward(1000);
        gear.left(485);
        gear.forward();
      }
    }
  }

  public static void main(String[] args)
  {
    new TouchSensorEx1();
  }

  // ------------------ Environment --------------------------
  static
  {
    NxtContext.useObstacle("sprites/square.gif", 250, 250);  
  }
}