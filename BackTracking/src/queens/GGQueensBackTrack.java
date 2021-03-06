package queens;

import java.awt.Color;
import java.util.ArrayList;

import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;

public class GGQueensBackTrack extends GameGrid {
	
	final static int nrQueens = 7; //changes number of queens & size of board!
	QueenActor[] queens = new QueenActor[nrQueens];
	private int nrSteps;
	
	public GGQueensBackTrack() {
		super(nrQueens,nrQueens, 600/nrQueens);
		this.setBgColor(Color.white);
		setTitle("Damenproblem");
		drawPattern();
		this.addStatusBar(25);
		show();
		for (int i = 0; i < queens.length; i++) {
			queens[i] = new QueenActor();
		}
		solveQueens();
	}
	
	public void act() {
		Monitor.wakeUp();
	}
	
	private void drawPattern() {
		GGBackground bg = getBg();
		for (int x = 0; x < nbHorzCells; x++)
			for (int y = 0; y < nbVertCells; y++) {
				if ((x+y) % 2 == 0)
					bg.fillCell(new Location(x, y), new Color(255, 206, 158));
				else bg.fillCell(new Location(x, y), new Color(209, 139, 71));
			}
	}

	public void reset() {
		removeAllActors();
		nrSteps = 0;
		Monitor.wakeUp();
		setStatusText("Situation reset..");
		solveQueens();
	}
	
	private void solveQueens() {
		boolean notSolved = true;
		int nrQueen = 0;
		boolean troubleForNextQueen = false;
		addActor(queens[0], new Location(0, nbVertCells-1));
		while (notSolved) {
			nrSteps++;
			refresh();
			Monitor.putSleep();
			if (isThreatenedByOtherQueen(queens[nrQueen].getLocation()) || troubleForNextQueen)
				if (queens[nrQueen].getY() == 0) {
					queens[nrQueen].removeSelf();
					setStatusText("Tracing steps back..");
					nrQueen--;
					troubleForNextQueen = true;
				}
				else {
					queens[nrQueen].move();
					setStatusText("Moving forward..");
					troubleForNextQueen = false;
				}
			else {
				if(nrQueen == queens.length - 1) { //solved!
					notSolved = false;
					success();
					break;
				} 
				else {
					nrQueen++;
					addActorNoRefresh(queens[nrQueen], new Location(nrQueen, nbVertCells-1));
					setStatusText("Adding next queen..");
				}
			}
		}
	}
	
	private void success() {
		//TODO: add some fancy sprite?
		setStatusText("Found Solution! It took " + nrSteps + " steps.");
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