package fourInARow;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class ComputerPlayer {

	private FourInARow2 gg;
	private int thisPlayer;
	private int enemyPlayer;

	public ComputerPlayer(FourInARow2 gg, int nbPlayer) {
		this.gg = gg;
		this.thisPlayer = nbPlayer;
	    this.enemyPlayer = (thisPlayer + 1) % 2;
	}

	public int getColumn() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private int[][] getBoardAsArrays() {
		int xMax = gg.getNbHorzCells();
		int yMax = gg.getNbVertCells() - 1; //topmost row doesn't belong to game
		int[][] board = new int[6][7];
		for (int x = 0; x < xMax; x++) {
			for (int y = 0; y < yMax; y++) { 
				Actor token = gg.getOneActorAt(new Location(xMax-x, yMax-y));
				if (token == null)
					//blub
			}
		}
	}
}
