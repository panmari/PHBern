package followSmoothly;
// JGameEx16.java

import ch.aplu.jgamegrid.*;
import java.awt.Color;
import java.awt.Point;

public class JGameEx16 extends GameGrid
{
  public JGameEx16()
  {
    super(10, 10, 60, Color.red);
    Ball ball = new Ball();
    addActor(ball, new Location(5, 9));
    addActor(new Playball(ball), new Location(0, 0));
    show();
  }

  public void act() {
	  
  }
  
  public static void main(String[] args)
  {
    new JGameEx16();
  }
}

class Playball extends Actor
{
  private Ball ball;
  private double vx, vy, x, y;
  private final double factor = 10;

  public Playball(Ball ball)
  {
    super("sprites/playStone_0.png");
    this.ball = ball;
  }

  public void moveSmoothly() 
  {
	x = vx*factor + x;
	y = vy*factor + y;
	this.setPixelLocation(new Point((int) x, (int) y));
  }
  
  public void act()
  {
	setSmoothDirection();
    moveSmoothly();
    tryToTouch();
  }

  private void tryToTouch()
  {
    if (ball.getLocation().equals(this.getLocation()))
    {
      ball.reset();
      setSmoothDirection();
    }
  }

private void setSmoothDirection() {
	Point ballPos = ball.getPixelLocation();
	x = getPixelLocation().x;
	y = getPixelLocation().y;
	int vx = ballPos.x - getPixelLocation().x;
	int vy = ballPos.y - getPixelLocation().y;
	double vectorLength = getPixelLocation().distance(ballPos);
	this.vx = vx / vectorLength;
	this.vy = vy / vectorLength;
}
}

// --------------------- class Ball ---------------------------
class Ball extends Actor
{
  public Ball()
  {
    super(true, "sprites/ball.gif");
  }
  
  public void reset() {
	  setLocation(gameGrid.getRandomEmptyLocation());
  }
}
