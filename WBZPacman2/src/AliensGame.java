// AliensGame.java

import ch.aplu.jgamegrid.*;
import java.awt.*;

public class AliensGame extends GameGrid implements GGActorCollisionListener
{
  private int nbAliens = 20;
  
  public AliensGame()
  {
    super(600, 600, 1, null, "sprites/town.jpg", false);
    setTitle("Move dart with mouse to pick the aliens");
    setSimulationPeriod(50);
    //playSound(this, GGSound.DUMMY);
    getBg().clear(new Color(182, 220, 250));

    Dart dart = new Dart();
    addActor(dart, new Location(100, 300));
    addMouseListener(dart, GGMouse.lDrag);
    dart.setCollisionSpot(new Point(30, 0)); // Endpoint of dart needle

    for (int i = 0; i < nbAliens; i++)
    {
      Actor alien = new Alien();
      addActor(alien, new Location((int)(560 * Math.random() + 20),
                                   (int)(400 * Math.random() + 40)));
      dart.addCollisionActor(alien);
      dart.addActorCollisionListener(this);
    }
    show();
    doRun();
  }

  public int collide(Actor actor1, Actor actor2)
  {
    actor2.removeSelf(); // actor2 is an alien
    nbAliens--;
    setTitle(nbAliens + " aliens remaining.");
    //playSound(this, GGSound.PING);
    if (nbAliens == 0)
      setTitle(nbAliens + " aliens remaining. You won!");
    refresh();
    return 0;
  }

  public static void main(String[] args)
  {
    new AliensGame();
  }
}
