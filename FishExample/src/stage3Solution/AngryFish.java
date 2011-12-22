package stage3Solution;

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
		//this fish is angry, so he has a child once his stomach is full.
		if (eatenFish > fullStomach) {
			Location childFishLoc = new Location(getX() + 15, getY() + 15);
			pond.addActor(new AngryFish(pond, trap), childFishLoc);
			eatenFish = 0;
		}
	}

	public void tryToEat() {
		//this fish is angry, he eats other fish.
		if(getNeighbours(eatingDistance, Fish.class).size() != 0) {
			getNeighbours(eatingDistance, Fish.class).get(0).removeSelf();
			eatenFish++;
		}
	}
}
