// Snake.java

import ch.aplu.jgamegrid.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Snake extends Actor
{
  private ArrayList<TailSegment> tail = new ArrayList<TailSegment>();

  public Snake()
  {
    super(true, "sprites/snakeHead.gif");
  }

  public void act()
  {
    if (gameGrid.kbhit())
    {
      switch (gameGrid.getKeyCode())
      {
        case KeyEvent.VK_UP:
          setDirection(Location.NORTH);
          break;
        case KeyEvent.VK_LEFT:
          setDirection(Location.WEST);
          break;
        case KeyEvent.VK_RIGHT:
          setDirection(Location.EAST);
          break;
        case KeyEvent.VK_DOWN:
          setDirection(Location.SOUTH);
          break;
        default:
          return;
      }
    }

    int lastIndex = tail.size() - 1;
    Location lastLocation = getLocation();
    if (lastIndex > -1)
    {
      lastLocation = tail.get(lastIndex).getLocation();
      for (int i = lastIndex; i > 0; i--)
        tail.get(i).setLocation(tail.get(i - 1).getLocation());
      tail.get(0).setLocation(getLocation());
    }
    move();
    
    if (!isInGrid())
    {
      gameOver("Border touched");
      return;
    }
    
   // for (TailSegment segment: tail)
    {
      Actor a = gameGrid.getOneActorAt(getLocation(), TailSegment.class);
      if (a != null)
      {
        gameOver("Tail touched");
        return;
      }
    }
    
    tryToEat(lastLocation);
  }

  public void tryToEat(Location lastLocation)
  {
    Actor actor = gameGrid.getOneActorAt(getLocation(), Food.class);
    if (actor != null)
    {
      TailSegment newSegment = new TailSegment();
      gameGrid.addActor(newSegment, lastLocation);
      tail.add(newSegment);
      actor.removeSelf();
      gameGrid.addActor(new Food(), gameGrid.getRandomEmptyLocation());
    }
  }
  
  private void gameOver(String text)
  {
    gameGrid.setTitle(text);
    gameGrid.removeAllActors();
    gameGrid.addActor(new Actor("sprites/gameover.gif"), new Location(10, 8));
    gameGrid.doPause();
  }
}