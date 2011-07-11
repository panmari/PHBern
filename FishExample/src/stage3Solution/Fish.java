package stage3Solution;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

class Fish extends Actor {

	protected FishTrap trap;
	private final int matingDistance = 10;
	private final double matingProbability = 0.1; 
	private final int matingExhaustion = 200;
	int matingCountDown = matingExhaustion;
	protected FishPond pond;

	public Fish(FishPond pond, FishTrap trap) {
		super(true, "sprites/fish.gif", 2);
		this.pond = pond;
		this.trap = trap;
	}

	public void act() {
		if (isMoveValid()) {
			tryToAvoidTrap();
			turnRandomly();
			tryToMate();
			tryToEat();
		}
		else
			avoidWall();
		move();
		matingCountDown--;
	}

	public void tryToEat() {
		//this fish is calm, he doesn't eat others.
	}

	public void tryToMate() {
		if(getNeighbours(matingDistance).size() != 0 
				&& Math.random() < matingProbability
				&& matingCountDown < 0) {
			pond.addActor(new Fish(pond, trap), getLocation());
			matingCountDown = matingExhaustion;
		}
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