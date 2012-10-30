package gui;

import java.awt.Color;
import java.awt.Point;

import models.Triangle;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGPanel;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;

public class VictimTriangle extends AbstractVictim {

	/**
	 * No triangle shall be created without being enemy to the Dalek!
	 * @param exterminate
	 * @param d
	 */
	public VictimTriangle(Exterminate gg, Dalek d) {
		super(gg, d);
	}
	
	public void reset() {
		Location loc = getLocation();
		shape = new Triangle(new GGVector(loc.x, loc.y),
				new GGVector(loc.x + 20, loc.y),
				new GGVector(loc.x, loc.y + 20));
		dalek.addEnemy(shape);
		draw();
	}
	
	public void setLocation(Location loc) {
		GGVector[] vs = ((Triangle) shape).getVertices();
		vs[0] = new GGVector(loc.x, loc.y);
		vs[1] =	new GGVector(loc.x + 20, loc.y);
		vs[2] =	new GGVector(loc.x, loc.y + 20);
	}

	protected void draw() {
		if (isDead)
			panel.setPaintColor(Color.black);
		else panel.setPaintColor(Color.green);
		GGVector[] vs = ((Triangle) shape).getVertices();
		for (int i = 0; i < 3; i++) {
			Point curr = Util.toPoint(vs[i]);
			Point next = Util.toPoint(vs[(i + 1)%3]);
			panel.drawLine(curr.x, curr.y, next.x, next.y);
		}
	}
}
