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
		int[][] board = getBoardAsArrays();
		return (int) (Math.random()*7);
	}
	
	private int[][] getBoardAsArrays() {
		int xMax = gg.getNbHorzCells();
		int yMax = gg.getNbVertCells() - 1; //topmost row doesn't belong to game
		int[][] board = new int[xMax][yMax];
		for (int x = 0; x < xMax; x++) {
			for (int y = 0; y < yMax; y++) { 
				if (getTokenAt(x, y) == null)
					board[x][y] = -1;
				else board[x][y] = getTokenAt(x, y).getPlayer();
			}
		}
		return board;
	}
	
	/**
	 * (0,0) ist links unten
	 * @param x
	 * @param y
	 * @return
	 */
	private Token getTokenAt(int x, int y) {
		int xMax = gg.getNbHorzCells();
		int yMax = gg.getNbVertCells() - 1; //topmost row doesn't belong to game
		return (Token) gg.getOneActorAt(new Location(xMax-x, yMax-y));
	}
	
}
