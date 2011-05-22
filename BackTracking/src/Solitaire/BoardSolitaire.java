package Solitaire;

import ch.aplu.jgamegrid.*;

import java.awt.Component;
import java.awt.Point;
import java.util.*;

public class BoardSolitaire extends GameGrid implements GGMouseTouchListener {
	final int DOWN = 0;
	final int UP = 1;
	Actor draggedMarble;
	ArrayList<Actor> allMarbles = new ArrayList<Actor>();

	private ArrayList<Location> boardPatternLocations = new ArrayList<Location>();
	private Location initialMarbleLocation;

	public BoardSolitaire() {
		super(7, 7, 70, null, "sprites/board.png", false);

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
				disableOtherTouchListeners(touchedMarble);
				//TODO: disable touchListener of all other Marbles
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
				draggedMarble.setLocation(getMouseLocation());
				Actor jumpedMarble = getJumpedMarble(mouseLoc, initialMarbleLocation);
				allMarbles.remove(jumpedMarble);
				removeActor(jumpedMarble);
			}
			else draggedMarble.setLocation(initialMarbleLocation);
			draggedMarble.show(DOWN);
			draggedMarble = null;
			activateAllTouchListeners();
			} catch (NullPointerException e) {
				System.out.println("this shouldn't happen, something lagged behind");
			}
			break;
		}
		refresh(); //this gets called a lot -> laggy?	
	}

	private void disableOtherTouchListeners(Actor touchedMarble) {
		for (Actor marble: allMarbles)
			if (marble != touchedMarble)
				marble.setMouseTouchEnabled(false);
	}
	
	private void activateAllTouchListeners() {
		for (Actor marble: allMarbles)
			marble.setMouseTouchEnabled(true);
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
			allMarbles.add(marble);
			addActor(marble, loc);
		}
		this.removeActorsAt(new Location(3, 3)); //make hole in middle
	}

	/**
	 * check if location is a valid jump location 
	 */
	private boolean isValidJumpLocation(Location loc, Location previousLoc) {
		ArrayList<Location> possibleLocs = new ArrayList<Location>();
		for (int possibleDir = 0; possibleDir < 360; possibleDir += 90)
			possibleLocs.add(previousLoc.getAdjacentLocation(possibleDir, 2));
		return (boardPatternLocations.contains(loc) && possibleLocs.contains(loc)
				&& getActorsAt(loc, Marble.class).size() == 1);
	}

	/**
	 * not in use!
	 * TODO: rewrite later mb?
	 */
	private void isGameOver() {
		ArrayList<Location> leftOvers = getOccupiedLocations();
		// One left => you win
		if (leftOvers.size() == 1) {
			addActor(new Actor("sprites/you_win.gif"), new Location(3, 3));
			restart();
		} else {
			boolean gameover = true;

			// more than one left, but without neighbours => you loose
			for (Location lO : leftOvers) {
				ArrayList<Location> neighbours = lO.getNeighbourLocations(1);
				for (Location n : neighbours) {
					if (getOneActorAt(n) != null)
						gameover = false;
				}
			}

			if (gameover) {
				addActor(new Actor("sprites/gameover.png"), new Location(3, 3));
				restart();
			}
		}
	}

	// restart game
	private void restart() {
		delay(3000);
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