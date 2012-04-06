package ph;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.util.Monitor;


public class CardGrid extends GameGrid {
	private LinkedList<TurtleCard> cardSet;
	private TurtleCard[][] grid = new TurtleCard[3][3];
			
	public CardGrid() {
		super(3, 3, 200);
		cardSet = new LinkedList<TurtleCard>();
		TurtleCardFactory tf = TurtleCardFactory.getInstance();
		cardSet.add(tf.makeTurtleCard("yf;gb;rb;bf", "sprites/tc1.jpg"));
		cardSet.add(tf.makeTurtleCard("rf;yb;gb;bf", "sprites/tc2.jpg"));
		cardSet.add(tf.makeTurtleCard("gf;bb;yb;bf", "sprites/tc3.jpg"));
		cardSet.add(tf.makeTurtleCard("bf;yb;rb;gf", "sprites/tc4.jpg"));
		cardSet.add(tf.makeTurtleCard("rf;gb;yb;gf", "sprites/tc5.jpg"));
		cardSet.add(tf.makeTurtleCard("yf;bb;rb;gf", "sprites/tc6.jpg"));
		cardSet.add(tf.makeTurtleCard("rf;bb;yb;gf", "sprites/tc7.jpg"));
		cardSet.add(tf.makeTurtleCard("yf;gb;rb;bf", "sprites/tc8.jpg"));
		cardSet.add(tf.makeTurtleCard("yf;bb;rb;bf", "sprites/tc9.jpg"));
		show();
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
				//it was k, bc there is no card
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
		HalfTurtle turtle = newCard.getHalfTurtleAt(cp);
		HalfTurtle otherTurtle = cardInDirection.getHalfTurtleAt(cp.getOpposite());
		return !turtle.matches(otherTurtle);
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
					addActor(nextCard, new Location(x,y));
					return new Point(x, y);
				}
		return null;
	}
	
	public Point putDownCard(TurtleCard nextCard) {
		cardSet.remove(nextCard);
		//find first empty slot:
		for (int y = 0; y < grid.length; y++) 
			for (int x = 0; x < grid[y].length; x++)
				if (grid[x][y] == null) {
					grid[x][y] = nextCard;
					addActor(nextCard, new Location(x,y));
					return new Point(x, y);
				}
		return null;
	}

	public boolean rotateCardAt(Point p) {
		boolean originalPos = grid[p.x][p.y].rotateCardClockwise();
		refresh();
		return originalPos;
	}

	/**
	 * TODO: Where in cardSet should I put this card?
	 * @param p
	 * @return
	 */
	public Point removeCardAt(Point p) {
		cardSet.addLast(grid[p.x][p.y]);
		removeActorsAt(new Location(p.x, p.y));
		refresh();
		grid[p.x][p.y] = null;
		return positionInGridBefore(p);
	}

	private Point positionInGridBefore(Point p) {
		if (p.x == 0 && p.y == 0)
			return new Point(0, 0);
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

	public boolean isSolved() {
		return grid[2][2] != null && !isThereConflict(new Point(2, 2));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[y].length; x++) {
				sb.append(" | ");
				sb.append(grid[x][y]);
			}
			sb.append(" | ");
			sb.append("\n");
		}
		return sb.toString();
	}

	public LinkedList<TurtleCard> getCards() {
		return new LinkedList<TurtleCard>(cardSet);
	}

	public void removeLastCard() {
		for (int y = grid.length - 1; y >= 0; y--) 
			for (int x = grid[y].length - 1; x >= 0; x--)
				if (grid[x][y] != null) {
					cardSet.add(grid[x][y]);
					grid[x][y] = null;
					removeActorsAt(new Location(x,y));
					refresh();
					return;
				}
	}
	
	public void act() {
		Monitor.wakeUp();
	}
}
