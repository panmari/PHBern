package generator;

import gg.CardPosition;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.Location.CompassDirection;

public class DragHalfTurtle extends Actor {
	
	private String representation;
	private CardPosition pos;

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

	public void setLocationWithinCard(Location loc, CardPosition pos) {
		setLocation(loc);
		int offset;
		if (isTurtleFront())
			offset = 50;
		else offset = 53;
		setDirection(pos);
		this.pos = pos;
		switch (pos) {
		case UP:
			setLocationOffset(new Point(0, offset));
			break;
		case RIGHT:
			setLocationOffset(new Point(offset, 0));
			break;
		case DOWN:
			setLocationOffset(new Point(0, -offset));
			break;
		case LEFT:
			setLocationOffset(new Point(-offset, 0));
			break;
		}
		
	}
	
	/**
	 * Needs to be inverted in certain cases
	 * TODO: make prettier
	 */
	public void setDirection(CardPosition pos) {
		if ( (isTurtleFront() && (
				pos == CardPosition.UP ||
				pos == CardPosition.DOWN))
				|| 
				(!isTurtleFront() && ( 
				pos == CardPosition.LEFT ||
				pos == CardPosition.RIGHT)))
			super.setDirection(pos.ordinal()*90);
		else super.setDirection(pos.ordinal()*90 + 180);
	}
	
	private boolean isTurtleFront() {
		return representation.charAt(1) == 'f';
	}

	public CardPosition getPos() {
		return pos;
	}
	
	public String toString() {
		return this.representation;
	}
}
