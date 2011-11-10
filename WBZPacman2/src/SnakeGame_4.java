// SnakeGame_4.java, GameOver, if at Boarder

import ch.aplu.jgamegrid.*;
import java.awt.Color;

public class SnakeGame_4 extends GameGrid
{
  private final int startingSpeed = 400;

  public SnakeGame_4()
  {
    super(20, 20, 20, Color.darkGray, null, false);
    Snake snake = new Snake();
    addActor(snake, new Location(10, 10));
    addActor(new Food(), getRandomEmptyLocation());
    snake.setDirection(Location.NORTH);
    show();
    setSimulationPeriod(startingSpeed);
    doRun();
  }

  public static void main(String[] args)
  {
    new SnakeGame_4();
  }
}