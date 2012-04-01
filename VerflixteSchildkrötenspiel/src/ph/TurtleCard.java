package ph;
import java.util.LinkedList;


public class TurtleCard {
	private CardPosition rotation;
	private LinkedList<HalfTurtle> cardSetting;
	
	/**
	 * This constructor shouldn't be called directly. Rather create TurtleCards through 
	 * the TurtleCardFactory.
	 */
	public TurtleCard() {
		cardSetting = new LinkedList<HalfTurtle>();
		rotation = CardPosition.LEFT;
	}

	/**
	 * Returns true if it's in the original position again (aka Rotation.LEFT)
	 */
	public boolean rotateCardToRight() {
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
}
