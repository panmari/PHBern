package queens;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class QueenActor extends Actor {

	public QueenActor() {
		super("sprites/Dame.png");
	}

	public void reset() {
		this.setDirection(Location.NORTH);
	}
}
