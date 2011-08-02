package fourInARow;

public class MMBot extends ComputerPlayer {

	private final int searchDepth = 6;
	private final int VALUE_QUAD = 10000, VALUE_TRIPPLE = 100, VALUE_PAIR = 20;
	private int solution;
	private int VALUE_MIDDLE = 1;

	public MMBot(ArrayManager am, int nbPlayer) {
		super(am, nbPlayer);
	}

	@Override
	public int getColumn() {
		debugInfo("Start: \n" + am.getStringBoard(board));
		int value = maxValue(searchDepth);
		debugInfo(solution + " is worth: " + value);
		return solution;
	}

	private int maxValue(int depth) {
		int value, bestValue;
		bestValue = Integer.MIN_VALUE;
		for (int x = 0; x < xMax; x++) {
			if (insertToken(thisPlayer, x)) {
				if (depth <= 0 || isGameOver()) {
					value = evaluateSituation(thisPlayer);
				} else value = minValue(depth - 1);
				removeTopmostToken(x);
				if (value > bestValue) {
					bestValue = value;
					if (depth == searchDepth) {
						solution = x;
					}
				}
			}
		}
		return bestValue;
	}
	
	private int minValue(int depth) {
		int value, bestValue;
		bestValue = Integer.MAX_VALUE;
		for (int x = 0; x < xMax; x++) {
			if(insertToken(other(thisPlayer), x)){
				if (depth <= 0 || isGameOver())
					value = evaluateSituation(thisPlayer);
				else value = maxValue(depth - 1);
				removeTopmostToken(x);
				if (value < bestValue)
					bestValue = value;
			}
		}
		return bestValue;
	}
	
	private boolean isGameOver() {
		return getLines(4, thisPlayer) + getLines(4, other(thisPlayer)) > 0 || isBoardFull();
	}

	private int evaluateSituation(int player) {
		int result = 0;

		if (getLines(4, other(player)) > 0)
			return (-1) * VALUE_QUAD;

		if (getLines(4, player) > 0)
			return VALUE_QUAD;
		
		for (int x = 2; x <= 4; x++)
			for (int y = 0; y < yMax; y++) 
				if (board[x][y] == player)
					result += VALUE_MIDDLE ;
				else if (board[x][y] == other(player))
					result -= VALUE_MIDDLE;
		
		result += VALUE_TRIPPLE * getLines(3, player);
		result += VALUE_PAIR * getLines(2, player);

		result -= VALUE_TRIPPLE * getLines(3, other(player));
		result -= VALUE_PAIR * getLines(2, other(player));
		
		//debugInfo(am.getStringBoard(board) + player + " <-- player | value --> " + result);
		return result;
	}
}
