package fourInARow;

/**
 * Is responsible for the correct transition of the GameGrid into an Array which can be used
 * by a computerplayer to compute it's next move. 
 * 
 * TODO: should only give a copy of the board to the computerplayer, not the real one.
 * @author mazzzy
 *
 */
public class ArrayManager {
	
	int[][] boardArray;
	int xMax, yMax;
	FourInARowVsComputer gg;
	
	public ArrayManager(FourInARowVsComputer gg, int xMax, int yMax) {
		this.gg = gg;
		this.xMax = xMax;
		this.yMax = yMax;
		initializeBoardArray();
	}
	
	public void addToken(int x, int y, int player) {
		this.boardArray[x][y] = player;
	}
	

	private void initializeBoardArray() {
		boardArray = new int[xMax][yMax];
		for (int x = 0; x < xMax; x++)
			for (int y = 0; y < yMax; y++)
				boardArray[x][y] = gg.getNoTokenRepresentation();
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
	private void printBoard() {
		String boardString = "";
		for (int y = yMax - 1; y >= 0; y--) {
			for (int x = 0; x < xMax; x++) {
				boardString += "|" + boardArray[x][y] + "|";
			}
			boardString += "\n";
		}
		System.out.println(boardString);
	}

}
