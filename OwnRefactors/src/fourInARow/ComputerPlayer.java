package fourInARow;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class ComputerPlayer {

	private FourInARow2 gg;
	private int thisPlayer;
	private int enemyPlayer;
	private int[][] boardArray;

	public ComputerPlayer(FourInARow2 gg, int nbPlayer) {
		this.gg = gg;
		this.thisPlayer = nbPlayer;
		this.enemyPlayer = (thisPlayer + 1) % 2;
		int[][] board = initializeBoardArray();

	}

	public int getColumn() {
		printBoard(board);
		
		//do something useful with the given board to decide on the next move
		
		return (int) (Math.random() * 7);
	}

	private int[][] initializeBoardArray() {
		int xMax = gg.getNbHorzCells();
		int yMax = gg.getNbVertCells() - 1; // topmost row doesn't belong to game
		int[][] board = new int[xMax][yMax];
		for (int x = 0; x < xMax; x++)
			for (int y = 0; y < yMax; y++)
				board[x][y] = gg.getNoTokenRepresentation();
		return board;
	}
	
	public void updateBoard(int x, int y, int player) {
		this.boardArray[x][y] = player;
	}

	private boolean isBoardEmpty(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			if (board[i][0] != gg.getNoTokenRepresentation())
				return false;
		}
		return true;
	}

	private void printBoard(int[][] board) {
		String boardString = "";
		for (int y = board[0].length - 1; y >= 0; y--) {
			for (int x = 0; x < board.length; x++) {
				boardString += "|" + board[x][y] + "|";
			}
			boardString += "\n";
		}
		System.out.println(boardString);
	}

}
