package halma;

import ch.aplu.jgamegrid.*;
import java.util.*;
import java.awt.*;


//------------------------------------------------------------------------------
public class Halma extends GameGrid implements GGMouseListener
{
  final int DOWN = 0;
  final int UP = 1;
  final int BLUE = 0;
  final int RED = 1;
  final int GREEN = 2;

  final int restart = 5000;                     //restart game after ... milliseconds

  private int color = (int)(Math.random()*3);   //randomly choose one of the colors to start
  private boolean stillPlaying = false;         //needed if stone can still move after a jump

  private ArrayList<Location> allPossibleLocations = new ArrayList<Location>();

  private ArrayList<Location> redStartLocations = new ArrayList<Location>();
  private ArrayList<Location> blueStartLocations = new ArrayList<Location>();
  private ArrayList<Location> greenStartLocations = new ArrayList<Location>();

  private ArrayList<Location> redEndLocations = new ArrayList<Location>();
  private ArrayList<Location> blueEndLocations = new ArrayList<Location>();
  private ArrayList<Location> greenEndLocations = new ArrayList<Location>();

  //TODO: refactor into 1 marble, make colors as enum
  
  public Halma()
  {
    super(19, 25, 20, null, "sprites/halma.png", false);
    this.setBgColor(Color.WHITE);
    addActor(new Next(), new Location(16, 23));

    //load positions and set up board
    loadAllPossibleLocations();
    setUpBoard();

    addMouseListener(this, GGMouse.lPress);

    //set title
    if(color == BLUE)
      setTitle("BLUE starts");
    else if(color == RED)
      setTitle("RED starts");
    else
      setTitle("GREEN starts");
    show();
  }

  //if mouse is pressed
  public boolean mouseEvent(GGMouse mouse)
  {
    Actor stone = null;
    Location location = toLocationInGrid(mouse.getX(), mouse.getY());

    //only check stones of the playing color
    if(color == BLUE)
      stone = getOneActorAt(location, Blue.class);
    else if(color == GREEN)
      stone = getOneActorAt(location, Green.class);
    else
      stone = getOneActorAt(location, Red.class);

    Actor oneUp = getOneActorUp();

    //check if next-button is pressed
    if((getOneActorAt(location, Next.class) != null) && (oneUp != null))
    {
      oneUp.show(DOWN);
      nextPlayersTurn();
    }


    //if there is a stone
    if(stone != null && !stillPlaying)
    {
      //if no stone has been selected before, lift it up
      if(stone.getIdVisible() == DOWN && oneUp == null)
        stone.show(UP);
      //if one stone has been selected before and an other one is selected, set
      //the old one down and lift the new one up
      else if(oneUp != null)
      {
        stone.show(UP);
        oneUp.show(DOWN);
      }
      //if the selected stone has already been lifted up and selected again,
      //set it back down
      else
        stone.show(DOWN);
    }
    //if selected location is empty and a stone has been selected
    else if(oneUp != null && stone == null && isPossibleLocation(location))
    {
      Location upLoc = oneUp.getLocation();
      int dir = (int)upLoc.getDirectionTo(location);
      boolean hasActorsBetween = true;

      ArrayList<Location> allBetween = getInterjacent(upLoc, location);

      //if there are no stones between, but the stone has already jumped
      if(allBetween.isEmpty() && stillPlaying)
        hasActorsBetween = false;
      //if the direction isn't possible
      else if(dir != 0 && dir != 63 && dir != 116 && dir != 180 && dir != 243 && dir != 296)
        hasActorsBetween = false;
      //if the stone should move horizontally
      else if(dir == 0 || dir == 180)
      {
        //if there is no stone between and the stone has already jumped
        if(allBetween.size() == 1 && stillPlaying)
          hasActorsBetween = false;
        //if the stone hasn't jumped yet
        else
        {
          for(int b = 1; b < allBetween.size(); b+=2)
          {
            //if there are emtpy locations in between
            if(getOneActorAt(allBetween.get(b)) == null)
              hasActorsBetween = false;
          }
        }
      }
      //every other possibility
      else
      {
        for(Location between: allBetween)
        {
          //if there are emtpy locations in between
          if(getOneActorAt(between) == null)
            hasActorsBetween = false;
        }
      }

      //if move is possible
      if(hasActorsBetween)
      {
        stillPlaying = true;
        oneUp.setLocation(location);

        //if the new location of the set stone has no neighbours
        if(!hasNeighbours(location, upLoc))
        {
          oneUp.show(DOWN);
          nextPlayersTurn();
        }

        isGameOver();
      }
    }

    refresh();
    return true;
  }

private void nextPlayersTurn() {
	if(color == BLUE)
      {
        color = RED;
        setTitle("RED plays");
      }
      else if(color == RED)
      {
        color = GREEN;
        setTitle("GREEN plays");
      }
      else
      {
        color = BLUE;
        setTitle("BLUE plays");
      }
      stillPlaying = false;
}

