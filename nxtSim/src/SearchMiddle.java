
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import ch.aplu.nxtsim.NxtContext;
import ch.aplu.nxtsim.SensorPort;
import ch.aplu.nxtsim.TurtleRobot;
import ch.aplu.nxtsim.UltrasonicSensor;

class SearchMiddle
{
  SearchMiddle()
  {
    int[] distances = new int[4];
	TurtleRobot robot = new TurtleRobot();
    robot.setTurtleSpeed(30);
    UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
    //only for making things visible in simulation:
    us.setBeamAreaColor(Color.DARK_GRAY);
    us.setProximityCircleColor(Color.GREEN);
    us.setMeshTriangleColor(Color.RED);
    //--------------------------------
    robot.addPart(us);
    
    //assumes, the robot is initially facing a wall!
    int steps = 0;
    while (true) {
    	if (steps % 4 == 0 && steps != 0) {
    		int farthestDirection = 0;
    		for (int i = 0; i < 4; i++) {
    			if (distances[i] > distances[farthestDirection])
    				farthestDirection = i;
    		}
    		//check if close enough to center:
    		boolean closeCenter = true;
    		for (int i = 0; i < 4; i++) {
    			if (distances[farthestDirection] - distances[i] > 20)
    				closeCenter = false;
    		}
    		if (closeCenter) {
    			System.out.println("Found middle of room in "+ steps + " steps!");
    			robot.exit();
    		}
    		robot.right(farthestDirection*90); //turn towards farthest wall
    		int approachDistance = Math.min(100/(steps/4), 15);  
    		robot.forward(approachDistance);
    	}
    	distances[steps % 4] = us.getDistance();
    	System.out.println(us.getDistance());
    	robot.right(90); 
    	steps++;
    }
  }
	
  public static void main(String[] args) 
  {
    new SearchMiddle();
  }

// ------------------ Environment --------------------------
static
  {
	//meshes are defined in target space, not world space!
    Point[] meshHorizontal =
    {
      new Point(0, 0), new Point(500, 0), new Point(500, 10), new Point(0, 10)
    };
    Point[] meshVertical =
    {
      new Point(0, 0), new Point(10, 0), new Point(10, 500), new Point(0, 500)
    };
    BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
    NxtContext.useTarget(dummy, meshHorizontal, 0, 0);
    NxtContext.useTarget(dummy, meshHorizontal, 0, 490);
    NxtContext.useTarget(dummy, meshVertical, 0, 0);
    NxtContext.useTarget(dummy, meshVertical, 490, 0);
    
    Random rnd = new Random();
    int x = rnd.nextInt(400) + 50;
    int y = rnd.nextInt(400) + 50;
    NxtContext.setStartPosition(x, y);
  }
  
}