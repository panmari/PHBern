package stage3Solution;

public class AngryFish extends Fish {

	private final int eatingDistance = 10;
	private final int fullStomach = 5;
	private int eatenFish;
	
	public AngryFish(FishPond pond, FishTrap trap) {
		super(pond, trap);
		show(1);
	}

	public void tryToEat() {
		//this fish is angry, he eats others.
		if(getNeighbours(eatingDistance, Fish.class).size() != 0) {
			getNeighbours(eatingDistance, Fish.class).get(0).removeSelf();
			eatenFish++;
			if (eatenFish > fullStomach) {
				pond.addActor(new AngryFish(pond, trap), getLocation());
				eatenFish = 0;
			}
		}
	}
}
