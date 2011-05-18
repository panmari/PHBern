package queens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import ch.aplu.jgamegrid.*;

public class GGQueensBackTrack extends GameGrid {
	
	final static int nrQueens = 4;
	QueenActor[] queens = new QueenActor[nrQueens];
	
	public GGQueensBackTrack() {
		super(nrQueens,nrQueens, 100, Color.black);
		this.setBgColor(Color.white);
		setTitle("Damenproblem");
		this.addStatusBar(25);
		show();
		for (int i = 0; i < queens.length; i++) {
			queens[i] = new QueenActor(i);
		}
		startAddingQueens();
	}
	
	private void startAddingQueens() {
		addActor(queens[0], new Location(0, nbVertCells-1));
		queenz(0, false);
		refresh();
	}
	
	private void queenz(int nrQueen, boolean troubleForNextQueen) {
		refresh();
		delay(1000);
		if (isThreatenedByOtherQueen(queens[nrQueen].getLocation()) || troubleForNextQueen)
			if (queens[nrQueen].getY() == 0) {
				queens[nrQueen].removeSelf();
				setStatusText("Tracing steps back..");
				queenz(nrQueen-1, true);
			}
			else {
				queens[nrQueen].move();
				setStatusText("Moving forward..");
				queenz(nrQueen, false);
			}
		else {
			if(nrQueen == queens.length - 1) { //solved!
				success();
				return;
			}
			addActor(queens[nrQueen + 1], new Location(nrQueen + 1, nbVertCells-1));
			setStatusText("Added next queen..");
			queenz(nrQueen + 1, false);
		}
	}
	
	private void success() {
		//TODO: add some sprite?
		setStatusText("Found Solution!");
	}

	private boolean isThreatenedByOtherQueen(Location queenLoc) {
		ArrayList<Location> possibleLocations = getDiagonalLocations(queenLoc, true);
		possibleLocations.addAll(getDiagonalLocations(queenLoc, false));
		
		for (int x = 0; x < nbVertCells; x++)
			possibleLocations.add(new Location(x, queenLoc.y));
		
		for (int y = 0; y < nbHorzCells; y++)
			possibleLocations.add(new Location(queenLoc.x, y));
		
		while (possibleLocations.contains(queenLoc))
			possibleLocations.remove(queenLoc);
		
		for (Location loc: possibleLocations) {
			if (getActorsAt(loc, QueenActor.class).size() != 0)
				return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		new GGQueensBackTrack();
	}
}