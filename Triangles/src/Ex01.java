// Ex01.java

import java.awt.Point;

import models.Triangle;
import models.ViewingCone;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class Ex01 extends GameGrid implements GGMouseListener
{
  private final GGVector center = new GGVector(300, 300);
  private final GGVector dir = new GGVector(500, 300);
  private final double angle = Math.PI / 4;
  private final ViewingCone cone = new ViewingCone(center, dir, angle, true);
  private GGBackground bg;
  private int nbClicks;
  private GGVector[] vertices = new GGVector[3];

  public Ex01()
  {
    super(600, 600, 1, false);
    setSimulationPeriod(50);
    show();
    doRun();
    bg = getBg();
    init();
    addMouseListener(this, GGMouse.lPress);
  }
  
  private void init()
  {
    nbClicks = 0;
    bg.clear();
    drawCone();
  }
  
  private void drawCone()
  {
    drawLine(center, dir);
    GGVector dirLine = dir.sub(center);
    GGVector border1 = dirLine.clone();
    GGVector border2 = dirLine.clone();
    border1.rotate(angle / 2);
    border2.rotate(-angle / 2);
    drawLine(center, center.add(border1));
    drawLine(center, center.add(border2));
  }
  
  private void drawLine(GGVector v1, GGVector v2)
  {
    bg.drawLine(toPoint(v1), toPoint(v2));
  }

  public boolean mouseEvent(GGMouse mouse)
  {
    if (nbClicks == 3)
      init();
    Location location =
      toLocationInGrid(mouse.getX(), mouse.getY());
    vertices[nbClicks] = new GGVector(location.x, location.y);
    bg.drawCircle(toPoint(vertices[nbClicks]), 3);
    nbClicks++;
    if (nbClicks == 3)
    {
      Triangle t = new Triangle(vertices[0], vertices[1], vertices[2]);
      drawTriangle(t);
      System.out.println(t);
      cone.addObstacle(t);
      double r = cone.getDistanceToClosestObstacle();
      bg.drawCircle(new Point(300, 300), (int)r);
      System.out.println("Closest distance:  " + r);
      cone.removeObstacle(t);
    }
    return true;
  }

  private Point toPoint(GGVector v)
  {
    return new Point((int)v.x, (int)v.y);
  }

  private void drawTriangle(Triangle t)
  {
    drawLine(t.getVertices()[0], t.getVertices()[1]);
    drawLine(t.getVertices()[1], t.getVertices()[2]);
    drawLine(t.getVertices()[2], t.getVertices()[0]);
  }

  public static void main(String[] args)
  {
    new Ex01();
  }

}