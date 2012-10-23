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

	/**
	 * No triangle shall be created without being enemy to the Dalek!
	 * @param panel
	 * @param d
	 */
	public VictimTriangle(GGPanel panel, Dalek d) {
		super();
		this.panel = panel;
		this.triangle = Util.makeRandomTriangle();
		d.addEnemy(triangle);
	}
	
	public void reset() {
		draw();
	}

	private void draw() {
		panel.setPaintColor(Color.green);
		GGVector[] vs = triangle.getVertices();
		Location loc = getLocation();
		for (int i = 0; i < 3; i++) {
			Point curr = Util.toPoint(vs[i]);
			Point next = Util.toPoint(vs[(i + 1)%3]);
			panel.drawLine(curr.x + loc.x, curr.y + loc.y, next.x + loc.x, next.y + loc.y );
		}
	}
}
