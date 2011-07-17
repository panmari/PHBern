package gorillaStef;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class Banana extends Actor {
	
	private GorillaMain gg;
	private double vx, vy, x, y;
	private final double speedFactor = 0.1;
	
	public Banana(GorillaMain gg, double vx, double vy) {
		super(true, "sprites/banana_0.png");
		this.gg = gg;
		this.vx = vx;
		this.vy = vy;
	}
	
	public void reset() {
		x = getX();
		y = getY();
	}
	
	/**
	 * Banana collides with Gorilla -> current Player wins!
	 */
	public int collide(Actor a1, Actor a2) {
		gg.gorillaWasHit();
		setActEnabled(false);
		return 0;
	}
	
	public void act() {
		vy = vy + 9.81*speedFactor;
		vx = vx + gg.getWindSock().getWind()*speedFactor;
		x = x + vx*speedFactor;
		y = y + vy*speedFactor;
		gg.getBg().drawPoint(new Point((int) x, (int) y));
		setLocation(new Location((int) x, (int) y));
		//that doesn't make much sense physically but looks cool:
		turn(Math.sqrt(vx*vx + vy*vy)*speedFactor*2);
		if (getY() > gg.getNbVertCells())
			removeSelf();
	}
}
