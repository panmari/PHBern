package gg;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.util.Monitor;


public class CardGrid extends GameGrid {
	private List<TurtleCard> cardSet;
	private TurtleCard[][] grid = new TurtleCard[3][3];
	
	public CardGrid(String dataset, boolean shuffle) throws FileNotFoundException {
		super(3, 3, 164, java.awt.Color.GRAY, null, true, 4);
		this.cardSet = new DataSetParser(dataset).parse();
		if (shuffle)
			Collections.shuffle(cardSet);
		addStatusBar(25);
		setTitle("Tricky Turtle (www.java-online.ch)");
		show();
	}
	
	/**
	 * Checks if there is a conflict at the given location p. Only checks for conflicts
	 * in this location, not anywhere else. If the location is empty, it returns true.
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
				//border -> ok
			} catch (NullPointerException e) {
				//there is no card -> ok
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
				if (grid[x][y] != null)
					sb.append(grid[x][y]);
				else sb.append("                ");
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
		System.out.println(this.toIdString());
		System.out.println(this);
	}

	/**
	 * Shows every 5 seconds another solution (given as parameter).
	 * Does this in an endless loop.
	 * @param solutions
	 */
	public void cycleThroughSolutions(Set<SolutionGrid> solutions) {
		addNavigationListener(new SolutionNavigation());
		while (true) {
			int solutionCounter = 1;
			for (SolutionGrid sg: solutions) {
				removeAllActors();
				setStatusText("Cycling through solutions... Now showing solution #" + solutionCounter);
				grid = sg.getGrid();
				for (int x = 0; x < 3; x++) {
					for (int y = 0; y < 3; y++) {
						addActorNoRefresh(grid[x][y], new Location(x, y));
						grid[x][y].show();
					}
				}
				refresh();
				Monitor.putSleep();
				solutionCounter++;
			}
		}
	}
}
