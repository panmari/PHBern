package stage2;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

class Fish extends Actor {

	FishTrap trap;

	public Fish(FishTrap trap) {
		super(true, "sprites/fish.gif");
		this.trap = trap;
	}

	public void act() {
		if (isMoveValid()) {
			tryToAvoidTrap();
			turnRandomly();
			tryToMate();
		}
		else
			avoidWall();
		move();
	}

	private void tryToMate() {
		// TODO Teach the fish how to mate!
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