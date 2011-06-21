package halma;

import ch.aplu.jgamegrid.*;

import java.util.*;
import java.awt.*;

//------------------------------------------------------------------------------
public class Halma extends GameGrid implements GGMouseListener {
	final int restart = 5000; // restart game after ... milliseconds

	private boolean stillPlaying = false; // needed if stone can still move
											// after a jump
	private HalmaStone movingHS;

	//random player starts:
	private int currentPlayer = (int) (Math.random() * 3);
	private HalmaPlayer[] players = new HalmaPlayer[3];
	private ArrayList<Location> allPossibleLocations = new ArrayList<Location>();
	

	public Halma() {
		super(19, 25, 20, null, "sprites/halmaBG.png", false);
		this.setBgColor(Color.WHITE);
		addActor(new Next(), new Location(16, 23));

		// load positions and set up board
		loadAllPossibleLocations();
		setUpBoard();
		addMouseListener(this, GGMouse.lClick);
		// set title
		setTitle(players[currentPlayer] + " STARTS!"); 
		show();
	}

	// if mouse is pressed
	public boolean mouseEvent(GGMouse mouse) {
		Location location = toLocationInGrid(mouse.getX(), mouse.getY());

		// check if next-button is pressed
		if (getOneActorAt(location, Next.class) != null && movingHS != null) {
			movingHS.putDown();
			nextPlayersTurn();
			refresh();
			return true;
		}
		
		HalmaStone stone = getHalmaStoneAt(location);
		// if clicked on a stone:
		if (stone != null) {
			if (stone.isPickedUp())
				stone.putDown();
			else {
				if (movingHS == null) {
					stone.pickUp();
					movingHS = stone;
				} else {
					stone.pickUp();
					movingHS.putDown();
					movingHS = stone;
				}
			}
			refresh();
			return true;
		}
		// if clicked on empty, possible Location with a picked up Stone:
		if (movingHS != null && isPossibleLocation(location)) {
			// if move is possible
			if (hasActorsBetween(movingHS, location)) {
				stillPlaying = true;
				movingHS.setLocation(location);

				// if the new location of the set stone has no neighbours
				if (!hasNeighbours(location, movingHS.getLocation())) {
					movingHS.putDown();
					nextPlayersTurn();
				}
				checkGameOver();
			}
		}
		refresh();
		return true;
	}

	/**
	 * I have no idea how halma works, so i leave this untouched <.<
	 * @param hs
	 * @param loc
	 * @return
	 */
	private boolean hasActorsBetween(HalmaStone hs, Location loc) {
		Location hsLoc = hs.getLocation();
		int dir = (int) hsLoc.getDirectionTo(loc);

		ArrayList<Location> allBetween = getInterjacent(hsLoc, loc);
		// if there are no stones between, but the stone has already
		// jumped
		if (allBetween.isEmpty() && stillPlaying)
			return false;
		// if the direction isn't possible
		else if (dir != 0 && dir != 63 && dir != 116 && dir != 180
				&& dir != 243 && dir != 296)
			return false;
		// if the stone should move horizontally
		else if (dir == 0 || dir == 180) {
			// if there is no stone between and the stone has already
			// jumped
			if (allBetween.size() == 1 && stillPlaying)
				return false;
			// if the stone hasn't jumped yet
			else {
				for (int b = 1; b < allBetween.size(); b += 2) {
					// if there are emtpy locations in between
					if (getOneActorAt(allBetween.get(b)) == null)
						return false;
				}
			}
		}
		// every other possibility
		else {
			for (Location between : allBetween) {
				// if there are emtpy locations in between
				if (getOneActorAt(between) == null)
					return false;
			}
		}
		return true;
	}

	public HalmaStone getHalmaStoneAt(Location location) {
		return (HalmaStone) getOneActorAt(location);
	}

	private void nextPlayersTurn() {
		currentPlayer %= currentPlayer + 1;
		setTitle(players[currentPlayer] + " PLAYS!");
	}

