package stage2Collision;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

class Fish extends Actor {

	private FishTrap trap;
	private final int matingDistance = 10;
	private final double matingProbability = 0.5; 
	private final int matingExhaustion = 200;
	int matingCountDown = matingExhaustion;
	private FishPond pond;

	public Fish(FishPond pond, FishTrap trap) {
		super(true, "sprites/fish.gif");
		this.pond = pond;
		this.trap = trap;
	}

	public void act() {
		if (isMoveValid()) {
			tryToAvoidTrap();
			turnRandomly();
		}
		else
			avoidWall();
		move();
		matingCountDown--;
	}
	
	public int collide(Actor a1, Actor a2) {
		if (Math.random() < matingProbability) {
			giveBirth();
		}
		return matingExhaustion;
	}

	private void giveBirth() {
		Fish fish = new Fish(pond, trap);
		fish.addActorCollisionListener(fish);
		fish.addCollisionActors(pond.getActors(Fish.class));
		pond.addActor(fish, getLocation());
	}

	private void tryToMate() {
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