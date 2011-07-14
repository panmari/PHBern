package gorillaStef;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class Banana extends Actor {
	
	private double vx, vy, x, y;
	private final double speedFactor = 0.1;
	
	public Banana(double vx, double vy) {
		super(true, "sprites/banana_0.png");
		this.vx = vx;
		this.vy = vy;
	}
	
	public void reset() {
		x = getX();
		y = getY();
	}
	public void act() {
		vy = vy + 9.81*speedFactor;
		x = x + vx*speedFactor;
		y = y + vy*speedFactor;
		//just for pretty:
		gameGrid.getBg().drawPoint(new Point((int) x, (int) y));
		setLocation(new Location((int) x, (int) y));
		turn((int) Math.sqrt(vx*vx + vy*vy)*speedFactor*2);
	}
}
