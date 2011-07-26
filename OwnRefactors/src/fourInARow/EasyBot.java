package fourInARow;

import java.util.Random;

/**
 * Plays randomly apart from the two obvious cases.
 *
 */
public class EasyBot extends ComputerPlayer {

	private int enemyPlayer;
	public EasyBot(ArrayManager am, int nbPlayer) {
		super(am, nbPlayer);
		enemyPlayer = (thisPlayer + 1) % 2;
	}

	@Override
	public int getColumn() {
		 // Can I win in this turn?
	    for (int column = 0; column < xMax; column++)
	    {
	    	if(insertToken(thisPlayer, column)) {
	    		if (getLines(4, thisPlayer) > 0) {
			        debugInfo("Found something that makes me win: " + column);
			        removeTopmostToken(column);
			        return column;
	    		}
	    	removeTopmostToken(column);
	    	}
	    	
	    }

	    // Can enemy win in his next turn?
	    for (int column = 0; column < xMax; column++)
		{
	    	if(insertToken(enemyPlayer, column)) {
	    		if (getLines(4, enemyPlayer) > 0) {
			        debugInfo("Found something that makes enemy win: " + column);
			        removeTopmostToken(column);
			        return column;
	    		}
	    	removeTopmostToken(column);
	    	}
	    }
		return getRandomNotFullColumn();
	}

	private int getRandomNotFullColumn() {
		Random rnd = new Random();
		do {
			int rndColumn = rnd.nextInt(xMax);
			if (insertToken(thisPlayer, rndColumn)) {
				debugInfo("Chose randomly: " + rndColumn);
				removeTopmostToken(rndColumn);
				return rndColumn;
			}
				
		} while (!isBoardFull());
		return -1; //this shouldn't happen
	}

}
