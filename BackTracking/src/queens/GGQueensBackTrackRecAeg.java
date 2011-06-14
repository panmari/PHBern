package queens;

// GGQueensBackTrackRec.java
// Ueberarbeitet AP, 2-Jun-2011

import java.awt.Color;
import java.util.ArrayList;

import ch.aplu.jgamegrid.*;

public class GGQueensBackTrackRecAeg extends GameGrid
{
  private final static int nrQueens = 6; //changes number of queens & size of board!
  private boolean mustWait = true;

  public GGQueensBackTrackRecAeg()
  {
    super(nrQueens, nrQueens, 600 / nrQueens);
    this.setBgColor(Color.white);
    setTitle("Damenproblem");
    drawPattern();
    addStatusBar(25);
    addResetListener(
      new GGResetListener()
      {
        public boolean resetted()
        {
          return true;  // Consume reset event->reset disabled
        }
      });
    show();
    setStatusText("Press 'Run' or 'Step' to play.");
    while (true)
    {
      setSimulationPeriod(300);
      doWait();
      ArrayList<int[][]> solutions = solveQueens(nrQueens, nrQueens);
      setSimulationPeriod(2000);
      setStatusText("Done. " + solutions.size() 
        + " solutions. Press 'Run' or 'Step' to show them.");
      removeAllActors();
      refresh();
      doPause();
      mustWait = true;
      doWait();

      //iterate through solutions
      int nb = 1;
      for (int[][] computedSolution : solutions)
      {
        setStatusText("Solution #" + nb++ + " of " + solutions.size()); 
        arrangeQueensOnBoard(computedSolution);
        doWait();
      }
      setStatusText("Done. Press 'Run' or 'Step' to play again.");
      doPause();
      doWait();
    }
  }

  private void doWait()
  {
    while (mustWait)
      delay(1);
    mustWait = true;
  }

  public void act()
  {
    mustWait = false;
  }

  private void drawPattern()
  {
    GGBackground bg = getBg();
    for (int x = 0; x < nbHorzCells; x++)
      for (int y = 0; y < nbVertCells; y++)
      {
        if ((x + y) % 2 == 0)
          bg.fillCell(new Location(x, y), new Color(142, 72, 47));
        else
          bg.fillCell(new Location(x, y), new Color(255, 230, 145));
      }
  }

  private ArrayList<int[][]> solveQueens(int row, int column)
  {
    if (row <= 0)
    {
      ArrayList<int[][]> zeroCase = new ArrayList<int[][]>();
      zeroCase.add(new int[nrQueens][nrQueens]);
      return zeroCase;
    }
    else
      return oneMoreQueen(row - 1, column, solveQueens(row - 1, column));
  }

  private ArrayList<int[][]> oneMoreQueen(int newRow, int column, ArrayList<int[][]> partSolution)
  {
    ArrayList<int[][]> newSolution = new ArrayList<int[][]>();
    for (int[][] solution : partSolution)
    {
      for (int i = 0; i < column; i++)
      {
        if (noConflicts(newRow, i, solution))
        {
          setStatusText("No conflicts -> save partial solution");
          int[][] solutionClone = cloneArray(solution);
          solutionClone[i][newRow] = 1;
          newSolution.add(solutionClone);
        }
        else
          setStatusText("Conflict -> drop partial solution");
        doWait();
      }
    }
    return newSolution;
  }

  private boolean noConflicts(int newRow, int i, int[][] solution)
  {
    arrangeQueensOnBoard(solution);
    addActor(new Actor("sprites/Dame.png"), new Location(i, newRow));
    return !isThreatenedByOtherQueen(new Location(i, newRow));
  }

  private void arrangeQueensOnBoard(int[][] solution)
  {
    removeAllActors();
    for (int x = 0; x < nrQueens; x++)
    {
      for (int y = 0; y < nrQueens; y++)
      {
        if (solution[x][y] == 1)
          addActor(new Actor("sprites/Dame.png"), new Location(x, y));
      }
    }
  }

  public static int[][] cloneArray(int[][] solution)
  {
    int[][] copy = new int[solution.length][];
    for (int i = 0; i < solution.length; i++)
    {
      System.arraycopy(solution[i], 0, copy[i] = new int[solution[i].length], 0, solution[i].length);
    }
    return copy;
  }

  private boolean isThreatenedByOtherQueen(Location queenLoc)
  {
    ArrayList<Location> possibleLocations = getDiagonalLocations(queenLoc, true);
    possibleLocations.addAll(getDiagonalLocations(queenLoc, false));

    for (int x = 0; x < nbVertCells; x++)
      possibleLocations.add(new Location(x, queenLoc.y));

    for (int y = 0; y < nbHorzCells; y++)
      possibleLocations.add(new Location(queenLoc.x, y));

    while (possibleLocations.contains(queenLoc))
      possibleLocations.remove(queenLoc);

    for (Location loc : possibleLocations)
    {
      if (getActorsAt(loc, Actor.class).size() != 0)
        return true;
    }
    return false;
  }

  public static void main(String[] args)
  {
    new GGQueensBackTrackRecAeg();
  }
}