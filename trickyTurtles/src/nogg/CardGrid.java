package nogg;
import gg.CardPosition;
import gg.HalfTurtle;

import java.util.LinkedList;
import ch.aplu.jgamegrid.Location;
import ch.aplu.util.Monitor;


public class CardGrid {
	private LinkedList<TurtleCard> cardSet;
	private TurtleCard[][] grid = new TurtleCard[3][3];
			
	public CardGrid() {
		cardSet = new LinkedList<TurtleCard>();
		TurtleCardFactory tf = TurtleCardFactory.getInstance();
		cardSet.add(tf.makeTurtleCard("yf;gb;rb;bf", "sprites/tc1.jpg"));
		cardSet.add(tf.makeTurtleCard("rf;yb;gb;bf", "sprites/tc2.jpg"));
		cardSet.add(tf.makeTurtleCard("gf;rb;yb;bf", "sprites/tc3.jpg"));
		cardSet.add(tf.makeTurtleCard("bf;yb;rb;gf", "sprites/tc4.jpg"));
		cardSet.add(tf.makeTurtleCard("rf;gb;yb;gf", "sprites/tc5.jpg"));
		cardSet.add(tf.makeTurtleCard("yf;bb;rb;gf", "sprites/tc6.jpg"));
		cardSet.add(tf.makeTurtleCard("rf;bb;yb;gf", "sprites/tc7.jpg"));
		cardSet.add(tf.makeTurtleCard("yf;gb;rb;bf", "sprites/tc8.jpg"));
		cardSet.add(tf.makeTurtleCard("yf;bb;rb;bf", "sprites/tc9.jpg"));
	}
	
	/**
	 * TODO: make a null turtle to avert ugly exception-cases?
	 * Assumes that TurtleCards are added from top left to bottom right. 
	 * @param p, the location of the newly added TurtleCard
	 * @return
	 */
	public boolean isThereConflict(Location p) {
		TurtleCard newCard = grid[p.x][p.y];
		for (CardPosition cp: CardPosition.values()) {
			try {
				int newX = p.x + cp.x;
				int newY = p.y + cp.y;
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
	 * @param newCard
	 * @param cardInDirection
	 * @param cp
	 * @return
	 */
	private boolean mismatch(TurtleCard newCard, TurtleCard cardInDirection, CardPosition cp) {
		HalfTurtle turtle = newCard.getHalfTurtleAt(cp);
		HalfTurtle otherTurtle = cardInDirection.getHalfTurtleAt(cp.getOpposite());
		return !turtle.matches(otherTurtle);
	}
	
	public Location putDownCard(TurtleCard nextCard) {
		cardSet.remove(nextCard);
		//find first empty slot:
		for (int y = 0; y < grid.length; y++) 
			for (int x = 0; x < grid[y].length; x++)
				if (grid[x][y] == null) {
					grid[x][y] = nextCard;
					return new Location(x, y);
				}
		return null;
	}

	public boolean rotateCardAt(Location p) {
		boolean originalPos = getCardAt(p).rotateCardClockwise();
		return originalPos;
	}
	
	public TurtleCard[][] getGrid() {
		return grid;
	}

	public TurtleCard getCardAt(Location p) {
		return grid[p.x][p.y];
	}

	public boolean isSolved() {
		return grid[2][2] != null && !isThereConflict(new Location(2, 2));
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
					return;
				}
	}
	
	public void act() {
		Monitor.wakeUp();
	}
}
