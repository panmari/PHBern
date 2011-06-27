package stage1.copy;

import ch.aplu.jgamegrid.Actor;

public class FishTrap extends Actor {
	
	int trapRadius = 40;
	
	public FishTrap() {
		super("sprites/fishTrapRound.png");
	}
	
	public void act() {
		if (isAnyFishCloseby())
			getNeighbours(trapRadius).get(0).removeSelf();
	}
	
	private boolean isAnyFishCloseby() {
		return getNeighbours(trapRadius).size() > 0;
	}
	
	public int getTrapRadius() {
		return this.trapRadius;
	}
}
