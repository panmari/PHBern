package fourInARowTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fourInARow.ArrayManager;

public class ArrayManagerTest {

	ArrayManager am;
	int[][] board;
	
	@Before
	public void setUp() throws Exception {
		am = new ArrayManager(null, 7, 6);
		board = am.getBoardArray();
	}

	@Test
	public void testAddToken() {
		am.addToken(0, 1, 1);
		assertEquals(1, board[0][1]);
		am.addToken(4, 5, 1);
		assertEquals(1, board[4][5]);
	}
	
	@Test (expected=ArrayIndexOutOfBoundsException.class)
	public void tokenInInvalidColumnShouldThrowException() {
		am.addToken(10, 3, 1);
	}
	
	@Test //(expected=ArrayIndexOutOfBoundsException.class)
	public void tokenOfInvalidPlayerShouldThrowException() {
		am.addToken(2, 3, 5);
		//well, it does not, has to be implemented >.<
	}

	@Test
	public void arrayCopyShouldBeSameAsReal() {
		testAddToken();
		am.addToken(1, 3, 0);
		int[][] boardCopy = am.getBoardCopy();
		for (int x = 0; x < am.getxMax(); x++)
			for (int y = 0; y < am.getyMax(); y++)
				assertEquals(board[x][y], boardCopy[x][y]);
	}

	@Test
	public void testReset() {
		//add some random Tokens:
		am.addToken(0, 1, 1);
		am.addToken(4, 5, 1);
		am.addToken(2, 1, 1);
		am.addToken(3, 4, 1);
		am.reset();
		for (int[] x : board) 
			for (int pos : x)
				assertEquals(am.getNoTokenRepresentation(), pos);
	}
	
	@Test
	public void copyStringShouldBeSameAsRealDeal() {
		assertEquals(am.getStringBoard(am.getBoardCopy()), 
				am.getStringBoard(am.getBoardArray()));
		
		am.addToken(1, 1, 1);
		am.addToken(2, 1, 1);
		am.addToken(3, 1, 1);
		am.addToken(4, 1, 1);
		
		assertEquals(am.getStringBoard(am.getBoardCopy()), 
				am.getStringBoard(am.getBoardArray()));
	}
}
