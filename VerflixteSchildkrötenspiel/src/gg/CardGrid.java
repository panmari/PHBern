package gg;
import java.util.Arrays;
import java.util.LinkedList;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.util.Monitor;


public class CardGrid extends GameGrid {
	private LinkedList<TurtleCard> cardSet;
	private TurtleCard[][] grid = new TurtleCard[3][3];
			
	public CardGrid() {
		super(3, 3, 165, java.awt.Color.RED, null, true, 4);
		cardSet = new LinkedList<TurtleCard>();
		TurtleCardFactory tf = TurtleCardFactory.getInstance();
		cardSet.add(tf.makeTurtleCard("yb;rb;yf;bf", "sprites/karte1.gif"));
		cardSet.add(tf.makeTurtleCard("gb;bb;gf;rf", "sprites/karte2.gif"));
		cardSet.add(tf.makeTurtleCard("gb;rb;yf;bf", "sprites/karte3.gif"));
		cardSet.add(tf.makeTurtleCard("gb;rb;yf;bf", "sprites/karte4.gif"));
		cardSet.add(tf.makeTurtleCard("yb;bb;gf;rf", "sprites/karte5.gif"));
		cardSet.add(tf.makeTurtleCard("yb;rb;gf;bf", "sprites/karte6.gif"));
		cardSet.add(tf.makeTurtleCard("bb;rb;gf;yf", "sprites/karte7.gif"));
		cardSet.add(tf.makeTurtleCard("bb;gb;yf;rf", "sprites/karte8.gif"));
		cardSet.add(tf.makeTurtleCard("rb;bb;yf;gf", "sprites/karte9.gif"));
		addStatusBar(20);
		show();
	}
	
	/**
	 * Checks if there is a conflict at the given location p. Only checks for conflicts
	 * in this location, not anywhere else. If the location is empty, it returns true.
	 * TODO: make a null turtle for exception-cases?
	 * @param p
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
					addActor(nextCard, new Location(x,y));
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
	
	public TurtleCard[][] getCopyOfGrid() {
		TurtleCard[][] gridCopy = new TurtleCard[grid.length][grid[0].length];
		for (int i = 0; i < gridCopy.length; i++)
			gridCopy[i] = Arrays.copyOf(grid[i], grid[i].length);
		return gridCopy;
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
	
	public String toIdString() {
		return SolutionConsole.toIdString(grid);
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
					return;
				}
	}
	
	public void act() {
		Monitor.wakeUp();
	}
	
	public void reset() {
		setStatusText("The reset button doesn't work, restart the application");
	}

	public void showSolution() {
		doPause();
		refresh();
		setStatusText("Found Solution! Click once more on run to look for more.");
		System.out.println(this.toIdString());
	}
}
