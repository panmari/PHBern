package gg;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.util.Monitor;


public class CardGrid extends GameGrid {
	private LinkedList<TurtleCard> cardSet;
	private TurtleCard[][] grid = new TurtleCard[3][3];
			
	public CardGrid() {
		super(3, 3, 164, java.awt.Color.GRAY, null, true, 4);
		cardSet = new LinkedList<TurtleCard>();
		TurtleCardFactory tf = TurtleCardFactory.getInstance();
		cardSet.add(tf.makeTurtleCard("yb;rb;yf;bf", "sprites/crd0.gif"));
		cardSet.add(tf.makeTurtleCard("gb;bb;gf;rf", "sprites/crd1.gif"));
		cardSet.add(tf.makeTurtleCard("gb;rb;yf;bf", "sprites/crd2.gif"));
		cardSet.add(tf.makeTurtleCard("gb;rb;yf;bf", "sprites/crd3.gif"));
		cardSet.add(tf.makeTurtleCard("yb;bb;gf;rf", "sprites/crd4.gif"));
		cardSet.add(tf.makeTurtleCard("yb;rb;gf;bf", "sprites/crd5.gif"));
		cardSet.add(tf.makeTurtleCard("bb;rb;gf;yf", "sprites/crd6.gif"));
		cardSet.add(tf.makeTurtleCard("bb;gb;yf;rf", "sprites/crd7.gif"));
		cardSet.add(tf.makeTurtleCard("rb;bb;yf;gf", "sprites/crd8.gif"));
		addStatusBar(25);
		setTitle("Tricky Turtle (www.java-online.ch)");
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
		//it would be enough to only test the card to the left & top
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

	/**
	 * Shows every 5 seconds another solution (given as parameter).
	 * Does this in an endless loop.
	 * @param solutions
	 */
	public void cycleThroughSolutions(Set<SolutionGrid> solutions) {
		//setSimulationPeriod(5000);
		//doRun();
		SolutionGrid[] solutionsArray = solutions.toArray(new SolutionGrid[0]);
		for (int solutionCounter = 0; true; solutionCounter = (solutionCounter + 1) % solutions.size()) {
			removeAllActors();
			setStatusText("Cycling through solutions... Now showing solution #" + (solutionCounter+1));
			grid = solutionsArray[solutionCounter].getGrid();
			for (int x = 0; x < 3; x++)
				for (int y = 0; y < 3; y++)
					addActor(grid[x][y], new Location(x, y));
			refresh();
			Monitor.putSleep();
		}
	}
}
