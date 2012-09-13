package nogg;
import gg.CardPosition;
import gg.HalfTurtle;

import java.util.LinkedList;

/**
 * Representation of a card. Consists of 4 half turtles. 
 * Every Card has an Id, ascending by the time of their creation.
 * The first TurtleCard has the id 1
 * @author panmari
 *
 */
public class TurtleCard {
	private CardPosition rotation;
	private LinkedList<HalfTurtle> cardSetting;
	private static int idCounter = 0;
	private int id;
	
	/**
	 * This constructor shouldn't be called directly. Rather create TurtleCards through 
	 * the TurtleCardFactory.
	 */
	public TurtleCard(String sprite) {
		cardSetting = new LinkedList<HalfTurtle>();
		rotation = CardPosition.LEFT;
		id = idCounter++;
	}

	/**
	 * Returns true if it's in the original position again (aka Rotation.LEFT)
	 */
	public boolean rotateCardClockwise() {
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
}
