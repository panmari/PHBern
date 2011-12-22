package stage3;

import ch.aplu.jgamegrid.Location;

public class AngryFish extends Fish {

	private final int eatingDistance = 10;
	private final int fullStomach = 5;
	private int eatenFish;
	
	public AngryFish(FishPond pond, FishTrap trap) {
		super(pond, trap);
		show(1); //angry sprite
	}
	
	public void tryToMate() {
		//TODO: this fish is angry, so he has a child once his stomach is full.
	}

	public void tryToEat() {
		//this fish is angry, he eats other fish.
	}
}
