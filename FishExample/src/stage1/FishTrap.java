package stage1;

import ch.aplu.jgamegrid.Actor;

public class FishTrap extends Actor {
	public FishTrap() {
		super("sprites/fishTrap.jpg");
	}
	
	public void act() {
		if (getNeighbours(40).size() > 0)
			getNeighbours(40).get(0).removeSelf();
	}
}
