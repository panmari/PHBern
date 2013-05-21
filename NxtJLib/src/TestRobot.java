import ch.aplu.nxt.TurtleRobot;

class MyOopNXT 
{
  MyOopNXT() 
  {
    TurtleRobot robot = new TurtleRobot();
    robot.forward(200);
    robot.exit();
  }
	
  public static void main(String[] args) 
  {
    new MyOopNXT();
  }
}
