// Reversi.java

import ch.aplu.jgamegrid.*;
import java.util.*;

public class BubbleShooterV2 extends GameGrid implements GGMouseListener
{
  int nbOfBubbleColors = 5;                                          //Zahl zwischen 1 - 5
  Location shooter = new Location(18, 36);                           //Location of the shooter
  ArrayList<Location> bubbleList = new ArrayList<Location>();        //Location-List of shooter and bubbles to come
  ArrayList<Location> similarNeighbours = new ArrayList<Location>(); //Location-List of similar neighbours

  public BubbleShooterV2()
  {
    super(37, 38, 20, false);

    bubbleList.add(shooter);
    for(int x = 27; x < 37; x+=2)
      bubbleList.add(new Location(x, 36));

    //load shooter and future bubble list
    for(Location bubble: bubbleList)
      addRandomBubble2(bubble);

    //load bubbles
    for(int y = 1; y < 6 ; y++)
    {
      for(int x = y; x < 37-y; x+=2)
        addRandomBubble2(new Location(x, 2*y-1));
    }

    addMouseListener(this, GGMouse.lPress);
    show();
  }

  //do if mouse is pressed inside the playground
  public boolean mouseEvent(GGMouse mouse)
  {
    Location location = setValidLocation(toLocationInGrid(mouse.getX(), mouse.getY()));
    //doRun();

    if(getOneActorAt(location) == null && hasNeighbours(location.getNeighbourLocations(2)))
    {
      //location = setValidLocation(location);
      getOneActorAt(shooter).setLocation(location);
      addSimilarNeighbourToList(location, getOneActorAt(location).getIdVisible());
      removeAllSimilarNeighbours();
      resetShooterAndBubbleList();
    }

    endOfGame();

    refresh();


    return true;
  }

  //Add a random Bubble
  public void addRandomBubble2(Location loc)
  {
    int rN = (int)(Math.random()*nbOfBubbleColors);
    addActor(new Bubble(rN), loc);
  }

  //Checks if cell has at least on neighbour in the north, east, south or west
  public boolean hasNeighbours(ArrayList<Location> locNeighbours)
  {
    boolean hasNeighbours = false;

    for (Location l: locNeighbours)
    {
      if(getOneActorAt(l) != null)
      {
        hasNeighbours = true;
        for (Location bubble: bubbleList)
        {
          if(l.equals(bubble))
            hasNeighbours = false;
        }
      }
    }

    return hasNeighbours;
  }

  //changes location of shot bubble, if does not match to grid
  public Location setValidLocation(Location location)
  {
    if(getOneActorAt(new Location(location.x, location.y-2)) != null)
      return new Location(location.x+1, location.y);
    else if(getOneActorAt(new Location(location.x - 1, location.y)) != null)
      return new Location(location.x+1, location.y);
    else if(getOneActorAt(new Location(location.x + 1, location.y)) != null)
      return new Location(location.x-1, location.y);
    else if(getOneActorAt(new Location(location.x, location.y-1)) != null)
      return new Location(location.x+1, location.y+1);
    else if(getOneActorAt(new Location(location.x-1, location.y-1)) != null)
      return new Location(location.x, location.y+1);
    else if(getOneActorAt(new Location(location.x+1, location.y-1)) != null)
      return new Location(location.x, location.y+1);
    else if(getOneActorAt(new Location(location.x-2, location.y-1)) != null)
      return new Location(location.x-1, location.y+1);
    else if(getOneActorAt(new Location(location.x+2, location.y-1)) != null)
      return new Location(location.x+1, location.y+1);
    else
      return location;
  }

  //Adds all neighbors with the same ImageID to the ArrayList similar Neighbours
  public void addSimilarNeighbourToList(Location loc, int imgId)
  {
    similarNeighbours.add(loc);
    ArrayList<Location> neighbours = loc.getNeighbourLocations(2);
    for(Location tempLoc: neighbours)
    {
      boolean isInList = false;
      //Location tempLoc = neighbours.get(n);
      for(int sN = 0; sN < similarNeighbours.size() && !isInList; sN++)
      {
        if(tempLoc.equals(similarNeighbours.get(sN)))
        {
          isInList = true;
        }
      }

      Actor tempActor = getOneActorAt(tempLoc);

      if(tempActor != null)
      {
        if(!isInList && tempActor.getIdVisible() == imgId)
        {
          addSimilarNeighbourToList(tempLoc, imgId);
        }
      }
    }
  }

  //removes all actors of the ArrayList similarNeighbours if the listsize is bigger than 2
  //it also clears the whole ArrayList
  public void removeAllSimilarNeighbours()
  {
    if(similarNeighbours.size() > 2)
    {
      for(int n = 0; n < similarNeighbours.size(); n++)
      {
        this.removeActorsAt(similarNeighbours.get(n));
      }
    }

    similarNeighbours.clear();
  }

  //reloads the shooter and updates the BubbleList
  public void resetShooterAndBubbleList()
  {
    for(int b = 1; b < bubbleList.size(); b++)
      getOneActorAt(bubbleList.get(b)).setLocation(bubbleList.get(b-1));

    addRandomBubble2(bubbleList.get(bubbleList.size()-1));
  }

  //Checks if only shooter and bubble list is left
  public void endOfGame()
  {
    if(getNumberOfActors() <= 6)
      addActor(new Actor("sprites/gameover.png"), new Location(18, 15));
  }

  public static void main(String[] args)
  {
    new BubbleShooterV2();
  }
}
/*------------------------------------------------------------------------------
 * Bubble
 */
class Bubble extends Actor
{
  public Bubble(int imgId)
  {
    super("sprites/peg.png", 6);
    show(imgId);
  }

  public void act()
  {

  }
}