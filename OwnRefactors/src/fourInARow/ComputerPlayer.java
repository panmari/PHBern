package fourInARow;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class ComputerPlayer {

	private int thisPlayer;
	private int[][] board;
	private int xMax, yMax;
	//private final int minValue = -10000, maxValue = 10000;
	private final int minValue = Integer.MIN_VALUE, maxValue = Integer.MAX_VALUE;
	private int solution;
	private final int searchDepth = 6;
	private final int VALUE_QUAD = 1000, VALUE_TRIPPLE = 10, VALUE_PAIR = 2;
	private int solutionValue;
	private ArrayManager am;

	public ComputerPlayer(ArrayManager am, int nbPlayer) {
		this.am = am;
		this.thisPlayer = nbPlayer;
		this.board = am.getBoardArray();
		this.xMax = am.getxMax();
		this.yMax = am.getyMax();
	}

	public int getColumn() {
		System.out.println("value of solution: " + maxValueAB(thisPlayer, searchDepth, minValue, maxValue));
		//maxValue(thisPlayer, searchDepth);
		am.printBoard();
		return solution;
	}

	private int maxValueAB(int player, int depth, int alpha, int beta) {
		int otherPlayer = (player + 1) % 2;
		int currentValue;
		
		if (depth <= 0)
			return evaluateSituation(player);
		for (int x = 0; x < xMax; x++) {
			if (insertStone(player, x)) {
				if (!isBoardFull())
					currentValue = minValueAB(otherPlayer, depth - 1, alpha, beta);
				else
					currentValue = minValueAB(otherPlayer, 0, alpha, beta);
				removeTopmostToken(x);
				if (currentValue >= beta)
					return beta;
				if (currentValue > alpha) {
					if (depth == this.searchDepth) {
						this.solution = x;
						solutionValue = currentValue; //debug
					}
					alpha = currentValue;
				}
			}
		}
		return alpha;
	} 
	
	private int minValueAB(int player, int depth, int alpha, int beta) {
		int otherPlayer = (player + 1) % 2;
		int currentValue;
		if (depth <= 0)
			return evaluateSituation(player);
		for (int x = 0; x < xMax; x++) {
			if (insertStone(player, x)) {
				if (!isBoardFull())
					currentValue = maxValueAB(otherPlayer, depth - 1, alpha,beta);
				else
					currentValue = maxValueAB(otherPlayer, 0, alpha, beta);
				removeTopmostToken(x);
				if (currentValue <= alpha)
					return alpha;
				if (currentValue < beta)
					beta = currentValue;
			}
		}
		return beta;
	}

	private int maxValue(int player, int depth) {
		int result = minValue;
		int otherPlayer = (player + 1) % 2;
		int currentValue;

		for (int x = 0; x < xMax; x++) {
			if (insertStone(player, x)) {
				if (depth <= 0)
					currentValue = evaluateSituation(player);
				else
					currentValue = minValue(otherPlayer, depth - 1);
				removeTopmostToken(x);
				if (currentValue > result) {
					result = currentValue;
					if (depth == searchDepth) {
						solution = x;
						solutionValue = result; //debug
					}
				}
			}
		}
		return result;
	}

	private int minValue(int player, int depth) {
		int otherPlayer = (player + 1) % 2;
		int result = maxValue;
		int currentValue;

		for (int x = 0; x < xMax; x++) {
			if (insertStone(player, x)) {
				if (depth <= 0)
					currentValue = evaluateSituation(player);
				else
					currentValue = maxValue(otherPlayer, depth - 1);
				removeTopmostToken(x);
				if (currentValue < result)
					result = currentValue;
			}
		}
		return result;
	} 

	/**
	 * subject to change >.<
	 * @param player
	 * @return
	 */
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
	
	
	private int getLines(int length, int player) {
		return (getHorizontalLines(length, player) + 
				getVerticalLines(length, player) + 
				getDiagonalLines(length, player));
	}

	private int getHorizontalLines(int length, int player) {
		int total_hits = 0;
		if (length > 4 || length < 2)
			return 0;

		for (int y = 0; y < yMax; y++) {
			for (int x = 0; x <= xMax - length; x++) {
				int hit = 1;
				for (int t = 1; t < length; t++) {
					if (board[x + t][y] != board[x][y]
							|| board[x + t][y] != player) {
						hit = 0;
						break;
					}
				}
				total_hits += hit;
			}
		}
		return total_hits;
	}

	private int getVerticalLines(int length, int player) {
		int total_hits = 0;

		if (length > 4 || length < 2)
			return 0;

		for (int x = 0; x < xMax; x++) {
			for (int y = 0; y <= yMax - length; y++) {
				int hit = 1;
				for (int t = 1; t < length; t++) {
					if (board[x][y + t] != board[x][y]
							|| board[x][y + t] != player) {
						hit = 0;
						break;
					}
				}
				total_hits += hit;
			}
		}
		return total_hits;
	}

	private int getDiagonalLines(int length, int player) {
		int total_hits = 0;

		for (int x = 0; x <= (xMax - length); x++) {
			for (int y = 0; y <= (yMax - length); y++) {
				int hit = 1;
				for (int t = 1; t < length; t++) {
					if ((board[x + t][y + t] != board[x][y])
							|| (board[x + t][y + t] != player))
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
							|| (board[x - t][y + t] != player))
						hit = 0;
				}
				total_hits += hit;
			}
		}
		return total_hits;
	}
	
	private boolean isBoardFull() {
		for (int x = 0; x < xMax; x++)
			if (board[x][yMax-1] == am.getNoTokenRepresentation())
				return false;
		return false;
	}

	private boolean insertStone(int player, int x) {
		int y = 0;
		while (y < yMax) {
			if (board[x][y] == am.getNoTokenRepresentation()) {
				board[x][y] = player;
				return true;
			}
			y++;
		}
		return false;
	}

	private void removeTopmostToken(int column) {
		int y = yMax - 1;
		while (y >= 0) {
			if (board[column][y] != am.getNoTokenRepresentation()) {
				board[column][y] = am.getNoTokenRepresentation();
				return;
			}
			y--;
		}
	}

}
