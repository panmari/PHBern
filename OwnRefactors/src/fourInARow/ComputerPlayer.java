package fourInARow;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class ComputerPlayer {

	private FourInARowVsComputer gg;
	private int thisPlayer;
	private int enemyPlayer;
	private int[][] board;
	int xMax, yMax;
	private final int VALUE_QUAD = 100, VALUE_TRIPPLE = 10, VALUE_PAIR = 1;

	public ComputerPlayer(FourInARowVsComputer gg, int nbPlayer) {
		this.gg = gg;
		this.thisPlayer = nbPlayer;
		this.enemyPlayer = (thisPlayer + 1) % 2;
		initializeBoardArray();
	}

	public int getColumn() {

		// do something useful with the given board to decide on the next move

		return (int) (Math.random() * 7);
	}

	private void initializeBoardArray() {
		xMax = gg.getNbHorzCells();
		yMax = gg.getNbVertCells() - 1; // topmost row doesn't belong to game
		board = new int[xMax][yMax];
		for (int x = 0; x < xMax; x++)
			for (int y = 0; y < yMax; y++)
				board[x][y] = gg.getNoTokenRepresentation();
	}

	public void updateBoard(int x, int y, int player) {
		this.board[x][y] = player;
		printBoard(board);
	}

	private boolean isBoardEmpty(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			if (board[i][0] != gg.getNoTokenRepresentation())
				return false;
		}
		return true;
	}

	private int evaluateSituation(int player) {
		int otherPlayer = (player + 1) % 2;
		int result = 0;

		if (getLines(4, otherPlayer) > 0)
			return (-1) * VALUE_QUAD;

		if (getLines(4, player) > 0) 
			return VALUE_QUAD;

		result += VALUE_TRIPPLE * getLines(3, player);
		result += VALUE_PAIR * getLines(2, player);

		result -= VALUE_TRIPPLE * getLines(3, otherPlayer);
		result -= VALUE_PAIR * getLines(2, otherPlayer);
		return result;
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

	int getLines(int length, int player) {
		return (getHorizontalLines(length, player)
				+ getVerticalLines(length, player) + getDiagonalLines(length,
				player));
	}

	int getHorizontalLines(int length, int player) {
		int total_hits = 0;
		if (length > 4 || length < 2)
			return 0;

		for (int y = 0; y < yMax; y++) {
			for (int x = 0; x <= xMax - length; x++) {
				int hit = 1;
				for (int t = 1; t < length; t++) {
					if ((board[x + t][y] != board[x][y])
							|| (board[x + t][y] != player))
						hit = 0;
				}
				total_hits += hit;
			}
		}
		return total_hits;
	}

	int getVerticalLines(int length, int player) {
		int total_hits = 0;

		if (length > 4 || length < 2)
			return 0;

		for (int x = 0; x < 7; x++) {
			for (int y = 0; y <= yMax - length; y++) {
				int hit = 1;
				for (int t = 1; t < length; t++) {
					if ((board[x][y + t] != board[x][y])
							|| (board[x][y + t] != player))
						hit = 0;
				}
				total_hits += hit;
			}
		}
		return total_hits;
	}

	int getDiagonalLines(int length, int color) {
		int total_hits = 0;

		for (int x = 0; x <= (xMax - length); x++) {
			for (int y = 0; y <= (yMax - length); y++) {
				int hit = 1;
				for (int t = 1; t < length; t++) {
					if ((board[x + t][y + t] != board[x][y])
							|| (board[x + t][y + t] != color))
						hit = 0;
				}
				total_hits += hit;
			}
		}

		for (int x = xMax - 1; x >= length - 1; x--) {
			for (int y = 0; y <= (yMax - length); y++) {
				int hit = 1;
				for (int t = 1; t < length; t++) {
					if ((board[x - t][y + t] != board[x][y])
							|| (board[x - t][y + t] != color))
						hit = 0;
				}
				total_hits += hit;
			}
		}
		return total_hits;
	}

	/**
	 * removes the topmost Token from the given column (only in Array, not in
	 * GameGrid)
	 */
	private void undoLastMove(int column) {
		int y = yMax - 1;
		while (y >= 0) {
			if (board[column][y] != gg.getNoTokenRepresentation()) {
				board[column][y] = gg.getNoTokenRepresentation();
				return;
			}
			y--;
		}
	}

}
