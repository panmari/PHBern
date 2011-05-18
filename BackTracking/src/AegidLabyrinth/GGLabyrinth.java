package AegidLabyrinth;

import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;

import java.awt.Color;
  
public class GGLabyrinth extends GameGrid
{
  private static final int nbHorzCells = 31; // must be odd
  private static final int nbVertCells = 31; // ditto
  private static final int cellSize = 18;

  public GGLabyrinth()
  {
    super(nbHorzCells, nbVertCells, cellSize);
    setSimulationPeriod(250);
    show();
    doRun();
   	initializeMaze();
    setTitle("Recursive Depthsearch");
  }

  private GGMaze drawMaze(GameGrid gg)
  {
    GGBackground bg = getBg();
    GGMaze maze = new GGMaze(nbHorzCells, nbVertCells);
    for (int x = 0; x < nbHorzCells; x++)
      for (int y = 0; y < nbVertCells; y++)
      {
        Location location = new Location(x, y);
        if (maze.isWall(location))
          bg.fillCell(location, Color.black);
        else
          bg.fillCell(location, Color.white);
      }
    return maze;
  }
  
  private void initializeMaze() {
	  GGMaze maze = drawMaze(this);
	  Bug sbug = new Bug(this);
	  addActor(sbug, maze.getStartLocation());
	  sbug.startSearch();
  }
  
  public void reset(){
	  this.removeActors(TextActor.class);
	  //initializeMaze();
	  
	  Monitor.wakeUp();
  }

  public static void main(String[] args)
  {
    new GGLabyrinth();
  }
}
