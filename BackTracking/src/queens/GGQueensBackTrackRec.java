package queens;

import java.awt.Color;
import java.util.ArrayList;

import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;

public class GGQueensBackTrackRec extends GameGrid {
	
	final static int nrQueens = 7; //changes number of queens & size of board!
	
	public GGQueensBackTrackRec() {
		super(nrQueens,nrQueens, 600/nrQueens);
		this.setBgColor(Color.white);
		setTitle("Damenproblem");
		drawPattern();
		addStatusBar(25);
		show();
		ArrayList<int[][]> solutions = solveQueens(nrQueens, nrQueens);
		setStatusText("Done, found " + solutions.size() + " different solutions. ");
		
		setSimulationPeriod(2000);
		while (true) { //iterate through solutions
			for(int[][] computedSolution: solutions) {
				arrangeQueensOnBoard(computedSolution);
				refresh();
				Monitor.putSleep();
			}
		}
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
		setStatusText("Reset doesn't work, restart the application.");
	}
	
	private ArrayList<int[][]> solveQueens(int row, int column) {
		if (row <= 0) {
			ArrayList<int[][]> zeroCase = new ArrayList<int[][]>();
			zeroCase.add(new int[nrQueens][nrQueens]);
			return zeroCase;
		}
		else return oneMoreQueen(row -1, column, solveQueens(row -1, column));
	}
	
	private ArrayList<int[][]> oneMoreQueen(int newRow, int column, ArrayList<int[][]> partSolution) {
		ArrayList<int[][]> newSolution = new ArrayList<int[][]>();
		for (int[][] solution: partSolution) {
			for (int i = 0; i < column; i++) {
				if (noConflicts(newRow, i, solution)) {
					setStatusText("No conflicts -> save partial solution");
					int[][] solutionClone = cloneArray(solution);
					solutionClone[i][newRow] = 1;
					newSolution.add(solutionClone);
				} else setStatusText("Conflict -> drop partial solution");
				refresh();
				Monitor.putSleep(); //for max speed, comment this out
			}
		}
		return newSolution;
	}

	private boolean noConflicts(int newRow, int i, int[][] solution) {
		arrangeQueensOnBoard(solution);
		addActorNoRefresh(new QueenActor(),new Location(i, newRow));
		return !isThreatenedByOtherQueen(new Location(i, newRow));
	}
	
	private void arrangeQueensOnBoard(int[][] solution) {
		removeAllActors();
		for (int x = 0; x < nrQueens; x++) {
			for (int y = 0; y < nrQueens; y++) {
				if (solution[x][y] == 1)
					addActorNoRefresh(new QueenActor(),new Location(x, y));
			}
		}
	}

	public static int[][] cloneArray(int[][] solution){
		int[][] copy = new int[solution.length][];
		for(int i = 0 ; i < solution.length ; i++){
			System.arraycopy(solution[i], 0, copy[i] = new int[solution[i].length], 0, solution[i].length);
		}
		return copy;
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
		new GGQueensBackTrackRec();
	}
}