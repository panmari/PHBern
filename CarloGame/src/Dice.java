// Dice.java

import ch.aplu.jgamegrid.Actor;

public class Dice extends Actor
{
  public Dice()
  {  
    super("sprites/dice.png", 24);
  }
  
  public void act()
  {
    showNextSprite();
  }  
  
}
