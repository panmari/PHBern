package queens;

import ch.aplu.jgamegrid.Actor;

public class QueenActor extends Actor {

	public QueenActor(int i) {
		super(true, "sprites/smallbug.gif");
	}

	public void reset() {
		setY(this.getYStart());
		this.setDirection(270);
	}
}
