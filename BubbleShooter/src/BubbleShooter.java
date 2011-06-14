// Reversi.java

import ch.aplu.jgamegrid.*;
import java.util.*;
import java.awt.*;

public class BubbleShooter extends GameGrid implements GGMouseListener
{
  int imageID = 0;
  Bubble bubble;
  Location shooter = new Location(10, 20);
  ArrayList<Actor> sameNeighboursV = new ArrayList<Actor>();


  public BubbleShooter()
  {
    super(21, 21, 40, Color.cyan, false);
    imageID = (int)(Math.random()*6);
    bubble = new Bubble(imageID);

    addActor(bubble, shooter);

    for(int b = 20; b > 16; b--)
      addActor(new Bubble((int)(Math.random()*6)), new Location(b, 20));

    for(int y = 0; y < 5 ; y++)
    {
      for(int x = y; x < 21-y; x++)
        addActor(new Bubble((int)(Math.random()*6)), new Location(x, y));
    }

    addMouseListener(this, GGMouse.lPress);
    show();
  }

  public boolean mouseEvent(GGMouse mouse)
  {
    Location location = toLocationInGrid(mouse.getX(), mouse.getY());

    if(getOneActorAt(location) == null && hasNeighbours(location.getNeighbourLocations(1)))
    {
      bubble.setLocation(location);

      System.out.println("Location: " + location + " ImageID: " + imageID);

      ArrayList<Actor> sameNeighbours = getSameNeighboursV(bubble.getNeighbours(1), imageID);
      //getSameNeighboursV(bubble.getNeighbours(1), imageID);

      if(sameNeighbours.size() > 1)
      {
        for(int n = sameNeighbours.size()-1; n >= 0; n--)
          sameNeighbours.get(n).removeSelf();

        bubble.removeSelf();
        bubble = null;
      }

      reload();

      refresh();
    }

    return true;
  }

  //Checks if cell has at least on neighbour in the north, east, south or west
  public boolean hasNeighbours(ArrayList<Location> locNeighbours)
  {
    boolean hasNeighbours = false;

    for (Location l: locNeighbours)
    {
      if(getOneActorAt(l) != null)
        hasNeighbours = true;
    }

    return hasNeighbours;
  }

  //Return ArrayList of neighbours with the same ImageID
  public ArrayList<Actor> getSameNeighbours(ArrayList<Actor> neighbours, int imgID)
  {
    ArrayList<Actor> sameNeighbours = new ArrayList<Actor>();

    for(int n = neighbours.size()-1; n >= 0; n--)
    {
      Actor nb = neighbours.get(n);
      System.out.println(nb.getLocation());

      if(nb.getIdVisible() == imgID)
      {
        sameNeighbours = getSameNeighbours(nb.getNeighbours(1) ,imgID);
        sameNeighbours.add(nb);
      }
    }
    return sameNeighbours;
  }

  public ArrayList<Actor> getSameNeighboursV(ArrayList<Actor> neighbours, int imgID)
  {
    ArrayList<Actor> sameNeighboursTemp = new ArrayList<Actor>();

    for(int n = neighbours.size()-1; n >= 0; n--)
    {
      Actor nb = neighbours.get(n);
      
      if(nb.getIdVisible() == imgID)
      {
        System.out.println("Loc: " + nb.getLocation() + " ID: " + imgID);
        sameNeighboursTemp = getSameNeighboursV(nb.getNeighbours(1) ,imgID);
        for(int t = sameNeighboursTemp.size()-1; t >= 0; t--)
        {
          boolean add = true;
          for(int sN = sameNeighboursV.size()-1; sN >= 0; sN--)
          {
            if(sameNeighboursV.get(sN).getLocation() == sameNeighboursTemp.get(t).getLocation() || sameNeighboursV.get(sN).getLocation() == bubble.getLocation())
            {
              add = false;
              break;
            }
          }
          if(add)
            sameNeighboursV.add(sameNeighboursTemp.get(t));
          else
            add = true;
        }
      }
    }
    return sameNeighboursV;
  }

  /*public ArrayList<Actor> getSameNeighboursRec(Location location, int imgID, double direction)
  {
    ArrayList<Actor> sameNeighbours = new ArrayList<Actor>();

    for(int dir = 0; dir <= 315; dir += 45)
    {
      if(dir != direction-180)
      {
        Location loc = location.getNeighbourLocation(dir);
        Actor a = getOneActorAt(loc);
        if(a != null && a.getIdVisible() == imgID)
        {
          System.out.println(a.getLocation());
          sameNeighbours = getSameNeighboursRec(loc, imgID, dir);
          sameNeighbours.add(a);
        }
      }
    }

    return sameNeighbours;
  }*/

  //Reload shooter and list of future bubbles
  public void reload()
  {
    for(int x = 17; x < 21; x++)
    {
      int imgID = getOneActorAt(new Location(x, 20)).getIdVisible();

      if(x == 17)
      {
        imageID = imgID;
        bubble = new Bubble(imgID);
        addActor(bubble, shooter);
      }
      else if(x == 20)
      {
        getOneActorAt(new Location(x-1, 20)).show(imgID);
        getOneActorAt(new Location(x, 20)).show((int)(Math.random()*6));
      }
      else
        getOneActorAt(new Location(x-1, 20)).show(imgID);
    }
    sameNeighboursV.clear();
  }

  public static void main(String[] args)
  {
    new BubbleShooter();
  }
}
/*------------------------------------------------------------------------------
 * class Bubble
 */
class Bubble2 extends Actor
{
  public Bubble2(int imageID)
  {
    super("sprites/peg.png", 6);
    show(imageID);
  }

  public void act()
  {
      
  }
}