package fourInARow;

public class MiniMaxBot extends ComputerPlayer {

	private final int searchDepth = 6;
	private final int VALUE_QUAD = 1000, VALUE_TRIPPLE = 10, VALUE_PAIR = 2;
	//private final int minValue = -10000, maxValue = 10000;
	private final int minValue = Integer.MIN_VALUE, maxValue = Integer.MAX_VALUE;
	private int solution;
	
	
	public MiniMaxBot(ArrayManager am, int nbPlayer) {
		super(am, nbPlayer);
	}

	@Override
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

}
