package gui;

import java.awt.Color;
import java.awt.Point;

import models.Rectangle;
import models.Triangle;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGPanel;
import ch.aplu.jgamegrid.GGRectangle;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;

public class VictimRectangle extends AbstractVictim {

	/**
	 * No triangle shall be created without being enemy to the Dalek!
	 * @param exterminate
	 * @param d
	 */
	public VictimRectangle(Exterminate gg, Dalek d) {
		super(gg, d);
	}
	
	public void reset() {
		Location loc = getLocation();
		shape = new Rectangle(new GGRectangle(Util.toVector(loc), 0, 10, 10));
		dalek.addEnemy(shape);
		draw();
	}
	
	public void setLocation(Location loc) {
		((Rectangle) shape).setVertices(new GGRectangle(Util.toVector(loc), 0, 10, 10));
	}

	protected void draw() {
		if (isDead)
			panel.setPaintColor(Color.black);
		else panel.setPaintColor(Color.green);
		GGVector[] vs = ((Rectangle) shape).getVertices();
		for (int i = 0; i < 4; i++) {
			Point curr = Util.toPoint(vs[i]);
			Point next = Util.toPoint(vs[(i + 1) % 4]);
			panel.drawLine(curr.x, curr.y, next.x, next.y);
		}
	}
}
