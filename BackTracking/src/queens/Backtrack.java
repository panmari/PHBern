package queens;
// Backtrack3.java

import ch.aplu.util.*;
import ch.aplu.turtle.*;
import java.awt.Color;

// ------------------------- Class Queen -------------------------
class Queen extends Turtle
{
  static Turtle _marker = new Turtle();
  static int id = 0;

  Queen(int id)
  {
    super(_marker);
    _marker.hideTurtle();
    setPos(id*50, -50);
    speed(50);
    switch( id )
    {
      case 0:
        setColor(Color.red);
        break;
      case 1:
        setColor(Color.green);
        break;
      case 2:
        setColor(Color.blue);
        break;
      case 3:
        setColor(Color.yellow);
        break;
    }
    id++;
  }

  void restart()
  {
    if ( getY() > -50 )
    {
      status("Must backtrack!");
      penErase();
      back(getY() + 50);
      setPenColor(Color.blue);
    }
  }

  static void drawBoard()
  {
    _marker.setPenColor(Color.black);
    for (int i = 0; i < 5; i++)
    {
      _marker.setX(-25 + i * 50);
      _marker.setY(-25);
      _marker.forward(200);
    }

    _marker.right(90);

    for (int i = 0; i < 5; i++)
    {
      _marker.setY(-25 + i * 50);
      _marker.setX(-25);
      _marker.forward(200);
    }

    _marker.setPos(-100, -100);
  }


  static void status(String text)
  {
    _marker.setPenColor(Color.black);
    _marker.label(text);
    GWindow.delay(1000);
    _marker.setPenColor(Color.white);
    _marker.label(text);
  }
}

// ------------------------- Application class -------------------------
class Backtrack3
{
  int[] v = new int[4];
  Queen[] q = new Queen[4];

  Backtrack3()
  {
    initQueens();
    if ( playGame() )
      Queen.status("Success!");
  }

  void initQueens()
  {
    Queen.drawBoard();
    for ( int k = 0; k < 4; k++ )
    {
      v[k] = -1;
      q[k] = new Queen(k);
    }
  }

  boolean playGame()
  {
    while (v[0] < 3)
    {
      restart_123();
      move(0);
      while (v[1] < 3)
      {
        restart_23();
        move(1);
        if (isSafe(1))
        {
          while (v[2] < 3)
          {
            restart_3();
            move(2);
            if (isSafe(2))
            {
              while (v[3] < 3)
              {
                move(3);
                if (isSafe(3))
                  return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  boolean isSafe(int z)
  {
    boolean safe = true;

    // Some queen in same horizontal line
    for (int k = 0; k < 4 && k != z && v[k] != -1; k++)
      if (v[k] == v[z])
        safe = false;

    // Some queen in same diagonal line
    for (int k = 0; k < 4 && k != z && v[k] != -1; k++)
      if (Math.abs(v[k] - v[z]) == Math.abs(k - z))
        safe = false;

    if ( safe )
      Queen.status("Safe? -- Yes!");
    else
      Queen.status("Safe?--No!");

    return safe;
  }

  void move(int k)
  {
    if ( v[k] == -1 )
      q[k].setY(0);
    else
      q[k].forward(50);

    v[k]++;
  }

  void restart_3()
  {
    v[3] = -1;
    q[3].restart();
  }

  void restart_23()
  {
    v[2] = v[3] = -1;
    q[3].restart();
    q[2].restart();
  }

  void restart_123()
  {
    v[1] = v[2] = v[3] = -1;
    q[3].restart();
    q[2].restart();
    q[1].restart();
  }

  public static void main(String[] args)
  {
    new Backtrack3();
  }
}