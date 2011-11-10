// LightsOut.java

import ch.aplu.jgamegrid.*;
import java.awt.*;

public class LightsOut extends GameGrid implements GGMouseListener
{

  public LightsOut()
  {
    super(5, 5, 50, Color.black, false);
    setTitle("LightsOut");
    for (int i = 0; i < 5; i++)
    {
      for (int k = 0; k < 5; k++)
      {
        Actor actor = new Actor("sprites/lightout.gif", 2);
        addActor(actor, new Location(i, k));
        actor.show(1);
      }
    }
    addMouseListener(this, GGMouse.lPress);
    show();
	refresh();
  }

  public boolean mouseEvent(GGMouse mouse)
  {
    Location loc = toLocationInGrid(mouse.getX(), mouse.getY());
    Location[] locs  = new Location[5];
    locs[0] = new Location(loc.x, loc.y);
    locs[1] = new Location(loc.x, loc.y - 1);
    locs[2] = new Location(loc.x, loc.y + 1);
    locs[3] = new Location(loc.x - 1, loc.y);
    locs[4] = new Location(loc.x + 1, loc.y);

    for (int i = 0; i < 5; i++)
    {
       Actor a = getOneActorAt(locs[i]);
       if (a != null)
          a.showNextSprite();
    }
    refresh();
    return true;
  }

  public static void main(String[] args)
  {
    new LightsOut();
  }
}
