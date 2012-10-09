// Clownfish.java

import ch.aplu.jgamegrid.*;

public class Clownfish extends Actor
{
  private CarloFrame cf;
  private int nbTurns = 0;
  
  public Clownfish(CarloFrame cf)
  {
    super("sprites/nemo.gif");
    this.cf = cf;
  }

  public void act()
  {
    move();
    if (getX() == 0 || getX() == 9)
    {
      turn(180);
      setHorzMirror(isHorzMirror() ? false : true);
      nbTurns++;
      cf.setTurns(nbTurns);
    }
  }
} 