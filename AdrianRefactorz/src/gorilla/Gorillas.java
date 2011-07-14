package gorilla;

//Gorillas.java
import ch.aplu.jgamegrid.*;
import java.util.*;
import java.awt.*;
import javax.swing.JOptionPane;

public class Gorillas extends GameGrid implements GGActorCollisionListener
{
  int speed = 0;
  int degree = 0;

  int wind = 0;
  
  Gorilla leftG = new Gorilla();
  Gorilla rightG = new Gorilla();

  Windsock windsock = new Windsock();

  Banana banana;

  Location leftL = new Location(35,565);
  Location rightL = new Location(1165,565);
  Location bananaRight = new Location(1130, 535);
  Location bananaLeft = new Location(70, 535);

  public boolean leftPlayer;
  boolean gameover = false;

  public Gorillas()
  {
    super(1200, 600, 1, null, "sprites/townBig.jpg", true);
    setSimulationPeriod(50);
    wind = (int)(Math.random()*6);

    addActor(windsock, new Location(600, 40));
    windsock.show(wind);
    if(Math.random() <= 0.5)
    {
      windsock.setHorzMirror(true);
      wind = 0-wind;
    }

    addActor(leftG, leftL);
    addActor(rightG, rightL);
    rightG.setHorzMirror(true);

    if(Math.random() <= 0.5)
    {
      leftPlayer = false;
    }
    else
      leftPlayer = true;

    banana = new Banana(this);
    banana.setHorzMirror(leftPlayer);

    if(!leftPlayer)
    {
      rightG.show(1);
      addActor(banana, bananaRight);
    }
    else
    {
      leftG.show(1);
      addActor(banana, bananaLeft);
    }

    leftG.addCollisionActor(banana);
    rightG.addCollisionActor(banana);

    leftG.setCollisionCircle(new Point(0, 0), 25);
    rightG.setCollisionCircle(new Point(0, 0), 25);

    leftG.addActorCollisionListener(this);
    rightG.addActorCollisionListener(this);

    show();

    degree = requestEntry("Angel:");
    speed = requestEntry("Speed:");

    banana.setDirSpeedWind(degree, speed, wind);

    while(!gameover)
    {
      if(banana.moveOn && !gameover)
      { 
        doRun();
        rightG.show(0);
        leftG.show(0);

        refresh();
      }
      if(!banana.moveOn && !gameover)
      {
        doPause();
        if(leftPlayer)
        {
          leftPlayer = false;
          changePlayer(bananaRight, false);
        }
        else
        {
          leftPlayer = true;
          changePlayer(bananaLeft, true);
        }
      }
    }
  }

  public int collide(Actor g, Actor b)
  {
    doPause();
    g.show(1);
    addActor(new Actor("sprites/gameover.png"), new Location(600, 250));
    gameover = true;
    return 0;
  }

  public void changePlayer(Location loc, boolean leftP)
  {
    banana.setLocation(loc);
    banana.setHorzMirror(leftP);
    banana.show(0);

    if(leftP)
      leftG.show(1);
    else
      rightG.show(1);

    wind = (int)(Math.random()*6);

    windsock.show(wind);
    if(Math.random() <= 0.5)
    {
      windsock.setHorzMirror(true);
      wind = 0-wind;
    }

    refresh();

    degree = requestEntry("Angle:");
    speed = requestEntry("Speed:");

    banana.setDirSpeedWind(degree, speed, wind);

    banana.moveOn = true;
    doRun();

    rightG.show(0);
    leftG.show(0);

    refresh();
  }

  private int requestEntry(String prompt)
  {
    String entry = "";
    while (entry.length() <= 0 && !isNumber(entry))
    {
      entry = JOptionPane.showInputDialog(null, prompt, "", JOptionPane.PLAIN_MESSAGE);
      if (entry == null)
        System.exit(0);
      else
        entry.trim();
    }

    return Integer.parseInt(entry);
  }

  public boolean isNumber(String s)
  {
    boolean nbAndPos = false;
    try
    {
      int i = Integer.parseInt(s);
      if(i < 0)
        nbAndPos = false;
      else
        nbAndPos = true;
    } catch (NumberFormatException ex)
    {
      nbAndPos = false;
    }

    return nbAndPos;
  }

  public static void main(String[] args)
  {
    new Gorillas();
  }
}

//Gorilla
class Gorilla extends Actor
{
  public Gorilla()
  {
    super("sprites/gorilla.png", 2);
  }

  public void act()
  {
    
  }
}

//Windsock
class Windsock extends Actor
{
  public Windsock()
  {
    super("sprites/windsock.png", 6);
  }

  public void act()
  {

  }
}

//Banana
class Banana extends Actor
{
  int degree = 0;
  int end = 0;
  int speed = 0;
  int wind = 0;
  int mult = 0;
  boolean moveOn = false;
  boolean left = true;
  Gorillas gorillas;
  int counter = 0;

  public Banana(Gorillas g)
  {
    super(true, "sprites/banana.png");
    gorillas = g;
  }

  public void setDirSpeedWind(int deg, int s, int w)
  {
    counter = 0;
    left = gorillas.leftPlayer;
    wind = w;
    mult = Math.abs(wind);

    setHorzMirror(left);

    if(left)
      degree = 360-deg;
    else
      degree = 180+deg;

    end = deg;
    speed = (int)s/25;

    if(left)
      degree = degree - (wind*mult);
    else
      degree = degree + (wind*mult);

    if(left && degree < 270)
      left = false;
    else if(!left && degree > 270)
      left = true;

    moveOn = true;
  }

  public void act()
  {
    setDirection(degree);

    if(degree == 270)
    {
      if(getY() <= -20)
        moveOn = false;
    }
    else if(left && degree != 90)
    {
      if(degree < 360)
        degree++;
      else if(degree == 360)
        degree = 0;
    }
    else if(!left && degree != 90)
      degree--;
    else if(degree != 90)
      moveOn = false;

    for (int s = 0; s <= speed && moveOn; s++)
    {
      move();
      if(counter <= 3)
        counter++;
      else
        counter = 0;
      
      show(counter);

      if(getX() <= -20 || getX() >= 1220 || getY() >= 620)
        moveOn = false;
    }
  }
}
