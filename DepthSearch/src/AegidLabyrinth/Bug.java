package AegidLabyrinth;
import ch.aplu.jgamegrid.*;
import ch.aplu.jgamegrid.Location;

import java.util.*;
import java.awt.*;
import ch.aplu.util.*;

class Bug extends Actor
{
  private final Location startLocation;
  private final Location exitLocation;
  private ArrayList<Location> visitedLocations;
  private Location previousLoc;
  private GameGrid gg;

  public Bug(GameGrid gg, Location startLoc, Location exitLoc)
  {
    super(true, "sprites/smallbug.gif"); // Rotatable
    this.startLocation = startLoc;
    this.exitLocation = exitLoc;
    previousLoc = startLocation;
    visitedLocations = new ArrayList<Location>();
    this.gg = gg;
  }

  public void startSearch()
  {
    searchPath(startLocation, 0);
  }

  public void act()
  {
    Monitor.wakeUp();
  }

  private void searchPath(Location loc, int dist)
  {
    Monitor.putSleep();
    gg.setPaintOrder(Bug.class, TextActor.class);
    if (visitedLocations.contains(exitLocation)
      || visitedLocations.contains(loc))
      return;
    else
    {
      visitedLocations.add(loc);
      setLocationFacing(loc);
      if (loc.equals(exitLocation))
        return;
      else
      {
        // Aktuelle Zelle markieren und beschriften
        TextActor distMark = new TextActor("" + dist);
        distMark.setLocationOffset(new Point(-7, 0));
        gg.addActor(distMark, loc);

        // Naechste Zelle bestimmen (rekursiv)
        if (canMove(new Location(loc.x, loc.y - 1))) // up
          searchPath(new Location(loc.x, loc.y - 1), dist + 1);
        if (canMove(new Location(loc.x - 1, loc.y))) // left
          searchPath(new Location(loc.x - 1, loc.y), dist + 1);
        if (canMove(new Location(loc.x, loc.y + 1))) // down
          searchPath(new Location(loc.x, loc.y + 1), dist + 1);
        if (canMove(new Location(loc.x + 1, loc.y))) // right
          searchPath(new Location(loc.x + 1, loc.y), dist + 1);

        // Falls das Ziel auf diesem Weg nicht erreicht, rotes X
        if (!visitedLocations.contains(exitLocation))
        {
          gg.removeActorsAt(loc, TextActor.class); //delete number
          TextActor wrongMark = new TextActor("x", Color.red,
            Color.white, new Font("SansSerif", Font.PLAIN, 12));
          distMark.setLocationOffset(new Point(-8, 0));
          gg.addActor(wrongMark, loc);
          setLocationFacing(loc);
        }
      }
    }
  }

  private void setLocationFacing(Location loc)
  {
    setDirection(previousLoc.getCompassDirectionTo(loc));
    previousLoc = loc;
    setLocation(loc);
  }

  private boolean canMove(Location location)
  {
    Color c = getBackground().getColor(location);
    return (!c.equals(Color.black));
  }
}