	// load the staring and end locations of the blue player
	private void loadBlueLocations() {
		ArrayList<Location> startLocations = new ArrayList<Location>();
		ArrayList<Location> endLocations = new ArrayList<Location>();
		int counter = 6;
		for (int y = 18; y >= 12; y -= 2) {
			for (int x = (18 - y) / 2; x <= counter; x += 2) {
				startLocations.add(new Location(x, y));
				allPossibleLocations.add(new Location(x, y));
			}
			counter--;
		}
		counter = 6;
		for (int y = 6; y <= 12; y += 2) {
			for (int x = y + counter; x <= 12 + counter; x += 2) {
				endLocations.add(new Location(x, y));
				allPossibleLocations.add(new Location(x, y));
			}
			counter--;
		}
		this.players[0] = new HalmaPlayer(this, HalmaColor.Blue,
				startLocations, endLocations);
	}

	// load the staring and end locations of the green player
	private void loadGreenLocations() {
		ArrayList<Location> startLocations = new ArrayList<Location>();
		ArrayList<Location> endLocations = new ArrayList<Location>();
		int counter = 0;
		for (int y = 18; y >= 12; y -= 2) {
			for (int x = y + counter; x >= 12 + counter; x -= 2) {
				startLocations.add(new Location(x, y));
				allPossibleLocations.add(new Location(x, y));
			}
			counter++;
		}
		counter = 0;
		for (int y = 6; y <= 12; y += 2) {
			for (int x = (18 - y) / 2; x >= counter; x -= 2) {
				endLocations.add(new Location(x, y));
				allPossibleLocations.add(new Location(x, y));
			}
			counter++;
		}
		this.players[1] = new HalmaPlayer(this, HalmaColor.Green,
				startLocations, endLocations);
	}

	// load the staring and end locations of the red player
	private void loadRedLocations() {
		ArrayList<Location> startLocations = new ArrayList<Location>();
		ArrayList<Location> endLocations = new ArrayList<Location>();
		int counter = 0;
		for (int y = 6; y >= 0; y -= 2) {
			for (int x = y + (counter * 3); x <= 12 - counter; x += 2) {
				// addActor(new Red(0), new Location(x, y));
				startLocations.add(new Location(x, y));
				allPossibleLocations.add(new Location(x, y));
			}
			counter++;
		}

		counter = 0;
		for (int y = 18; y <= 24; y += 2) {
			for (int x = (y + counter) / 3; x <= 12 - counter; x += 2) {
				// addActor(new Red(0), new Location(x, y));
				endLocations.add(new Location(x, y));
				allPossibleLocations.add(new Location(x, y));
			}
			counter++;
		}
		this.players[2] = new HalmaPlayer(this, HalmaColor.Red,
				startLocations, endLocations);
	}

	// load all locations inside the board on which a stone can jump
	private void loadAllPossibleLocations() {
		//TODO: does this have to be hardcoded? :-s
		loadBlueLocations();
		loadGreenLocations();
		loadRedLocations();

		int counter = 5;
		int y2 = 12;
		for (int y = y2; y >= 8; y -= 2) {
			for (int x = counter; x <= 18 - counter; x += 2) {
				allPossibleLocations.add(new Location(x, y));
				if (y != 12)
					allPossibleLocations.add(new Location(x, y2));
			}
			counter++;
			y2 += 2;
		}
	}

	// set up all the stones
	private void setUpBoard() {
		for (HalmaPlayer p: players)
			p.initializeStones();
	}

	// check if location is inside board
	private boolean isPossibleLocation(Location loc) {
		return allPossibleLocations.contains(loc) && getOneActorAt(loc) == null;
	}

