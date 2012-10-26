package gui;

import java.awt.Color;
import java.awt.Point;

import models.Triangle;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGPanel;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;

public class VictimTriangle extends Actor {
	
	private Triangle triangle;
	private GGPanel panel;
	private Dalek dalek;
	private boolean isDead = false;

	/**
	 * No triangle shall be created without being enemy to the Dalek!
	 * @param exterminate
	 * @param d
	 */
	public VictimTriangle(Exterminate exterminate, Dalek d) {
		super();
		this.panel = exterminate.getPanel();
		this.dalek = d;
	}
	
	public void reset() {
		Location loc = getLocation();
		triangle = new Triangle(new GGVector(loc.x, loc.y),
				new GGVector(loc.x + 20, loc.y),
				new GGVector(loc.x, loc.y + 20));
		dalek.addEnemy(triangle);
		draw();
	}
	
	public void act() {
		if (triangle.liesInside(dalek.getStandPoint())) {
			isDead = true;
			dalek.removeEnemy(triangle);
		}
		draw();
	}
	
	public void setLocation(Location loc) {
		GGVector[] vs = triangle.getVertices();
		vs[0] = new GGVector(loc.x, loc.y);
		vs[1] =	new GGVector(loc.x + 20, loc.y);
		vs[2] =	new GGVector(loc.x, loc.y + 20);
	}

	private void draw() {
		if (isDead)
			panel.setPaintColor(Color.black);
		else panel.setPaintColor(Color.green);
		GGVector[] vs = triangle.getVertices();
		for (int i = 0; i < 3; i++) {
			Point curr = Util.toPoint(vs[i]);
			Point next = Util.toPoint(vs[(i + 1)%3]);
			panel.drawLine(curr.x, curr.y, next.x, next.y);
		}
	}
}
