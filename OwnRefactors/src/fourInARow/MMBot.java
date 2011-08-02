package fourInARow;

public class MMBot extends ComputerPlayer {

	private final int searchDepth = 8;
	private final int VALUE_QUAD = 10000, VALUE_TRIPPLE = 100, VALUE_PAIR = 20;
	private int solution;
	private int VALUE_MIDDLE = 1;

	public MMBot(ArrayManager am, int nbPlayer) {
		super(am, nbPlayer);
	}

	@Override
	public int getColumn() {
		debugInfo("Start: \n" + am.getStringBoard(board));
		int value = maxValue(searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		debugInfo(solution + " is worth: " + value);
		return solution;
	}

	private int maxValue(int depth, int alpha, int beta) {
		int newAlpha;
		for (int x = 0; x < xMax; x++) {
			if (insertToken(thisPlayer, x)) {
				if (depth <= 0 || isGameOver()) {
					newAlpha = evaluateSituation(thisPlayer);
				} else newAlpha = minValue(depth - 1, alpha, beta);
				removeTopmostToken(x);
				if (newAlpha > alpha) { //maximizing
					alpha = newAlpha;
					if (depth == searchDepth)
						solution = x;
				}
				if (newAlpha >= beta) //beta cut-off
					return beta;
			}
		}
		return alpha;
	}
	
	private int minValue(int depth, int alpha, int beta) {
		int newBeta;
		for (int x = 0; x < xMax; x++) {
			if(insertToken(other(thisPlayer), x)){
				if (depth <= 0 || isGameOver())
					newBeta = evaluateSituation(thisPlayer);
				else newBeta = maxValue(depth - 1, alpha, beta);
				removeTopmostToken(x);
				if (newBeta < beta) //minimizing
					beta = newBeta;
				if (newBeta <= alpha) //alpha cut-off
					return alpha; 
			}
		}
		return beta;
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
