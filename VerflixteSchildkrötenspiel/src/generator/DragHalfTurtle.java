package generator;

import java.awt.Point;

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
		int offset;
		if (isTurtleFront())
			offset = 50;
		else offset = 52;

		switch (dir) {
		case NORTH:
			setLocationOffset(new Point(0, offset));
			setDirection(dir);
			break;
		case EAST:
			setLocationOffset(new Point(offset, 0));
			setDirection(dir);
			break;
		case SOUTH:
			setLocationOffset(new Point(0, -offset));
			setDirection(dir);
			break;
		case WEST:
			setLocationOffset(new Point(-offset, 0));
			setDirection(dir);
			break;
		default:
			throw new RuntimeException();
		}
		
	}
	
	/**
	 * Needs to be inverted in certain cases
	 * TODO: make prettier
	 */
	public void setDirection(CompassDirection dir) {
		if ( (isTurtleFront() && (
				dir == CompassDirection.SOUTH ||
				dir == CompassDirection.NORTH))
				|| 
				(!isTurtleFront() && ( 
				dir == CompassDirection.WEST ||
				dir == CompassDirection.EAST)))
			super.setDirection(dir.getDirection()+180);
		else super.setDirection(dir);
	}
	
	private boolean isTurtleFront() {
		return representation.charAt(1) == 'f';
	}
}
