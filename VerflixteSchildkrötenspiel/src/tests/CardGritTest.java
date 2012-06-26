package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gg.CardGrid;
import gg.DataSetParser;
import gg.TurtleCard;

import java.awt.Point;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.aplu.jgamegrid.Location;


public class CardGritTest {

	CardGrid gg;
	List<TurtleCard> allCards;
	
	@Before
	public void setUp() throws Exception {
		allCards = new DataSetParser("cardset.data").parse();
		gg = new CardGrid(allCards);
		allCards = gg.getCards();
	}

	@Test
	public void testIsThereConflict() {
		TurtleCard[][] grid = gg.getGrid();
		assertNull(grid[0][0]);
		
		Location p = gg.putDownCard(allCards.get(0));
		assertNotNull(grid[p.x][p.y]);
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownCard(allCards.get(0));
		assertNotNull(grid[p.x][p.y]);
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownCard(allCards.get(0));
		assertFalse(gg.isThereConflict(p));
		
		p = gg.putDownCard(allCards.get(0));
		assertNotNull(grid[p.x][p.y]);
		assertEquals(new Point(0,1), p);
		assertTrue(gg.isThereConflict(p));
	}
	
	@Test
	public void shouldNotLaySameCardTwice() {
		gg.putDownCard(allCards.get(0));
		gg.putDownCard(allCards.get(0));
		gg.putDownCard(allCards.get(0));
		
		assertFalse(gg.getCardAt(new Location(0,0)).equals(gg.getCardAt(new Location(1,0))));
		assertFalse(gg.getCardAt(new Location(0,0)).equals(gg.getCardAt(new Location(2,0))));
	}
}
