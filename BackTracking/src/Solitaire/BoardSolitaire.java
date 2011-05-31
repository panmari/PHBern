package Solitaire;

import ch.aplu.jgamegrid.*;

import java.awt.Point;
import java.util.*;

public class BoardSolitaire extends GameGrid implements GGMouseTouchListener {
	final int DOWN = 0;
	final int UP = 1;
	Actor draggedMarble;

	private ArrayList<Location> boardPatternLocations = new ArrayList<Location>();
	private Location initialMarbleLocation;

	public BoardSolitaire() {
		super(7, 7, 70, null, "sprites/board.png", false);
		setTitle("Solitaire");
		loadMarbleLocations();
		loadMarbles();
		show();
		setSimulationPeriod(20);
		doRun();
	}

	public void act() {
		refresh();
	}
	
	public void mouseTouched(Actor touchedMarble, GGMouse mouse, Point spot) {
		Location mouseLoc = toLocation(mouse.getX(), mouse.getY());
		Point mousePoint = new Point(mouse.getX(), mouse.getY());
		switch (mouse.getEvent()) {
		case GGMouse.lPress:
				//define draggedMarble:
			if (draggedMarble == null) {
				draggedMarble = touchedMarble;
				initialMarbleLocation = touchedMarble.getLocation();
				draggedMarble.show(UP);
				draggedMarble.setOnTop();
			} else { //to prevent "hanging" on Linux OS: reset Marble
				draggedMarble.show(DOWN);
				draggedMarble.setLocationOffset(new Point(0, 0));
				draggedMarble.setLocation(initialMarbleLocation);
				draggedMarble = null;
			}
			break;
		case GGMouse.lDrag:
			if (draggedMarble != null)
				draggedMarble.setPixelLocation(mousePoint);
			break;
			
		case GGMouse.lRelease:
			if (draggedMarble != null) {
				draggedMarble.setLocationOffset(new Point(0, 0));
				if (isValidJumpLocation(mouseLoc, initialMarbleLocation, true)
						&& jumpedMarbleExists(mouseLoc, initialMarbleLocation)) {
					draggedMarble.setLocation(mouseLoc);
					Actor jumpedMarble = getJumpedMarble(mouseLoc, initialMarbleLocation);
					removeActor(jumpedMarble);
				}
				else {
					draggedMarble.setLocation(initialMarbleLocation);
				}
				draggedMarble.show(DOWN);
				draggedMarble = null;
				isGameOver();
			}
			break;
		}
	}

	private boolean jumpedMarbleExists(Location loc, Location initialLoc) {
		return getJumpedMarble(loc, initialLoc) != null;
	}
	
	private Actor getJumpedMarble(Location loc, Location initialLoc) {
		Double jumpDirection = loc.getDirectionTo(initialLoc);
		Location overJumpedLoc = loc.getNeighbourLocation(jumpDirection);
		return getOneActorAt(overJumpedLoc, Marble.class);
	}

	/**
	 * Initializes the pattern of the board.
	 * In our case, it always looks like:
	 * xxx000xxx
	 * xxx000xxx
	 * xxx000xxx
	 * 000000000
	 * 000000000
	 * 000000000
	 * xxx000xxx
	 * xxx000xxx
	 * xxx000xxx
	 */
	private void loadMarbleLocations() {
		for (int y = 0; y < 7; y++) {
			if (y < 2 || y > 4) {
				for (int x = 2; x < 5; x++)
					boardPatternLocations.add(new Location(x, y));
			} else {
				for (int x = 0; x < 7; x++)
					boardPatternLocations.add(new Location(x, y));
			}
		}
	}

	/** 
	 * Initializes marbles on the board. It puts a marble
	 * on every location of the boards pattern, except for the middle.
	 */
	private void loadMarbles() {
		for (Location loc: boardPatternLocations) {
			Marble marble = new Marble(DOWN);
			marble.addMouseTouchListener(this, GGMouse.lPress | GGMouse.lDrag | GGMouse.lRelease);
			addActorNoRefresh(marble, loc);
		}
		this.removeActorsAt(new Location(3, 3)); //make hole in middle
		
		
	}

	/**
	 * check if location is a valid jump location 
	 * 	 * 
	 * A location is only a valid jump location if:
	 * 		1. It's part of the boards pattern.
	 * 		2. It's situated orthogonally with a distance of 2 squares
	 * 		3. The dragged Marble is the only marble on this field (= it was empty before)
	 * 
	 * Point 3 changes if you use this method while not dragging a marble! Then, the amount
	 * of marbles on the field has to be 0
	 */
	private boolean isValidJumpLocation(Location loc, Location previousLoc, boolean isDragged) {
		int expectedMarbleNr;
		if (isDragged)
			expectedMarbleNr = 1;
		else expectedMarbleNr = 0;
		ArrayList<Location> validJumpLocs = new ArrayList<Location>();
		for (int possibleDir = 0; possibleDir < 360; possibleDir += 90)
			validJumpLocs.add(previousLoc.getAdjacentLocation(possibleDir, 2));
		return (boardPatternLocations.contains(loc) && validJumpLocs.contains(loc)
				&& getActorsAt(loc, Marble.class).size() == expectedMarbleNr);
	}

	private void isGameOver() {
		ArrayList<Actor> leftMarbles = getActors(Marble.class);
		// One left => you win
		if (leftMarbles.size() == 1) {
			addActor(new Actor("sprites/you_win.gif"), new Location(3, 3));
			restart();
		} else {
			//check if there are any valid moves left
			for (Actor a: leftMarbles) {
				if (hasOrthagonalJumpableNeighbours(a)) 
					return;
			}
			//no more valid jumps possible => you lose!
			addActor(new Actor("sprites/gameover.png"), new Location(3, 3));
			restart();
		}
	}

	private boolean hasOrthagonalJumpableNeighbours(Actor a) {
		Location marbleLoc = a.getLocation();
		for (Location loc: marbleLoc.getNeighbourLocations(0.5)) {
			double locDir = marbleLoc.getDirectionTo(loc);
			Location jumpLoc = loc.getNeighbourLocation(locDir);
			if (getActorsAt(loc, Marble.class).size() != 0 &&
					isValidJumpLocation(jumpLoc, marbleLoc, false))
				return true;
		}
		return false;
	}

	// restart game
	private void restart() {
		delay(5000);
		removeAllActors();
		loadMarbles();
	}

	public static void main(String[] args) {
		new BoardSolitaire();
	}
}

// ------------------------------------------------------------------------------
class Marble extends Actor {
	public Marble(int imgID) {
		super("sprites/marble.png", 2);
		show(imgID);
	}
}