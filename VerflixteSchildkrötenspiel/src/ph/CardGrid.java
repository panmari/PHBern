package ph;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;


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
	
	public boolean isThereConflict(Point newCardPos) {
		TurtleCard newCard = grid[newCardPos.x][newCardPos.y];
		for (CardPosition cp: CardPosition.values()) {
			try {
				int newX = newCardPos.x + cp.x;
				int newY = newCardPos.y + cp.y;
				if (mismatch(newCard, grid[newX][newY], cp))
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
	public boolean mismatch(TurtleCard newCard, TurtleCard cardInDirection, CardPosition cp) {
		return !newCard.getHalfTurtleAt(cp).matches(cardInDirection.getHalfTurtleAt(cp.getOpposite()));
	}

	/**
	 * @param deadCards 
	 * @return a point with the coordinates of the new card or
	 * 			null, if there is no empty slot for another card.
	 */
	public Point putDownNextAliveCard(ArrayList<TurtleCard> deadCards) {
		if (cardSet.isEmpty())
			return null;
		
		TurtleCard nextCard = null;
		for (TurtleCard tc: cardSet) {
			if (!deadCards.contains(tc)) {
				nextCard = tc;
				break;
			}
		}
		if (nextCard == null)
			throw new NoCardAliveException();
		else cardSet.remove(nextCard);
		//find first empty slot:
		for (int y = 0; y < grid.length; y++) 
			for (int x = 0; x < grid[y].length; x++)
				if (grid[x][y] == null) {
					grid[x][y] = nextCard;
					return new Point(x, y);
				}
		return null;
	}

	public boolean rotateCardAt(Point p) {
		return grid[p.x][p.y].rotateCardToRight();
	}

	/**
	 * TODO: Where in cardSet should I put this card?
	 * @param p
	 * @return
	 */
	public Point removeCardAt(Point p) {
		cardSet.addLast(grid[p.x][p.y]);
		grid[p.x][p.y] = null;
		return positionInGridBefore(p);
	}

	private Point positionInGridBefore(Point p) {
		if (p.x == 0 && p.y == 0)
			throw new IllegalArgumentException();
		int x = (p.x + 2) % 3;
		int y = p.y;
		if (p.x == 0) 
			y = p.y - 1;
		return new Point(x, y);
	}
	
	public TurtleCard[][] getGrid() {
		return grid;
	}

	public TurtleCard getCardAt(Point p) {
		return grid[p.x][p.y];
	}
}
