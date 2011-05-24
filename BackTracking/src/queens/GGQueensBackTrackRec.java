package queens;

import java.awt.Color;
import java.util.ArrayList;

import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;

public class GGQueensBackTrackRec extends GameGrid {
	
	final static int nrQueens = 7; //changes number of queens & size of board!
	private int nrSteps;
	
	public GGQueensBackTrackRec() {
		super(nrQueens,nrQueens, 600/nrQueens);
		this.setBgColor(Color.white);
		setTitle("Damenproblem");
		drawPattern();
		this.addStatusBar(25);
		show();
		System.out.println(solveQueens(nrQueens, nrQueens));
		System.out.println("done");
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
		solveQueens(nrQueens, nrQueens);
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
					solution[i][newRow] = 1;
					newSolution.add(solution);
				}
			}
		}
		return newSolution;
	}

	private boolean noConflicts(int newRow, int i, int[][] solution) {
		removeAllActors();
		for (int x = 0; x < nrQueens; x++) {
			for (int y = 0; y < nrQueens; y++) {
				if (solution[x][y] == 1)
					addActor(new QueenActor(),new Location(x, y));
			}
		}
		return !isThreatenedByOtherQueen(new Location(i, newRow));
	}

	/*
	
	def damenproblem(reihen, spalten):
	    if reihen <= 0:
	        return [[]] # keine Dame zu setzen; leeres Brett ist Lösung
	    else:
	        return eine_dame_dazu(reihen - 1, spalten, damenproblem(reihen - 1, spalten))
	 
	# Probiere alle Spalten, in denen für eine gegebene Teillösung
	# eine Dame in "neue_reihe" gestellt werden kann.
	# Wenn kein Konflikt mit der Teillösung auftritt,
	# ist eine neue Lösung des um eine Reihe erweiterten
	# Bretts gefunden.
	def eine_dame_dazu(neue_reihe, spalten, vorherige_loesungen):
	    neue_loesungen = []
	    for loesung in vorherige_loesungen:
	        # Versuche, eine Dame in jeder Spalte von neue_reihe einzufügen.
	        for neue_spalte in range(spalten):
	            # print('Versuch: %s in Reihe %s' % (neue_spalte, neue_reihe))
	            if kein_konflikt(neue_reihe, neue_spalte, loesung):
	                # Kein Konflikte, also ist dieser Versuch eine Lösung.
	                neue_loesungen.append(loesung + [neue_spalte])
	    return neue_loesungen
	 
	# Kann eine Dame an die Position "neue_spalte"/"neue_reihe" gestellt werden,
	# ohne dass sie eine der schon stehenden Damen schlagen kann?
	def kein_konflikt(neue_reihe, neue_spalte, loesung):
	    # Stelle sicher, dass die neue Dame mit keiner der existierenden
	    # Damen auf einer Spalte oder Diagonalen steht.
	    for reihe in range(neue_reihe):
	        if loesung[reihe]         == neue_spalte              or  # Gleiche Spalte
	           loesung[reihe] + reihe == neue_spalte + neue_reihe or  # Gleiche Diagonale
	           loesung[reihe] - reihe == neue_spalte - neue_reihe:    # Gleiche Diagonale
	                return False
	    return True
	 
	for loesung in damenproblem(8, 8):
	    print(loesung)
	    
	    */
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
		new GGQueensBackTrackRec();
	}
}