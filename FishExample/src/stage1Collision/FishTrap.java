package stage1Collision;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;

public class FishTrap extends Actor {
	
	public FishTrap() {
		super("sprites/fishTrapRound.png");
		setCollisionCircle(new Point(0,0), 40);
	}
	
	public void act() {
	}

	@Override
	public int collide(Actor a1, Actor a2) {
		a2.removeSelf();
		return 1;
	}
}
