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
				new GGVector(loc.x + 10, loc.y),
				new GGVector(loc.x, loc.y + 10));
		dalek.addEnemy(triangle);
		draw();
	}
	
	public void act() {
		//lulz, do nothing
	}

	private void draw() {
		panel.setPaintColor(Color.green);
		GGVector[] vs = triangle.getVertices();
		Location loc = getLocation();
		for (int i = 0; i < 3; i++) {
			Point curr = Util.toPoint(vs[i]);
			Point next = Util.toPoint(vs[(i + 1)%3]);
			panel.drawLine(curr.x, curr.y, next.x, next.y);
		}
	}
}
