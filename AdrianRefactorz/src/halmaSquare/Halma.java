package halmaSquare;

import ch.aplu.jgamegrid.*;

import java.util.*;
import java.awt.*;

//------------------------------------------------------------------------------
public class Halma extends GameGrid implements GGMouseListener {
	final int restart = 5000; // restart game after ... milliseconds

	private int nbPlayers = 2;
	private boolean jumpModeOn = false; 
	// needed if stone can still move after a jump
	private HalmaStone movingHS;

	//random player starts:
	private int currentPlayer = (int) (Math.random() * nbPlayers);	
	private HalmaPlayer[] players = new HalmaPlayer[nbPlayers];

	public Halma() {
		super(16, 16, 25, Color.BLACK, null, false);
		this.setBgColor(Color.WHITE);
		addActor(new Next(), new Location(16, 23));
		initializePlayers();
		setUpBoard();
		addMouseListener(this, GGMouse.lClick);
		setTitle(players[currentPlayer] + " STARTS!"); 
		show();
	}

	public boolean mouseEvent(GGMouse mouse) {
		Location clickLoc = toLocationInGrid(mouse.getX(), mouse.getY());

		// check if next-button is pressed
		if (getOneActorAt(clickLoc, Next.class) != null && movingHS != null) {
			movingHS.putDown();
			movingHS = null;
			nextPlayersTurn();
			refresh();
			return true;
		}
		
		HalmaStone stone = getHalmaStoneOfCurrentPlayerAt(clickLoc);
		// if clicked on a stone:
		if (stone != null && !jumpModeOn) {
			if (stone == movingHS) {
				stone.putDown();
				movingHS = null;
			} else {
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
		if (movingHS != null && isPossibleLocation(clickLoc)) {
			// if move is possible
			if (hasActorsBetween(movingHS, clickLoc)) {
				Location previousHSLoc = movingHS.getLocation();
				jumpModeOn = true;
				movingHS.setLocation(clickLoc);
				// if the new location of the set stone has no neighbours
				if (!hasNeighbours(clickLoc, previousHSLoc)) {
					movingHS.putDown();
					movingHS = null;
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
		// if there are no stones between, but the stone has already jumped
		if (allBetween.isEmpty() && jumpModeOn)
			return false;
		// if the direction isn't possible
		else if (dir != 0 && dir != 63 && dir != 116 && dir != 180
				&& dir != 243 && dir != 296)
			return false;
		// if the stone should move horizontally
		else if (dir == 0 || dir == 180) {
			// if there is no stone between and the stone has already
			// jumped
			if (allBetween.size() == 1 && jumpModeOn)
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

	public HalmaStone getHalmaStoneOfCurrentPlayerAt(Location location) {
		HalmaStone hs = (HalmaStone) getOneActorAt(location);
		if (hs != null && hs.getOwningPlayer() == players[currentPlayer])
			return hs;
		else return null;
	}

	private void nextPlayersTurn() {
		jumpModeOn = false;
		currentPlayer = (currentPlayer + 1) % players.length;
		setTitle(players[currentPlayer] + " PLAYS!");
	}

	private void initializePlayers() {
		ArrayList<Location>[] startLocations = new ArrayList[nbPlayers];
		
		for (int i = 0; i < nbPlayers; i++)
			startLocations[i] = new ArrayList<Location>();
		
		for (int y = 0; y < 6; y++)
			for (int x = 0; x < 6; x++)
				if (x + y < 6 && !(x == 0 && y == 5) && !(x == 5 && y == 0)) {
					startLocations[0].add(new Location(x, y));
					startLocations[1].add(new Location(nbHorzCells - x - 1, nbHorzCells - y - 1));
				}
		
		this.players[0] = new HalmaPlayer(this, HalmaColor.Blue,
				startLocations[0], startLocations[1]);
		this.players[1] = new HalmaPlayer(this, HalmaColor.Red,
				startLocations[1], startLocations[0]);
	}
	
	/**
	 * Adds all Players stones at the startpositions.
	 */
	private void setUpBoard() {
		for (HalmaPlayer p: players)
			p.initializeStones();
	}

	private boolean isPossibleLocation(Location loc) {
		return getOneActorAt(loc) == null;
	}

	/**
	 *  get all cells between loc1 and loc2
	 *  TODO: Refactor
	 * @param loc1
	 * @param loc2
	 * @return
	 */
	private ArrayList<Location> getInterjacent(Location loc1, Location loc2) {
		ArrayList<Location> list = new ArrayList<Location>();
		if (loc1.x == loc2.x) // Special case: vertical
		{
			for (int y = Math.min(loc1.y, loc2.y) + 1; y < Math.max(loc1.y, loc2.y); y++)
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
		else return false;
	}

	// check if there are neighbours in given direction
	private boolean hasNeighboursInDir(Location loc, Location upLoc,
			int addToX, int addToY) {
		boolean hasNeighbourInDir = false;
		boolean isEmptyAndPossible = false;
		Location tmpLoc = loc;
		ArrayList<Location> tmpLocList = new ArrayList<Location>();

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
					/*
					for (Location posLoc : thi) {
						if (posLoc.equals(tmpLoc))
							isPosLoc = true;
					}
					*/
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

	/**
	 * TODO: Refactor this
	 */
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
	public HalmaPlayer player;
	private HalmaColor hc;
	private boolean pickedUp;

	public HalmaStone(HalmaPlayer player) {
		super("sprites/halmaStone.png", 6);
		show(player.getColor().ordinal() * 2);
		this.player = player;
	}

	public HalmaPlayer getOwningPlayer() {
		return player;
	}

	public void putDown() {
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