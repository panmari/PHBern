package fourInARow;

/**
 * Is responsible for the correct transition of the GameGrid into an Array which can be used
 * by a computerplayer to compute it's next move. Does nothing else but managing the board.
 * All game or strategic related data has to be computed in the computerPlayer!
 * 
 * TODO: should only give a copy of the board to the computerplayer, not the real one.
 * @author mazzzy
 *
 */
public class ArrayManager {
	
	private final int noToken = 2;
	private int[][] boardArray;
	private int xMax, yMax;
	private FourInARowVsComputer gg;
	
	public ArrayManager(FourInARowVsComputer gg, int xMax, int yMax) {
		this.gg = gg;
		this.xMax = xMax;
		this.yMax = yMax;
		boardArray = new int[xMax][yMax];
		initializeBoardArray();
	}
	
	public void addToken(int x, int y, int player) {
		assert (player == 1 || player == 0);
		this.boardArray[x][y] = player;
	}
	

	private void initializeBoardArray() {
		for (int x = 0; x < xMax; x++)
			for (int y = 0; y < yMax; y++)
				boardArray[x][y] = noToken;
	}
	
	public int[][] getBoardArray() {
		return boardArray;
	}
	
	/**
	 * TODO: check for invalid board changes!
	 */
	private void invariant() {
		
	}
	
	/**
	 * For debugging purposes only
	 * Prints the given array in the console
	 * @param board
	 */
	public void printBoard() {
		String boardString = "";
		for (int y = yMax - 1; y >= 0; y--) {
			for (int x = 0; x < xMax; x++) {
				boardString += "|" + boardArray[x][y] + "|";
			}
			boardString += "\n";
		}
		System.out.println(boardString);
	}

	public void reset() {
		initializeBoardArray();
	}
	
	public int getNoTokenRepresentation() {
		return noToken;
	}
	
	public int getxMax() {
		return xMax;
	}
	
	public int getyMax() {
		return yMax;
	}

}
