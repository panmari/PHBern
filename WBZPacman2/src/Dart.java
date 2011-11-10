// Dart.java

import ch.aplu.jgamegrid.*;

public class Dart extends Actor implements GGMouseListener
{
  private Location oldLocation = new Location();

  public Dart()
  {
    super(true, "sprites/dart.gif");  // Rotatable
  }

  public boolean mouseEvent(GGMouse mouse)
  {
    Location location =
      gameGrid.toLocationInGrid(mouse.getX(), mouse.getY());
    setLocation(location);
    double dx = location.x - oldLocation.x;
    double dy = location.y - oldLocation.y;
    if (dx * dx + dy * dy < 25)
      return true;
    double phi = Math.atan2(dy, dx);
    setDirection(Math.toDegrees(phi));
    oldLocation.x = location.x;
    oldLocation.y = location.y;
    return true;
  }
}