// SearchMiddle

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import ch.aplu.nxtsim.NxtContext;
import ch.aplu.nxtsim.SensorPort;
import ch.aplu.nxtsim.TurtleRobot;
import ch.aplu.nxtsim.UltrasonicSensor;

class AlignToWall
{
  private final static int alignStepSize = 2;

  AlignToWall()
  {
    TurtleRobot robot = new TurtleRobot();
    robot.setTurtleSpeed(30);
    UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
    us.setBeamAreaColor(Color.DARK_GRAY);
    us.setProximityCircleColor(Color.GREEN);
    us.setMeshTriangleColor(Color.RED);
    robot.addPart(us);
    
    //align robot perpendicular to the walls, assuming we're in a rectangular room.
    int previousDistance = Integer.MAX_VALUE;
    int successiveSameDistance = 0;
    while (true) {
    	int distance = us.getDistance();
     	if (Math.abs(distance - previousDistance) < 1)
      	   successiveSameDistance++;
          else {
          	  if(successiveSameDistance > 5) //minimal threshold
              break;
            else //look further for wall
              successiveSameDistance = 0;
          }
          previousDistance = distance;
          robot.right(alignStepSize);
          System.out.println("" + distance);
    }
    //align according to measurements from before:
   robot.left(Math.round(alignStepSize*successiveSameDistance/2f) + 2);  
   System.out.println("I'm aligned!");
  }

  public static void main(String[] args)
  {
    new AlignToWall();
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
    NxtContext.setStartDirection(rnd.nextInt(360));
  }
}