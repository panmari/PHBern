package Solitaire;

import ch.aplu.jgamegrid.*;

import java.awt.Component;
import java.awt.Point;
import java.util.*;

public class BoardSolitaire extends GameGrid implements GGMouseListener {
	final int DOWN = 0;
	final int UP = 1;
	Actor draggedMarble;

	private ArrayList<Location> boardPatternLocations = new ArrayList<Location>();
	private Location initialMarbleLocation;

	public BoardSolitaire() {
		super(7, 7, 70, null, "sprites/board.png", false);

		loadMarbleLocations();
		loadMarbles();
		addMouseListener(this, GGMouse.lDrag);
		addMouseListener(this, GGMouse.lRelease);
		show();
	}

	public boolean mouseEvent(GGMouse mouse) {
		//is getMouseLocation buggy?
		Location mouseLoc = this.toLocation(mouse.getX(), mouse.getY());
		Actor marble = getOneActorAt(mouseLoc, Marble.class);

		switch (mouse.getEvent()) {
		case GGMouse.lDrag:
			if (draggedMarble == null && marble != null) {
				draggedMarble = marble;
				initialMarbleLocation = marble.getLocation();
				draggedMarble.show(UP);
				draggedMarble.setOnTop();
			}
			if (draggedMarble != null)
				draggedMarble.setPixelLocation(new Point(mouse.getX(), mouse.getY()));
			break;
		case GGMouse.lRelease:
			draggedMarble.setLocationOffset(new Point(0, 0));
			if (isValidJumpLocation(mouseLoc, initialMarbleLocation))
				draggedMarble.setLocation(getMouseLocation());
			else draggedMarble.setLocation(initialMarbleLocation);
			draggedMarble.show(DOWN);
			draggedMarble = null;
			break;
		}
		/*
		 * //if there is a marble if(marble != null &&
		 * isPossibleLocation(location)) { //if no marble has been selected
		 * before, lift it up if(marble.getIdVisible() == DOWN && oneUp == null)
		 * marble.show(UP); //if one marble has been selected before and an
		 * other one is selected, set //the old one down and lift the new one up
		 * else if(oneUp != null) { marble.show(UP); oneUp.show(DOWN); } //if
		 * the selected marble has already been lifted up and selected again,
		 * //set it back down else marble.show(DOWN); } //if selected location
		 * is empty and a marble has been selected before check //if the marble
		 * between can be jumped and removed else if(oneUp != null && marble ==
		 * null && isPossibleLocation(location)) { Location upLoc =
		 * oneUp.getLocation(); Double dir = upLoc.getDirectionTo(location); int
		 * distance = upLoc.getDistanceTo(location);
		 * 
		 * if(distance == 2 || ((dir == 45 || dir == 135 || dir == 225 || dir ==
		 * 315) && distance == 3)) { Actor between =
		 * getOneActorAt(upLoc.getNeighbourLocation(dir));
		 * 
		 * if(between != null) { between.removeSelf();
		 * oneUp.setLocation(location); oneUp.show(DOWN);
		 * 
		 * isGameOver(); } } }
		 */
		refresh();
		return true;
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

	// set marbles on the board
	private void loadMarbles() {
		for (Location loc: boardPatternLocations)
			addActor(new Marble(DOWN), loc);
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

	// get marble which has been selected to be played
	private Actor getOneActorUp() {
		Actor oneUp = null;

		ArrayList<Actor> marbles = getActors(Marble.class);
		for (Actor m : marbles) {
			if (m.getIdVisible() == UP)
				oneUp = m;
		}

		return oneUp;
	}

	// check if game is won or lost
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