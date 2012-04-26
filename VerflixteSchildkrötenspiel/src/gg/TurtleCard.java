package gg;
import java.util.LinkedList;

import ch.aplu.jgamegrid.Actor;

/**
 * Representation of a card. Consists of 4 half turtles. 
 * Every Card has an Id, ascending by the time of their creation.
 * The first TurtleCard has the id 1
 * A newly created TurtleCard is oriented to the left side by default.
 */
public class TurtleCard extends Actor implements Cloneable {
	private CardPosition rotation;
	private LinkedList<HalfTurtle> cardSetting;
	private static int idCounter = 0;
	private int id;
	
	/**
	 * This constructor shouldn't be called directly. Rather create TurtleCards through 
	 * the TurtleCardFactory.
	 */
	public TurtleCard(String sprite) {
		super(true, sprite);
		cardSetting = new LinkedList<HalfTurtle>();
		rotation = CardPosition.LEFT;
		id = idCounter++;
	}

	/**
	 * Constructor only used for cloning
	 * @param tc the TurtleCard that is to be cloned
	 */
	private TurtleCard(TurtleCard tc) {
		//TODO: call super Constructor?
		this.cardSetting = tc.cardSetting;
		this.rotation = tc.rotation;
		this.id = tc.id;
	}

	/**
	 * Returns true if it's in the original position again (aka CardPosition.LEFT)
	 */
	public boolean rotateCardClockwise() {
		turn(90);
		HalfTurtle last = cardSetting.pollLast();
		cardSetting.addFirst(last);
		rotation = rotation.getNext();
		return rotation == CardPosition.LEFT;
	}

	public void addHalfTurtle(HalfTurtle addedHalfTurtle) {
		cardSetting.add(addedHalfTurtle);
	}
	
	public String toString() {
		return cardSetting.toString();
	}

	public HalfTurtle getHalfTurtleAt(CardPosition cp) {
		return cardSetting.get(cp.ordinal());
	}

	public int getId() {
		return id;
	}
	
	public CardPosition getRotation() {
		return rotation;
	}
	
	@Override
	public TurtleCard clone() {
		return new TurtleCard(this);
	}
	//TODO: Override equals
}
