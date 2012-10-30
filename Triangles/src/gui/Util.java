package gui;

import java.awt.Point;

import models.IObstacle;
import models.Triangle;

import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;

public class Util {

	public static GGVector toVector(Location l) {
		return new GGVector(l.x, l.y);
	}
	
	public static Point toPoint(GGVector v) {
		return new Point((int)Math.round(v.x), (int)Math.round(v.y));
	}

	public static IObstacle makeRandomTriangle() {
		GGVector[] vertices = new GGVector[3];
		for (int i = 0; i < 3; i++)
			vertices[i] = new GGVector(Math.random()*40, Math.random()*40);
		return new Triangle(vertices[0], vertices[1], vertices[2]);
	}

	public static Location toLocation(GGVector v) {
		return new Location((int)Math.round(v.x), (int)Math.round(v.y));
	}
}
