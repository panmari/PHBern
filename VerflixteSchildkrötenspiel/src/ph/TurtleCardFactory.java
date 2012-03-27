package ph;

public class TurtleCardFactory {
	
	private static TurtleCardFactory tf;
	private TurtleCard card;
	private int halfTurtleCount;
	
	public static TurtleCardFactory getInstance() {
		if (tf == null)
			tf = new TurtleCardFactory();
		return tf;
	}
	
	public void prepareEmptyCard() {
		card = new TurtleCard();
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
	 * A string of 4 turtles, separated by semicolon. A valid turtle is ie yf (=yellow front)
	 * or bb (= blue back)
	 * @param tcString
	 * @return
	 */
	public TurtleCard makeTurtleCard(String tcString) {
		prepareEmptyCard();
		for (String tString: tcString.split(";")) {
			Color tColor = Color.getByCharacter(tString.charAt(0));
			Orientation tOrientation = Orientation.getByCharacter(tString.charAt(1));
			addHalfTurtle(tColor, tOrientation);
		}
		return getNewCard();
	}
	
}
