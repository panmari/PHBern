// Alien.java

import ch.aplu.jgamegrid.*;

public class Alien extends Actor
{
  private boolean isLanded = false;
  private int step = 1;

  public Alien()
  {
    super("sprites/alien.gif", 2);
    show(0);
  }

  public void act()
  {
    if (isLanded)
    {
      if (getX() <= 0 || getX() >= 600)
        step = -step;
      setX(getX() + step);
    }
    else
    {  
      setY(getY() + 1);
      if (getY() > 580)
      {
        isLanded = true;
        show(1);
        setActorCollisionEnabled(false);
      }
    }
  }
}

