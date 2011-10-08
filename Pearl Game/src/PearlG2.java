// PearlG2.java

import ch.aplu.jgamegrid.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PearlG2 extends GameGrid
  implements GGMouseListener, GGButtonListener
{
  private int nbPearl = 0;
  private int nbTakenPearl;
  private int nbRows = 4;
  private int activeRow;
  private int[] igPearl;
  private final int dualMax = 4;
  private GGBackground bg;
  private GGButton okBtn = new GGButton("sprites/ok.gif", true);
  private GGButton newBtn = new GGButton("sprites/new.gif", true);

  public PearlG2()
  {
    super(8, 6, 60, false);
    setBgColor(new Color (80, 15, 247));
    bg = getBg();
    addMouseListener(this, GGMouse.lClick);
    addActor(okBtn, new Location(6, 4));
    okBtn.addButtonListener(this);
		addActor(newBtn, new Location(6, 4));
    newBtn.addButtonListener(this);
    init();
    show();
  }

  public void init()
  {
    nbTakenPearl = 0;
    int nb = 6;
    bg.clear();
    for (int k = 0; k < nbRows; k++)
    {
      nbPearl = nbPearl + nb;
      for (int i = 0; i < nb; i++)
      {
        Pearl pearl = new Pearl();
        addActor(pearl, new Location(1 + i, 1 + k));
      }
      nb--;
    }
    activeRow = 0;
    okBtn.show();
    newBtn.hide();
    refresh();
    setTitle(nbPearl + " Pearls. Remove any number of pearls from same row and press OK.");
  }

  public boolean mouseEvent(GGMouse mouse)
  {
    Location loc = toLocationInGrid(mouse.getX(), mouse.getY());
    Actor actor = null;
    actor = getOneActorAt(new Location(loc), Pearl.class);
    int y = actor.getY();
    if (actor != null)
    {
      if (activeRow != 0 && activeRow != y)
        setTitle("You must remove pearls from the same row.");
      else
      {
        activeRow = y;
        actor.removeSelf();
        nbPearl--;
        setTitle(nbPearl + " Pearls remaining. Click 'OK' to continue.");
        nbTakenPearl++;
        if (nbPearl == 0)
        {
          setTitle("Press 'new Game' to play again.");
          bg.setPaintColor(Color.red);
          bg.setFont(new Font("Arial", Font.BOLD, 32));
          bg.drawText("You lost!", new Point(toPoint(new Location(2, 5))));
					okBtn.hide();
          newBtn.show();
        }
        refresh();
      }
    }
    return false;
  }

  public void buttonClicked(GGButton button)
  {
    /*if (nbPearl == 0)
        init();
      else
      {
        if (nbTakenPearl == 0)
          setTitle("You have to remove at least 1 Pearl!");
        else
        {
          nbTakenPearl = 0;
          computerMove();
          okBtn.setEnabled(false);
        }
      }*/
      setTitle("Hallo");
  }

	public void buttonPressed(GGButton button)
  {
  }

  public void buttonReleased(GGButton button)
  {
  }

  private void computerMove()
  {
    int row1 = 0;
    int nbToRemoveMatches = 0;
    // if optimal Strategy is not possible, do something random.
    if (!isUSituation(igPearl))
    {
      ArrayList<Actor> matches = getActors(Pearl.class);
      // from a random (not empty!) row
      Collections.shuffle(matches);
      row1 = matches.get(0).getY();
      // take a random amount
      nbToRemoveMatches = (int)((igPearl[row1] - 1) * Math.random() + 1);
    }
    else
    {
      // list for saving all possible solutions
      List<int[]> solutions = new ArrayList<int[]>();
      int[] tgMatches = new int[6];
      // Try all possible situations and add them to "solutions if they're good.
      for (int y = 0; y < 6; y++)
      {
        System.arraycopy(igPearl, 0, tgMatches, 0, 6);
        row1 = y;
        for (int i = 0; i < igPearl[y]; i++)
        {
          tgMatches = makeSituation(tgMatches, y);
          if (isUSituation(tgMatches) == false)
          {
            solutions.add((new int[]
            {
              y, i + 1
            }));
          }
        }
      }
      // choose a random solution
      Collections.shuffle(solutions);
      row1 = solutions.get(0)[0];
      nbToRemoveMatches = solutions.get(0)[1];
      System.out.println("Number of solutions: " + solutions.size());
    }

    nbPearl = nbPearl - nbToRemoveMatches;
    ArrayList<Actor> matches = getActors(Pearl.class);
    Collections.shuffle(matches);
    int x = 0;
    while (nbToRemoveMatches > 0)
    {
      if (matches.get(x).getY() == row1)
      {
        matches.get(x).removeSelf();
        igPearl[row1] = igPearl[row1] - 1;
        nbToRemoveMatches--;
      }
      x++;
    }
    setTitle(nbPearl + " matches remaining. Your move now.");

    if (nbPearl == 0)
    {
      setTitle("Press 'new Game' to play again.");
      bg.setPaintColor(Color.red);
      bg.setFont(new Font("Arial", Font.BOLD, 40));
      bg.drawText("You lost!", new Point(toPoint(new Location(2, 5))));
      okBtn.hide();
      newBtn.show();
    }
    refresh();
    activeRow = 0; // Spieler darf neue "Ziehreihe" bestimmen
  }

   // for debugging only

  private String toString(int[] k)
  {
    String output = "";
    for (int i = 0; i < k.length; i++)
      output = (output + k[i] + ", ");
    return output;
  }
  /*
   * removes a match from a situation given the:
   * "sit", the original situation
   * "row" the row where a match should be removed from
   */

  private int[] makeSituation(int[] sit, int row)
  {
    sit[row] = sit[row] - 1;
    return sit;
  }
  // Check if its a U-Situation

  private Boolean isUSituation(int[] sit)
  {
    int[] allDuals = new int[dualMax];
    int[] oneDual = new int[dualMax];
    for (int y = 0; y < 6; y++)
    {
      oneDual = toDual(sit[y]);
      for (int i = 0; i < allDuals.length; i++)
      {
        allDuals[i] = allDuals[i] + oneDual[i];
      }
    }
    for (int i = 0; i < allDuals.length; i++)
    {
      if (allDuals[i] % 2 == 1)
        return true;
    }
    return false;
  }

  /*
   * gives an integer back in binary (as array of integer) only works for
   * input < 16, or arraylenght "dualMax" must be changed
   */
  private int[] toDual(int input)
  {
    int[] output = new int[dualMax]; // 4 dualstellen
    int x = 0;
    while (input != 0)
    {
      output[x] = input % 2;
      input = input / 2;
      x++;
    }
    return output;
  }

  public static void main(String[] args)
  {
    new PearlG2();
  }

}

class Pearl extends Actor
{
  public Pearl()
  {
    super("sprites/pearl.gif");
  }
}