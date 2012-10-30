package gui;

import java.awt.Color;
import java.awt.Point;

import models.Circle;
import models.Triangle;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGPanel;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;

public class VictimCircle extends AbstractVictim {

	private int radius = 20;
	
	public VictimCircle(Exterminate gg, Dalek d) {
		super(gg, d);
	}
	
	public void reset() {
		this.shape = new Circle(Util.toVector(getLocation()), radius);
		dalek.addEnemy(shape);
		draw();
	}
	
	public void setLocation(Location loc) {
		GGVector c = Util.toVector(loc);
		((Circle) shape).setCenter(c);
	}

	protected void draw() {
		if (isDead)
			panel.setPaintColor(Color.black);
		else panel.setPaintColor(Color.green);
		
		GGVector c = ((Circle) shape).getCenter();
		panel.drawCircle(Util.toPoint(c), radius);
	}
}
