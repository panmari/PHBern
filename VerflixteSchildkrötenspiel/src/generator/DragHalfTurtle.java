package generator;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.Location.CompassDirection;

public class DragHalfTurtle extends Actor {
	
	private String representation;

	public DragHalfTurtle(String representation, String sprite) {
		super(sprite);
		this.representation = representation;
	}
	
	private DragHalfTurtle(DragHalfTurtle toClone) {
		super(true, toClone.getScaledImage(1, 0));
		this.representation = toClone.representation;
	}
	
	public DragHalfTurtle clone() {
		return new DragHalfTurtle(this);
	}

	public void setLocationWithinCard(Location loc, CompassDirection dir) {
		setLocation(loc);
		switch (dir) {
		
		}
		
	}
	
}