  //load the staring and end locations of the blue player
  private void loadBlueLocations()
  {
    int counter = 6;
    for(int y = 18; y >= 12; y-=2)
    {
      for(int x = (18-y)/2; x <= counter; x+=2)
      {
        blueStartLocations.add(new Location(x, y));
        allPossibleLocations.add(new Location(x, y));
      }
      counter--;
    }

    counter = 6;
    for(int y = 6; y <= 12; y+=2)
    {
      for(int x = y+counter; x <= 12+counter; x+=2)
      {
        blueEndLocations.add(new Location(x, y));
        allPossibleLocations.add(new Location(x, y));
      }
      counter--;
    }
  }

  //load the staring and end locations of the green player
  private void loadGreenLocations()
  {
    int counter = 0;
    for(int y = 18; y >= 12; y-=2)
    {
      for(int x = y+counter; x >= 12+counter; x-=2)
      {
        greenStartLocations.add(new Location(x, y));
        allPossibleLocations.add(new Location(x, y));
      }
      counter++;
    }

    counter = 0;
    for(int y = 6; y <= 12; y+=2)
    {
      for(int x = (18-y)/2; x >= counter; x-=2)
      {
        greenEndLocations.add(new Location(x, y));
        allPossibleLocations.add(new Location(x, y));
      }
      counter++;
    }
  }

  //load the staring and end locations of the red player
  private void loadRedLocations()
  {
    int counter = 0;
    for(int y = 6; y >= 0; y-=2)
    {
      for(int x = y+(counter*3); x <= 12-counter; x+=2)
      {
        //addActor(new Red(0), new Location(x, y));
        redStartLocations.add(new Location(x, y));
        allPossibleLocations.add(new Location(x, y));
      }
      counter++;
    }

    counter = 0;
    for(int y = 18; y <= 24; y+=2)
    {
      for(int x = (y+counter)/3; x <= 12-counter; x+=2)
      {
        //addActor(new Red(0), new Location(x, y));
        redEndLocations.add(new Location(x, y));
        allPossibleLocations.add(new Location(x, y));
      }
      counter++;    
    }
  }

  //load all locations inside the board on which a stone can jump
  private void loadAllPossibleLocations()
  {
    loadBlueLocations();
    loadGreenLocations();
    loadRedLocations();

    int counter = 5;
    int y2 = 12;
    for(int y = y2; y >= 8; y-=2)
    {
      for(int x = counter; x <= 18-counter; x+=2)
      {
        allPossibleLocations.add(new Location(x, y));
        if(y != 12)
          allPossibleLocations.add(new Location(x, y2));
      }
      counter++;
      y2+=2;
    }
  }

  //set up all the stones
  private void setUpBoard()
  {
    for(Location loc: redStartLocations)
      addActor(new Red(0), loc);
    for(Location loc: blueStartLocations)
      addActor(new Blue(0), loc);
    for(Location loc: greenStartLocations)
      addActor(new Green(0), loc);
  }

  //check if location is inside board
  private boolean isPossibleLocation(Location loc)
  {
    return allPossibleLocations.contains(loc) && getOneActorAt(loc) == null;
  }

  //get stone which has been selected to be played
  private Actor getOneActorUp()
  {
    Actor oneUp = null;
    ArrayList<Actor> stones = null;

    if (color == BLUE)
      stones = getActors(Blue.class);
    else if (color == GREEN)
      stones = getActors(Green.class);
    else
      stones = getActors(Red.class);

    for(Actor m: stones)
    {
      if(m.getIdVisible() == UP)
        oneUp = m;
    }

    return oneUp;
  }

