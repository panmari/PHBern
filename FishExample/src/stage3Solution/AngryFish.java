package stage3Solution;

public class AngryFish extends Fish {

	public AngryFish(FishPond pond, FishTrap trap) {
		super(pond, trap);
		show(1);
	}

	public void tryToEat() {
		//this fish is angry, he eats others.
		if(getNeighbours(matingDistance).size() != 0 
				&& Math.random() < matingProbability
				&& matingCountDown < 0) {
			pond.addActor(new Fish(pond, trap), getLocation());
			matingCountDown = matingExhaustion;
		}
	}
}
