
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import ch.aplu.nxtsim.NxtContext;
import ch.aplu.nxtsim.SensorPort;
import ch.aplu.nxtsim.TurtleRobot;
import ch.aplu.nxtsim.UltrasonicSensor;

class SearchMiddleEffectively
{

  SearchMiddleEffectively()
  {
    TurtleRobot robot = new TurtleRobot();
    robot.setTurtleSpeed(30);
    UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
    us.setBeamAreaColor(Color.DARK_GRAY);
    us.setProximityCircleColor(Color.GREEN);
    us.setMeshTriangleColor(Color.RED);
    robot.addPart(us);
   
   int[] axisMax = new int[2];
   int[] axisMaxDirection = new int[2];
   int[] roomSize = new int[2];
   for (int i = 0; i < 4; i++) {
      int measuredDistance = us.getDistance();
      roomSize[i % 2] += measuredDistance;
      if (axisMax[i % 2] <= measuredDistance ) {
      	axisMax[i % 2] = measuredDistance;
       	axisMaxDirection[i % 2] = i;
       }
      robot.right(90);
   }
   int[] distancesToMiddle = new int[2];
   for (int i = 0; i < 2; i++){
	 distancesToMiddle[i] = axisMax[i] - roomSize[i]/2;
     if (axisMaxDirection[i] == 2  || axisMaxDirection[i] == 1)
       distancesToMiddle[i] = -distancesToMiddle[i];
   }
   int angle = (int) Math.toDegrees(Math.tan(distancesToMiddle[1]/((double)distancesToMiddle[0])));
   
   
   int diagonalDistance = (int) Math.sqrt(distancesToMiddle[0]*distancesToMiddle[0] + distancesToMiddle[1]*distancesToMiddle[1]);
   System.out.println(diagonalDistance + ": " + angle);
   turn(robot, angle);
   robot.forward(diagonalDistance);
  }
  
  private void turn(TurtleRobot robot, int angle) {
    if (angle < 0)
      robot.left(-angle);
    else
      robot.right(angle);
  }

  public static void main(String[] args)
  {
    new SearchMiddleEffectively();
  }

  // ------------------ Environment --------------------------
  static
  {
    Point[] meshHorizontal =
    {
      new Point(0, 0), new Point(500, 0), new Point(500, 10), new Point(0, 10)
    };
    Point[] meshVertical =
    {
      new Point(0, 0), new Point(10, 0), new Point(10, 500), new Point(0, 500)
    };
    BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
    NxtContext.useTarget(dummy, meshHorizontal, 0, -10);
    NxtContext.useTarget(dummy, meshHorizontal, 0, 500);
    NxtContext.useTarget(dummy, meshVertical, -10, 0);
    NxtContext.useTarget(dummy, meshVertical, 500, 0);

    Random rnd = new Random();
    int x = rnd.nextInt(400) + 50;
    int y = rnd.nextInt(400) + 50;
    NxtContext.setStartPosition(x, y);
    //NxtContext.setStartDirection(rnd.nextInt(360));
  }
}