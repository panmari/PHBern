package fourInARowTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fourInARow.ArrayManager;
import fourInARow.MMBot;

public class ComputerPlayerTest {

	private MMBot cp;
	private ArrayManager am;

	@Before
	public void setUp() throws Exception {
		am = new ArrayManager(null, 7, 6);
		cp = new MMBot(am, 0);
	}

	@Test
	public void shouldGet3InRow() {
		am.addToken(0, 1, 0);
		am.addToken(1, 2, 0);
		am.addToken(2, 3, 0);
		cp.updateBoard();
		assertEquals(1, cp.getLines(3, 0));
		assertEquals(2, cp.getLines(2, 0));
	}
	
	@Test
	public void shouldGet4InRow() {
		am.addToken(0, 1, 0);
		am.addToken(1, 2, 0);
		am.addToken(2, 3, 0);
		am.addToken(3, 4, 0);
		cp.updateBoard();
		assertEquals(1, cp.getLines(4, 0));
		assertEquals(2, cp.getLines(3, 0));
		assertEquals(3, cp.getLines(2, 0));
	}
	
	@Test
	public void shouldNotGet4InRow() {
		am.addToken(0, 1, 0);
		am.addToken(1, 2, 0);
		am.addToken(2, 3, 1);
		am.addToken(3, 4, 0);
		cp.updateBoard();
		assertEquals(0, cp.getLines(4, 0));
		assertEquals(0, cp.getLines(3, 0));
		assertEquals(1, cp.getLines(2, 0));
	}
	
	@Test
	public void shouldGet4InRowVertically() {
		am.addToken(1, 1, 0);
		am.addToken(1, 2, 0);
		am.addToken(1, 3, 0);
		am.addToken(1, 4, 0);
		cp.updateBoard();
		assertEquals(1, cp.getLines(4, 0));
		assertEquals(2, cp.getLines(3, 0));
		assertEquals(3, cp.getLines(2, 0));
	}
	
	@Test
	public void shouldGet4InRowHorizontally() {
		am.addToken(1, 1, 1);
		am.addToken(2, 1, 1);
		am.addToken(3, 1, 1);
		am.addToken(4, 1, 1);
		cp.updateBoard();
		assertEquals(1, cp.getLines(4, 1));
		assertEquals(2, cp.getLines(3, 1));
		assertEquals(3, cp.getLines(2, 1));
	}
}
