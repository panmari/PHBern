package tests;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import java.awt.Point;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ph.CardGrid;
import ph.TurtleCard;

public class CardGritTest {

	CardGrid gg;
	ArrayList<TurtleCard> noDeadCards;
	
	@Before
	public void setUp() throws Exception {
		gg = new CardGrid();
		noDeadCards = new ArrayList<TurtleCard>();
	}

	@Test
	public void testIsThereConflict() {
		TurtleCard[][] grid = gg.getGrid();
		assertNull(grid[0][0]);
		
		Point p = gg.putDownNextAliveCard(noDeadCards);
		assertNotNull(grid[p.x][p.y]);
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownNextAliveCard(noDeadCards);
		assertNotNull(grid[p.x][p.y]);
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownNextAliveCard(noDeadCards);
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownNextAliveCard(noDeadCards);
		assertNotNull(grid[p.x][p.y]);
		assertEquals(new Point(0,1), p);
		assertTrue(gg.isThereConflict(p));
	}

	@Test
	public void puttingDownLastCardShouldReturnNull() {
		assertEquals(new Point(0,0), gg.putDownNextAliveCard(noDeadCards));
		assertEquals(new Point(1,0), gg.putDownNextAliveCard(noDeadCards));
		assertEquals(new Point(2,0), gg.putDownNextAliveCard(noDeadCards));
		assertEquals(new Point(0,1), gg.putDownNextAliveCard(noDeadCards));
		assertEquals(new Point(1,1), gg.putDownNextAliveCard(noDeadCards));
		assertEquals(new Point(2,1), gg.putDownNextAliveCard(noDeadCards));
		assertEquals(new Point(0,2), gg.putDownNextAliveCard(noDeadCards));
		assertEquals(new Point(1,2), gg.putDownNextAliveCard(noDeadCards));
		assertEquals(new Point(2,2), gg.putDownNextAliveCard(noDeadCards));
		assertNull(gg.putDownNextAliveCard(noDeadCards));	
	}
	
	@Test
	public void shouldNotLaySameCardTwice() {
		gg.putDownNextAliveCard(noDeadCards);
		gg.putDownNextAliveCard(noDeadCards);
		gg.putDownNextAliveCard(noDeadCards);
		
		assertThat(gg.getCardAt(new Point(0,0)), is(not(gg.getCardAt(new Point(1,0)))));
		assertThat(gg.getCardAt(new Point(0,0)), is(not(gg.getCardAt(new Point(2,0)))));
	}
	
	@Test
	public void shouldNotLayDeadCard() {
		ArrayList<TurtleCard> deadCards = new ArrayList<TurtleCard>();
		deadCards.add(gg.getCardAt(gg.putDownNextAliveCard(noDeadCards)));
		deadCards.add(gg.getCardAt(gg.putDownNextAliveCard(noDeadCards)));
		deadCards.add(gg.getCardAt(gg.putDownNextAliveCard(noDeadCards)));
		gg.removeCardAt(new Point (2,0));
		gg.removeCardAt(new Point (2,0));
		gg.removeCardAt(new Point (2,0));
		gg.putDownNextAliveCard(deadCards);
		gg.putDownNextAliveCard(deadCards);
		gg.putDownNextAliveCard(deadCards);
		
		assertThat(deadCards, not(hasItem(gg.getCardAt(new Point(0,0)))));
		assertThat(deadCards, not(hasItem(gg.getCardAt(new Point(0,1)))));
	}
	
	@Test
	public void testRotateCardAt() {
		//fail("Not yet implemented");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRemoveFirstLaidCard() {
		Point p = gg.putDownNextAliveCard(noDeadCards);
		gg.removeCardAt(p);
	}
	
	@Test
	public void testRemoveSecondLaidCard() {
		Point p = gg.putDownNextAliveCard(noDeadCards);
		p = gg.putDownNextAliveCard(noDeadCards);
		assertEquals(new Point(0,0),gg.removeCardAt(p));
	}

	@Test
	public void testRemoveFourthLaidCard() {
		gg.putDownNextAliveCard(noDeadCards);
		gg.putDownNextAliveCard(noDeadCards);
		gg.putDownNextAliveCard(noDeadCards);
		Point p = gg.putDownNextAliveCard(noDeadCards);
		assertEquals(new Point(2,0),gg.removeCardAt(p));
	}
	
	@Test
	public void testRemoveSeventhLaidCard() {
		for (int i = 0; i < 6; i++)
			gg.putDownNextAliveCard(noDeadCards);
		Point p = gg.putDownNextAliveCard(noDeadCards);
		assertEquals(new Point(2,1),gg.removeCardAt(p));
	}
}
