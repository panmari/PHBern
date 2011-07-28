package fourInARow;

public class NewMM extends ComputerPlayer {

	private final int searchDepth = 6;
	private final int VALUE_QUAD = 1000, VALUE_TRIPPLE = 10, VALUE_PAIR = 2;
	private int solution, bestValue;

	public NewMM(ArrayManager am, int nbPlayer) {
		super(am, nbPlayer);
	}

	@Override
	public int getColumn() {
		bestValue = Integer.MIN_VALUE;
		System.out.println("Value: " + MaxMove(thisPlayer, searchDepth));
		am.printBoard();
		return solution;
	}

	private int MaxMove(int player, int depth) {
		if (depth == 0 || isBoardFull() || getLines(4, other(player)) + getLines(4, player) > 0) {
			return evaluateSituation(player);
		} else {
			for (int x = 0; x < xMax; x++) {
				if (insertToken(player, x)) {
					int value = MinMove(other(player), depth - 1);
					if (value > bestValue) {
						bestValue = value;
						if (depth == searchDepth)
							solution = x;
					}
					removeTopmostToken(x);
				}
			}
		}
		return bestValue;
	}

	private int MinMove(int player, int depth) {
		for (int x = 0; x < xMax; x++) {
			if (insertToken(player, x)) {
				int value = MaxMove(other(player), depth - 1);
				if (value > bestValue) {
					bestValue = value;
				}
				removeTopmostToken(x);
			}
		}
		return bestValue;
	}

	/**
	 * subject to change >.<
	 * 
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