  //get all cells between loc1 and loc2
  private ArrayList<Location> getInterjacent(Location loc1, Location loc2)
  {
    ArrayList<Location> list = new ArrayList<Location>();
    if (loc1.x == loc2.x) // Special case: vertical
    {
      for (int y = Math.min(loc1.y, loc2.y) + 1; y < Math.max(loc1.y, loc2.y); y++)
        list.add(new Location(loc1.x, y));
      return list;
    }
    if (loc1.x > loc2.x)   // Exchange
    {
      Location tmp = loc1.clone();
      loc1 = loc2;
      loc2 = tmp;
    }
    for (int x = loc1.x + 1; x < loc2.x; x++)
    {
      double inc = (double)(loc2.y - loc1.y) / (loc2.x - loc1.x);
      double y = loc1.y + (x - loc1.x) * inc;
      final double epsilon = 10E-6;
      if ((y - (int)y) < epsilon)
        list.add(new Location((int)x, (int)y));
    }
    return list;
  }

  //check if game is won or lost depending on each color
  private void isGameOver()
  {    
    if(color == BLUE)
    {
      boolean win = true;
      for(Location bL: blueEndLocations)
      {
        if(getOneActorAt(bL, Blue.class) == null)
          win = false;
      }

      if(win)
      {
        addActor(new Actor("sprites/you_win.gif"), new Location(10,11));
        setTitle("BLUE WINS!!!");
        restart();
      }
    }
    else if(color == GREEN)
    {
      boolean win = true;
      for(Location gL: greenEndLocations)
      {
        if(getOneActorAt(gL, Green.class) == null)
          win = false;
      }

      if(win)
      {
        addActor(new Actor("sprites/you_win.gif"), new Location(10,11));
        setTitle("GREEN WINS!!!");
        restart();
      }
    }
    else
    {
      boolean win = true;
      for(Location rL: redEndLocations)
      {
        if(getOneActorAt(rL, Red.class) == null)
          win = false;
      }

      if(win)
      {
        addActor(new Actor("sprites/you_win.gif"), new Location(10,11));
        setTitle("RED WINS!!!");
        restart();
      }
    }
  }

  //check if the stone has neighbours in each possible direction
  private boolean hasNeighbours(Location loc, Location upLoc)
  {
    if(hasNeighboursInDir(loc, upLoc, 2, 0))
      return true;
    else if(hasNeighboursInDir(loc, upLoc, -2, 0))
      return true;
    else if(hasNeighboursInDir(loc, upLoc, 1, 2))
      return true;
    else if(hasNeighboursInDir(loc, upLoc, 1, -2))
      return true;
    else if(hasNeighboursInDir(loc, upLoc, -1, 2))
      return true;
    else if(hasNeighboursInDir(loc, upLoc, -1, -2))
      return true;
    else return false;
  }

  //check if there are neighbours in given direction
  private boolean hasNeighboursInDir(Location loc, Location upLoc, int addToX, int addToY)
  {
    boolean hasNeighbourInDir = false;
    boolean isEmptyAndPossible = false;
    Location tmpLoc = loc;
    ArrayList <Location> tmpLocList = new ArrayList();

    while(!isEmptyAndPossible && !hasNeighbourInDir)
    {
      tmpLoc = new Location(tmpLoc.getX() + addToX, tmpLoc.getY() + addToY);
      tmpLocList.add(tmpLoc);

      if(getOneActorAt(tmpLoc) == null)
      {
        if(tmpLocList.size() < 2)
          isEmptyAndPossible = true;
        else if(upLoc.equals(tmpLoc))
          isEmptyAndPossible = true;
        else
        {
          boolean isPosLoc = false;
          for(Location posLoc: allPossibleLocations)
          {
            if(posLoc.equals(tmpLoc))
              isPosLoc = true;
          }
          if(isPosLoc)
          {
            isEmptyAndPossible = true;
            hasNeighbourInDir = true;
          }
          else
            isEmptyAndPossible = true;
        }
      }
    }

    return hasNeighbourInDir;
  }
  
  //restart game
  private void restart()
  {
    delay(restart);
    removeAllActors();
    setUpBoard();
  }

  public static void main(String[] args)
  {
    new Halma();
  }
}

class Blue extends Actor
{
  public Blue(int img)
  {
    super("sprites/blue.png", 2);
    show(img);
  }

  public void act()
  {

  }
}

class Green extends Actor
{
  public Green(int img)
  {
    super("sprites/green.png", 2);
    show(img);
  }

  public void act()
  {

  }
}

class Red extends Actor
{
  public Red(int img)
  {
    super("sprites/red.png", 2);
    show(img);
  }

  public void act()
  {

  }
}

class Next extends Actor
{
  public Next()
  {
    super("sprites/next.png");
  }

  public void act()
  {

  }
}