package ph;
import java.util.LinkedList;


public class CardGrid {
	private LinkedList<TurtleCard> cardSet;
	private TurtleCard[][] grid = new TurtleCard[3][3];
			
	public CardGrid() {
		cardSet = new LinkedList<TurtleCard>();
		TurtleCardFactory tf = TurtleCardFactory.getInstance();
		cardSet.add(tf.makeTurtleCard("yf;gb;rb;bf"));
		cardSet.add(tf.makeTurtleCard("rf;yb;gb;gf"));
		cardSet.add(tf.makeTurtleCard("gf;bb;yb;bf"));
		cardSet.add(tf.makeTurtleCard("bf;yb;rb;gf"));
		cardSet.add(tf.makeTurtleCard("rf;gb;yb;gf"));
		cardSet.add(tf.makeTurtleCard("yf;bb;rb;gf"));
		cardSet.add(tf.makeTurtleCard("rf;bb;yb;gf"));
		cardSet.add(tf.makeTurtleCard("yf;gb;rb;bf"));
		cardSet.add(tf.makeTurtleCard("yf;bb;rb;bf"));
	}
	
	boolean isThereConflict(int newlyAddedx, int newlyAddedy) {
		TurtleCard newCard = grid[newlyAddedx][newlyAddedy];
		for (CardPosition cp: CardPosition.values()) {
			try {
				if (mismatch(newCard, grid[newlyAddedx + cp.x][newlyAddedy + cp.y], cp))
					return true;
			} catch (ArrayIndexOutOfBoundsException e) {
				//it was k, bc there is only border
			} catch (NullPointerException e) {
				//it was k, bc there is only border
			}
		}
		return false;
	}

	/**
	 * This is way too long and needs refactoring
	 * @param newCard
	 * @param cardInDirection
	 * @param cp
	 * @return
	 */
	private boolean mismatch(TurtleCard newCard, TurtleCard cardInDirection, CardPosition cp) {
		return !newCard.getHalfTurtleAt(cp).matches(cardInDirection.getHalfTurtleAt(cp.getOpposite()));
	}

	public void putDownNextCard() {
		TurtleCard nextCard = cardSet.pollFirst();
		//find first empty slot:
		for (int y = 0; y < grid.length; y++) 
			for (int x = 0; x < grid[y].length; x++)
				if (grid[x][y] == null)
					grid[x][y] = nextCard;
	}
}
