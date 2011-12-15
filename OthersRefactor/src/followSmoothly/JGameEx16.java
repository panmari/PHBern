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
    Playball playBall = new Playball(ball);
    addActor(ball, new Location(5, 9));
    addActor(playBall, new Location(0, 0));
    show();
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
  private final double speedFactor = 10;

  public Playball(Ball ball)
  {
    super("sprites/playStone_0.png");
    this.ball = ball;
  }
  
  /**
   * This gets called after playball is added to the Gamegrid,
   * so it initializes the direction & the position variables.
   */
  public void reset() {
	  setSmoothDirection();
	  x = getPixelLocation().x;
	  y = getPixelLocation().y;
  }

  public void moveSmoothly() 
  {
	x = vx*speedFactor + x;
	y = vy*speedFactor + y;
	setPixelLocation(new Point((int) x, (int) y));
  }
  
  public void act()
  {
    moveSmoothly();
    tryToTouch();
  }

  private void tryToTouch()
  {
    if (ball.getLocation().equals(getLocation()))
    {
      ball.reset();
      setSmoothDirection();
    }
  }

private void setSmoothDirection() {
	Point ballPos = ball.getPixelLocation();
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
