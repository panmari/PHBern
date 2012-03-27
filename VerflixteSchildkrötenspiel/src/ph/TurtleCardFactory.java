package ph;

public class TurtleCardFactory {
	
	TurtleCard card;
	private int halfTurtleCount;
	
	public void makeNewEmptyCard() {
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
	
}
