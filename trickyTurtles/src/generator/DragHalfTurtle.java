package generator;

import gg.CardPosition;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

/**
 * The representation of a HalfTurtle, just for the generator.
 * It shares many attributes with the class HalfTurtle.
 * @author mazzzy
 *
 */
public class DragHalfTurtle extends Actor implements Comparable<DragHalfTurtle>{
	
	private String representation;
	private CardPosition pos;
	private int id;
	private static int counter;

	public DragHalfTurtle(String representation, String sprite) {
		super(sprite);
		this.representation = representation;
		this.setMouseTouchCircle(new Point(0,0), 32);
		this.id = counter++;
	}
	
	private DragHalfTurtle(DragHalfTurtle toClone) {
		super(true, toClone.getScaledImage(1, 0));
		this.representation = toClone.representation;
		this.id = toClone.id;
	}
	
	public DragHalfTurtle clone() {
		return new DragHalfTurtle(this);
	}

	/**
	 * Due to the nature of the sprites, the offset needs to be set in
	 * a quite ugly fashion.
	 */
	public void setLocationWithinCard(Location loc, CardPosition pos) {
		setLocation(loc);
		int offset;
		if (isTurtleFront())
			offset = 51;
		else offset = 50;
		setDirection(pos);
		this.pos = pos;
		switch (pos) {
		case UP:
			setLocationOffset(new Point(0, -offset));
			break;
		case RIGHT:
			setLocationOffset(new Point(offset, 0));
			break;
		case DOWN:
			setLocationOffset(new Point(0, offset));
			break;
		case LEFT:
			setLocationOffset(new Point(-offset, 0));
			break;
		}
		
	}
	
	/**
	 * Sets the sprite rotation according to the given CardPosition
	 * The sprite needs to be inverted in certain cases.
	 */
	public void setDirection(CardPosition pos) {
		if (!isTurtleFront())
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
	
	/**
	 * Assumes that original and counterpart are created right after each other. 
	 * @return id of the matching counterpart.
	 */
	public int getIdOfCounterpart(){
		if (id % 2 == 0)
			return id + 1;
		else return id - 1;
	}

	@Override
	public int compareTo(DragHalfTurtle other) {
		return new Integer(id).compareTo(new Integer(other.id));
	}
}
