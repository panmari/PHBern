package ph;
import java.util.Arrays;
import java.util.LinkedList;


public class TurtleCard {
	private Rotation rotation;
	private LinkedList<HalfTurtle> cardSetting;
	
	/**
	 * In the order they appear on the card. First HalfTurtle is on the left side.
	 * The Array must consist of four HalfTurtles!
	 * @param fourHalfTurtles
	 */
	public TurtleCard(HalfTurtle[] fourHalfTurtles) {
		if (fourHalfTurtles.length != 4)
			throw new IllegalArgumentException();
		//TODO: check if there are any null objects in array
		this.cardSetting = new LinkedList<HalfTurtle>(Arrays.asList(fourHalfTurtles));
		this.rotation = Rotation.LEFT;
	}
	
	public TurtleCard() {
		cardSetting = new LinkedList<HalfTurtle>();
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
}
