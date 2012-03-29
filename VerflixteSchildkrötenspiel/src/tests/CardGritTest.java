package tests;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import ph.CardGrid;
import ph.TurtleCard;

public class CardGritTest {

	CardGrid gg;
	@Before
	public void setUp() throws Exception {
		gg = new CardGrid();
	}

	@Test
	public void testIsThereConflict() {
		TurtleCard[][] grid = gg.getGrid();
		assertNull(grid[0][0]);
		
		Point p = gg.putDownNextCard();
		assertNotNull(grid[p.x][p.y]);
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownNextCard();
		assertNotNull(grid[p.x][p.y]);
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownNextCard();
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownNextCard();
		assertNotNull(grid[p.x][p.y]);
		assertEquals(new Point(0,1), p);
		assertTrue(gg.isThereConflict(p));
	}

	@Test
	public void testPutDownNextCard() {
		assertEquals(new Point(0,0), gg.putDownNextCard());
		assertEquals(new Point(1,0), gg.putDownNextCard());
		assertEquals(new Point(2,0), gg.putDownNextCard());
		assertEquals(new Point(0,1), gg.putDownNextCard());
		assertEquals(new Point(1,1), gg.putDownNextCard());
		assertEquals(new Point(2,1), gg.putDownNextCard());
		assertEquals(new Point(0,2), gg.putDownNextCard());
		assertEquals(new Point(1,2), gg.putDownNextCard());
		assertEquals(new Point(2,2), gg.putDownNextCard());
		assertNull(gg.putDownNextCard());	
	}

	@Test
	public void testRotateCardAt() {
		//fail("Not yet implemented");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRemoveFirstLaidCard() {
		Point p = gg.putDownNextCard();
		gg.removeCardAt(p);
	}
	
	@Test
	public void testRemoveSecondLaidCard() {
		Point p = gg.putDownNextCard();
		p = gg.putDownNextCard();
		assertEquals(new Point(0,0),gg.removeCardAt(p));
	}

	@Test
	public void testRemoveFourthLaidCard() {
		gg.putDownNextCard();
		gg.putDownNextCard();
		gg.putDownNextCard();
		Point p = gg.putDownNextCard();
		assertEquals(new Point(2,0),gg.removeCardAt(p));
	}
	
	@Test
	public void testRemoveSeventhLaidCard() {
		for (int i = 0; i < 6; i++)
			gg.putDownNextCard();
		Point p = gg.putDownNextCard();
		assertEquals(new Point(2,1),gg.removeCardAt(p));
	}
}
