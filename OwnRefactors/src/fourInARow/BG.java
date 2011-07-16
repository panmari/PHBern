package fourInARow;
// BG.java

import ch.aplu.jgamegrid.Actor;

public class BG extends Actor
{
  public BG()
  {
    super(false, "sprites/4inARowBG.png");
  }

  public void reset()
  {
    this.setLocationOffset(new java.awt.Point(0, 4 * 70));
    this.setOnTop();
  }
}
