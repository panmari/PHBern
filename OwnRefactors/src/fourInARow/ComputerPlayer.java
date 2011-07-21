package fourInARow;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class ComputerPlayer {

	private FourInARowVsComputer gg;
	private int thisPlayer;
	private int enemyPlayer;
	private int[][] board;
	int xMax, yMax;
	private final int minValue = -10000, maxValue = 10000;
	private int solution;
	private final int searchDepth = 6;
	private final int VALUE_QUAD = 1000, VALUE_TRIPPLE = 10, VALUE_PAIR = 2;

	public ComputerPlayer(FourInARowVsComputer gg, int nbPlayer) {
		this.gg = gg;
		this.thisPlayer = nbPlayer;
		this.enemyPlayer = (thisPlayer + 1) % 2;
		initializeBoardArray();
	}

	public int getColumn() {
		maxValueAB(thisPlayer, searchDepth, minValue, maxValue);
		return solution;
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

	int maxValueAB(int player, int depth, int alpha, int beta) {
		int otherPlayer = (player + 1) % 2;
		int currentValue;
		
		if (depth == 0)
			return evaluateSituation(player);
		for (int x = 0; x < xMax; x++) {
			if (insertStone(player, x)) {
				if (!isBoardFull())
					currentValue = minValueAB(otherPlayer, depth - 1, alpha,
							beta);
				else
					currentValue = minValueAB(otherPlayer, 0, alpha, beta);
				undoLastMove(x);
				if (currentValue >= beta)
					return beta;
				if (currentValue > alpha) {
					if (depth == this.searchDepth)
						this.solution = x;
					alpha = currentValue;
				}
			}
		}
		return alpha;
	} 
	
	private boolean insertStone(int player, int x) {
		int y = 0;
		while (y < yMax) {
			if (board[x][y] == gg.getNoTokenRepresentation()) {
				board[x][y] = player;
				return true;
			}
			y++;
		}
		return false;
	}

	private boolean isBoardFull() {
		for (int x = 0; x < xMax; x++)
			if (board[x][yMax-1] == gg.getNoTokenRepresentation())
				return false;
		return false;
	}

	int minValueAB(int player, int depth, int alpha, int beta) {
		int otherPlayer = (player + 1) % 2;
		int currentValue;
		if (depth == 0)
			return evaluateSituation(player);
		for (int x = 0; x < xMax; x++) {
			if (insertStone(player, x)) {
				if (!isBoardFull())
					currentValue = maxValueAB(otherPlayer, depth - 1, alpha,
							beta);
				else
					currentValue = maxValueAB(otherPlayer, 0, alpha, beta);
				undoLastMove(x);
				if (currentValue <= alpha)
					return alpha;
				if (currentValue < beta)
					beta = currentValue;
			}
		}
		return beta;
	}

	int maxValue(int player, int depth) {
		int result = minValue;
		int otherPlayer = (player + 1) % 2;
		int currentValue;

		for (int x = 0; x < xMax; x++) {
			if (insertStone(player, x)) {
				if (depth <= 0)
					currentValue = (evaluateSituation(player));
				else
					currentValue = minValue(otherPlayer, depth - 1);
				undoLastMove(x);
				if (currentValue > result) {
					result = currentValue;
					if (depth == searchDepth)
						solution = x;
				}
			}
		}
		return result;
	}

	int minValue(int player, int depth) {
		int otherPlayer = (player + 1) % 2;
		int result = maxValue;
		int currentValue;

		for (int x = 0; x < xMax; x++) {
			if (insertStone(player, x)) {
				if (depth <= 0)
					currentValue = evaluateSituation(player);
				else
					currentValue = maxValue(otherPlayer, depth - 1);
				undoLastMove(x);
				if (currentValue < result)
					result = currentValue;
			}
		}
		return result;
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
