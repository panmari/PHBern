package stage1Solution;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

class Fish extends Actor {

	private FishTrap trap;
	private FishPond pond;

	public Fish(FishPond pond, FishTrap trap) {
		super(true, "sprites/fish.gif");
		this.trap = trap;
		this.pond = pond;
	}

	public void act() {
		if (isMoveValid()) {
			tryToAvoidTrap();
			turnRandomly();
		}
		else
			avoidWall();
		move();
	}

	private void tryToAvoidTrap() {
		Location trapLoc = trap.getLocation();
		Location fishLoc = getLocation();
		if (fishLoc.getDistanceTo(trapLoc) < 50) {
			System.out.println("panic!");
			turn(180);
		}
	}

	private void avoidWall() {
		turn(180);
	}

	private void turnRandomly() {
		double angle = (Math.random() * 20) - 10;
		turn(angle);
	}
}