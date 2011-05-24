package Solitaire;

import ch.aplu.jgamegrid.*;

import java.awt.Component;
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
	}

	public void mouseTouched(Actor touchedMarble, GGMouse mouse, Point spot) {
		
		Location mouseLoc = toLocation(mouse.getX(), mouse.getY());
		Point mousePoint = new Point(mouse.getX(), mouse.getY());
		switch (mouse.getEvent()) {
		case GGMouse.lDrag:
			if (draggedMarble == null) {
				draggedMarble = touchedMarble;
				initialMarbleLocation = touchedMarble.getLocation();
				draggedMarble.show(UP);
				draggedMarble.setOnTop();
			}
			else{
				draggedMarble.setPixelLocation(mousePoint);
			}
			break;
			
		case GGMouse.lRelease:
			try {
				draggedMarble.setLocationOffset(new Point(0, 0));
				if (isValidJumpLocation(mouseLoc, initialMarbleLocation)
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
			} catch (NullPointerException e) {
				System.out.println("this shouldn't happen, something lagged behind");
			}
			break;
		}
		refresh(); //this gets called a lot -> laggy?	
	}

	private boolean jumpedMarbleExists(Location loc, Location initialLoc) {
		return getJumpedMarble(loc, initialLoc) != null;
	}
	
	private Actor getJumpedMarble(Location loc, Location initialLoc) {
		Double dir = loc.getDirectionTo(initialLoc);
		Location overJumpedLoc = loc.getNeighbourLocation(dir);
		return getOneActorAt(overJumpedLoc, Marble.class);
	}

	// Set locations where a marble is set at the start
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
	 * Initializes marbles on the board
	 */
	private void loadMarbles() {
		for (Location loc: boardPatternLocations) {
			Marble marble = new Marble(DOWN);
			marble.addMouseTouchListener(this, GGMouse.lDrag | GGMouse.lRelease);
			addActor(marble, loc);
		}
		this.removeActorsAt(new Location(3, 3)); //make hole in middle
	}

	/**
	 * check if location is a valid jump location 
	 * A location is only a valid jump location if:
	 * 		1. It's part of the boards pattern.
	 * 		2. It's situated orthogonally with a distance of 2 squares
	 * 		3. The dragged Marble is the only marble on this field (= it was empty before)
	 */
	private boolean isValidJumpLocation(Location loc, Location previousLoc) {
		ArrayList<Location> validJumpLocs = new ArrayList<Location>();
		for (int possibleDir = 0; possibleDir < 360; possibleDir += 90)
			validJumpLocs.add(previousLoc.getAdjacentLocation(possibleDir, 2));
		return (boardPatternLocations.contains(loc) && validJumpLocs.contains(loc)
				&& getActorsAt(loc, Marble.class).size() == 1);
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
				if (!a.getNeighbours(1).isEmpty()) 
					return;
			}
			//no more valid jumps possible => you lose!
			addActor(new Actor("sprites/gameover.png"), new Location(3, 3));
			restart();
		}
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