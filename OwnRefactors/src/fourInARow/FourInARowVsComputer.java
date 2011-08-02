package fourInARow;

import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;

import java.awt.*;
import java.util.Arrays;

public class FourInARowVsComputer extends GameGrid implements GGMouseListener {
	private int currentPlayer = 0;
	public boolean finished = false;
	Token activeToken;
	private ComputerPlayer computerPlayer;
	private ArrayManager arrayManager;
	private String moveInfo = "Move mouse to a column and click to set the token.";

	public FourInARowVsComputer() {
		super(7, 7, 70, null, null, false);
		addMouseListener(this, GGMouse.lPress | GGMouse.move);
		this.getBg().setBgColor(Color.white);
		activeToken = new Token(currentPlayer, this);
		addActor(activeToken, new Location(0, 0), Location.SOUTH);
		// outside of grid, so it doesn't disturb game:
		addActor(new BG(), new Location(3, -1));
		getBg().setFont(new Font("SansSerif", Font.BOLD, 48));
		getBg().setPaintColor(Color.red);
		show();
		setSimulationPeriod(30);
		doRun();
		addStatusBar(30);
		setStatusText(moveInfo);
		setTitle("Four In A Row (against Computer). Developed by Stefan Moser.");
		arrayManager = new ArrayManager(this, nbHorzCells, nbVertCells - 1);
		computerPlayer = new MMBot(arrayManager, 1); // menu for choosing?
	}

	public void reset() {
		getBg().clear();
		removeActors(Token.class);
		currentPlayer = 0; // Human player always starts (bc i'm lazy)
		setStatusText("Game reset! " + (currentPlayer == 0 ? "Yellow" : "Red")
				+ " player begins.");
		activeToken = new Token(currentPlayer, this);
		addActor(activeToken, new Location(0, 0), Location.SOUTH);
		arrayManager.reset();
		finished = false;
	}

	public void computerMove() {
		setMouseEnabled(false);
		int col = computerPlayer.getColumn();
		activeToken.setX(col);
		activeToken.setActEnabled(true);
		currentPlayer = (currentPlayer + 1) % 2; // change Player
		setStatusText(moveInfo);
	}

	@Override
	public boolean mouseEvent(GGMouse mouse) {
		Location mouseLoc = toLocation(mouse.getX(), mouse.getY());
		if (mouse.getEvent() == GGMouse.move) { // move active Token with mouse
			if (!finished && activeToken.getX() != mouseLoc.x)
				activeToken.setX(mouseLoc.x);
			return true;
		}

		if (finished) {
			reset();
			return true;
		}
		
		if (isColumnNotFull(mouseLoc.x)) { 
			activeToken.setActEnabled(true);
			setMouseEnabled(false);
			currentPlayer = (currentPlayer + 1) % 2;
		} else {
			setStatusText("This column is full.");
		}

		return true;
	}

	private boolean isColumnNotFull(int x) {
		return getOneActorAt(new Location(x, 1)) == null;
	}

	public int getPlayerOfTokenAt(int x, int y) {
		Location loc = new Location(x, y);
		if (getOneActorAt(loc) == null)
			return arrayManager.getNoTokenRepresentation();
		else
			return ((Token) getOneActorAt(loc)).getPlayer();
	}

	public boolean check4Win(Location loc) {
		int col = loc.x;
		int row = loc.y;
		return (checkVertically(col, row, 4) || checkHorizontally(col, row, 4)
				|| checkDiagonally1(col, row, 4) || checkDiagonally2(col, row,
				4));
	}

	private boolean checkDiagonally2(int col, int row, int nrOfTokens) {
		for (int j = 0; j < nrOfTokens; j++) {
			int adjacentSameTokens = 0;
			for (int i = 0; i < nrOfTokens; i++) {
				if ((col - i + j) >= 0
						&& (col - i + j) < nbHorzCells
						&& (row + i - j) >= 1
						&& (row + i - j) < nbVertCells
						&& getPlayerOfTokenAt(col - i + j, row + i - j) == getPlayerOfTokenAt(
								col, row)) {
					adjacentSameTokens++;
				}
			}
			if (adjacentSameTokens >= nrOfTokens)
				return true;
		}
		return false;
	}

	private boolean checkDiagonally1(int col, int row, int nrOfTokens) {
		for (int j = 0; j < nrOfTokens; j++) {
			int adjacentSameTokens = 0;
			for (int i = 0; i < nrOfTokens; i++) {
				if ((col + i - j) >= 0
						&& (col + i - j) < nbHorzCells
						&& (row + i - j) >= 1
						&& (row + i - j) < nbVertCells
						&& getPlayerOfTokenAt(col + i - j, row + i - j) == getPlayerOfTokenAt(
								col, row)) {
					adjacentSameTokens++;
				}
			}
			if (adjacentSameTokens >= nrOfTokens)
				return true;
		}
		return false;
	}

	private boolean checkHorizontally(int col, int row, int nrOfTokens) {
		int adjacentSameTokens = 1;
		int i = 1;
		while (col - i >= 0
				&& getPlayerOfTokenAt(col - i, row) == getPlayerOfTokenAt(col,
						row)) {
			adjacentSameTokens++;
			i++;
		}
		i = 1;
		while (col + i < nbHorzCells
				&& getPlayerOfTokenAt(col + i, row) == getPlayerOfTokenAt(col,
						row)) {
			adjacentSameTokens++;
			i++;
		}
		return (adjacentSameTokens >= nrOfTokens);
	}

	private boolean checkVertically(int col, int row, int nrOfTokens) {
		int adjacentSameTokens = 1;
		int i = 1;
		while (row + i < nbVertCells
				&& getPlayerOfTokenAt(col, row + i) == getPlayerOfTokenAt(col,
						row)) {
			adjacentSameTokens++;
			i++;
		}
		return (adjacentSameTokens >= nrOfTokens);
	}

	public static void main(String[] args) {
		new FourInARowVsComputer();
	}

	/**
	 * Transformation of cell position -> array position happens here!
	 * 
	 * @param x in the GameGrid
	 * @param y in the GameGrid
	 * @param token
	 */
	public void updateArray(int x, int y, Token token) {
		arrayManager.addToken(x, (nbVertCells - 1) - y, token.getPlayer());
	}

	public void printBoard() {
		arrayManager.printBoard();
	}

	public void tokenArrived(Location tokLoc, int player) {
		if (check4Win(tokLoc)) {
			gameOver((player == 0 ? "You won!" : "You lost!")
				+ " Click on the board to play again.");
		} else if (isBoardFull()){
			gameOver("Tie! Click on the board to play again");
		} else {
			// make new Token:
			activeToken = new Token((player + 1) % 2, this);
			addActor(activeToken, new Location(getX(), 0),
					Location.SOUTH);
		}
		setMouseEnabled(true);
		if (player == 0 && !finished) 
			computerMove();
	}

	private void gameOver(String reason) {
		setStatusText(reason);
		getBg().drawText("Game Over", new Point(10, 55));
		finished = true;
		refresh();
		Monitor.putSleep(2000); // wait for 2 seconds
	}
	
	private boolean isBoardFull() {
		for (int x = 0; x < nbHorzCells; x++)
			if (isColumnNotFull(x))
				return false;
		return true;
	}
}
