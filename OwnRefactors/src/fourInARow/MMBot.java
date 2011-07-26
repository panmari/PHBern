package fourInARow;

public class MMBot extends ComputerPlayer {

	private final int searchDepth = 6;
	private final int VALUE_QUAD = 1000, VALUE_TRIPPLE = 10, VALUE_PAIR = 2;
	private int solution;

	public MMBot(ArrayManager am, int nbPlayer) {
		super(am, nbPlayer);
	}

	@Override
	public int getColumn() {
		int value = alphabeta(searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, thisPlayer);
		debugInfo(solution + " is worth: " + value);
		am.printBoard();
		return solution;
	}

	private int alphabeta(int depth, int alpha, int beta, int player) {
		if (depth == 0 || isBoardFull() || getLines(4, other(player)) > 0) {
			am.printBoard();
			debugInfo("Value: " + evaluateSituation(thisPlayer));
			return evaluateSituation(thisPlayer);
		}
		if (player == thisPlayer) {
			for (int x = 0; x < xMax; x++) {
				insertToken(player, x);
				int newAlpha = alphabeta(depth - 1, alpha, beta, other(player));
				if (alpha < newAlpha) { //maximize alpha
					alpha = newAlpha;
					if (depth == searchDepth)
						solution = x;
				}
				removeTopmostToken(x);
				if (beta <= alpha) // beta cut-off
					break;
			}
			return alpha;

		} else {
			for (int x = 0; x < xMax; x++) {
				insertToken(player, x);
				int newBeta = alphabeta(depth - 1, alpha, beta, other(player));
				if (beta > newBeta) { //minimize beta
					beta = newBeta;
					if (depth == searchDepth)
						solution = x;
				}
				removeTopmostToken(x);
				if (beta <= alpha)
					break; // alpha cut-off
			}
			return beta;
		}
	}

	private int other(int player) {
		return (player + 1) % 2;
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
}
