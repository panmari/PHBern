package ph;
import java.util.LinkedList;


public class TurtleCard {
	private Rotation rotation;
	private LinkedList<HalfTurtle> cardSetting;
	
	/**
	 * This constructor shouldn't be called directly. Rather create TurtleCards through 
	 * the TurtleCardFactory.
	 */
	public TurtleCard() {
		cardSetting = new LinkedList<HalfTurtle>();
		rotation = Rotation.LEFT;
	}

	/**
	 * Returns true if it's in the original position again (aka Rotation.LEFT)
	 */
	public boolean rotateCardToRight() {
		HalfTurtle first = cardSetting.pollFirst();
		cardSetting.addLast(first);
		rotation = rotation.getNext();
		return rotation == Rotation.LEFT;
	}

	public void addHalfTurtle(HalfTurtle addedHalfTurtle) {
		cardSetting.add(addedHalfTurtle);
	}
	
	public String toString() {
		return cardSetting.toString();
	}
}
