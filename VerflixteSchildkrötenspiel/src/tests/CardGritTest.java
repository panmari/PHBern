package tests;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gg.CardGrid;
import gg.TurtleCard;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import ch.aplu.jgamegrid.Location;


public class CardGritTest {

	CardGrid gg;
	LinkedList<TurtleCard> allCards;
	
	@Before
	public void setUp() throws Exception {
		gg = new CardGrid();
		allCards = gg.getCards();
	}

	@Test
	public void testIsThereConflict() {
		TurtleCard[][] grid = gg.getGrid();
		assertNull(grid[0][0]);
		
		Location p = gg.putDownCard(allCards.getFirst());
		assertNotNull(grid[p.x][p.y]);
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownCard(allCards.getFirst());
		assertNotNull(grid[p.x][p.y]);
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownCard(allCards.getFirst());
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownCard(allCards.getFirst());
		assertNotNull(grid[p.x][p.y]);
		assertEquals(new Point(0,1), p);
		assertTrue(gg.isThereConflict(p));
	}
	
	@Test
	public void shouldNotLaySameCardTwice() {
		gg.putDownCard(allCards.getFirst());
		gg.putDownCard(allCards.getFirst());
		gg.putDownCard(allCards.getFirst());
		
		assertThat(gg.getCardAt(new Location(0,0)), is(not(gg.getCardAt(new Location(1,0)))));
		assertThat(gg.getCardAt(new Location(0,0)), is(not(gg.getCardAt(new Location(2,0)))));
	}
}