	// get all cells between loc1 and loc2
	private ArrayList<Location> getInterjacent(Location loc1, Location loc2) {
		ArrayList<Location> list = new ArrayList<Location>();
		if (loc1.x == loc2.x) // Special case: vertical
		{
			for (int y = Math.min(loc1.y, loc2.y) + 1; y < Math.max(loc1.y,
					loc2.y); y++)
				list.add(new Location(loc1.x, y));
			return list;
		}
		if (loc1.x > loc2.x) // Exchange
		{
			Location tmp = loc1.clone();
			loc1 = loc2;
			loc2 = tmp;
		}
		for (int x = loc1.x + 1; x < loc2.x; x++) {
			double inc = (double) (loc2.y - loc1.y) / (loc2.x - loc1.x);
			double y = loc1.y + (x - loc1.x) * inc;
			final double epsilon = 10E-6;
			if ((y - (int) y) < epsilon)
				list.add(new Location((int) x, (int) y));
		}
		return list;
	}

	// check if game is won or lost depending on each color
	private void checkGameOver() {
		if(players[currentPlayer].isWinner()) {
			 addActor(new Actor("sprites/you_win.gif"), new Location(10,11));
		     setTitle(players[currentPlayer] + " WINS!!!");
		        restart();
		}
	}

	// check if the stone has neighbours in each possible direction
	private boolean hasNeighbours(Location loc, Location upLoc) {
		if (hasNeighboursInDir(loc, upLoc, 2, 0))
			return true;
		else if (hasNeighboursInDir(loc, upLoc, -2, 0))
			return true;
		else if (hasNeighboursInDir(loc, upLoc, 1, 2))
			return true;
		else if (hasNeighboursInDir(loc, upLoc, 1, -2))
			return true;
		else if (hasNeighboursInDir(loc, upLoc, -1, 2))
			return true;
		else if (hasNeighboursInDir(loc, upLoc, -1, -2))
			return true;
		else
			return false;
	}

	// check if there are neighbours in given direction
	private boolean hasNeighboursInDir(Location loc, Location upLoc,
			int addToX, int addToY) {
		boolean hasNeighbourInDir = false;
		boolean isEmptyAndPossible = false;
		Location tmpLoc = loc;
		ArrayList<Location> tmpLocList = new ArrayList();

		while (!isEmptyAndPossible && !hasNeighbourInDir) {
			tmpLoc = new Location(tmpLoc.getX() + addToX, tmpLoc.getY()
					+ addToY);
			tmpLocList.add(tmpLoc);

			if (getOneActorAt(tmpLoc) == null) {
				if (tmpLocList.size() < 2)
					isEmptyAndPossible = true;
				else if (upLoc.equals(tmpLoc))
					isEmptyAndPossible = true;
				else {
					boolean isPosLoc = false;
					for (Location posLoc : allPossibleLocations) {
						if (posLoc.equals(tmpLoc))
							isPosLoc = true;
					}
					if (isPosLoc) {
						isEmptyAndPossible = true;
						hasNeighbourInDir = true;
					} else
						isEmptyAndPossible = true;
				}
			}
		}

		return hasNeighbourInDir;
	}

	// restart game
	private void restart() {
		delay(restart);
		removeAllActors();
		setUpBoard();
	}

	public static void main(String[] args) {
		new Halma();
	}
}

class HalmaStone extends Actor {
	private HalmaColor hc;
	private boolean pickedUp;

	public HalmaStone(HalmaColor hc) {
		super("sprites/halmaStone.png", 6);
		show(hc.ordinal() * 2);
		this.hc = hc;
	}

	public void putDown() {
		// careful, could be hazardous, if not pickUp was called before:
		assert pickedUp;
		pickedUp = false;
		this.showPreviousSprite();
	}

	public void pickUp() {
		assert !pickedUp;
		pickedUp = true;
		this.showNextSprite();
	}

	public boolean isPickedUp() {
		return pickedUp;
	}
	public HalmaColor getColor() {
		return hc;
	}
}

class Next extends Actor {
	public Next() {
		super("sprites/next.png");
	}
}