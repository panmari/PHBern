package gg;

/**
 * A Factory utility used for making TurtleCards. Supports two ways of making cards:
 * <ul>
 * <li> By giving a string of a whole card </li>
 * <li> By piecing the card together using addHalfTurtle() </li>
 * </ul>
 * It is recommended to use the first method for creating a full set of cards (takes
 * much less typing).
 * This utility uses a singleton pattern, so at any time there is only
 * one instance of it.
 * @see DataSetParser
 */
public class TurtleCardFactory {
	
	private static TurtleCardFactory tf;
	private TurtleCard card;
	private int halfTurtleCount;
	
	public static TurtleCardFactory getInstance() {
		if (tf == null)
			tf = new TurtleCardFactory();
		return tf;
	}
	
	public void prepareEmptyCard(String sprite) {
		card = new TurtleCard(sprite);
		halfTurtleCount = 0;
	}
	
	/**
	 * must be called 4 times to make a valid card
	 * @param color
	 * @param orientation
	 */
	public void addHalfTurtle(Color color, Orientation orientation) {
		card.addHalfTurtle(new HalfTurtle(color, orientation));
		halfTurtleCount++;
	}
	
	private boolean isCardReady() {
		return halfTurtleCount == 4;
	}
	
	public TurtleCard getNewCard() {
		if (isCardReady())
			return card;
		else
			throw new CardNotReadyException("Not enough HalfTurtles on card!");
	}
	
	/**
	 * Takes as paramater a string of 4 turtles, separated by semicolon. 
	 * A valid turtle is eg yf (=yellow front)
	 * or bb (= blue back)
	 * @param tcString the card as string
	 * @param sprite the file path to the sprite of the card
	 * @return
	 */
	public TurtleCard makeTurtleCard(String tcString, String sprite) {
		prepareEmptyCard(sprite);
		for (String tString: tcString.split(";")) {
			Color tColor = Color.getByCharacter(tString.charAt(0));
			Orientation tOrientation = Orientation.getByCharacter(tString.charAt(1));
			addHalfTurtle(tColor, tOrientation);
		}
		return getNewCard();
	}
	
}
