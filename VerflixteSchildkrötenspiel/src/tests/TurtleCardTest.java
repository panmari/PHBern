package tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ph.Color;
import ph.HalfTurtle;
import ph.Orientation;
import ph.TurtleCard;

/**
 * @author panmari
 *
 */
public class TurtleCardTest {

	private TurtleCard tc;

	/**
	 * Creates a card for testing purposes
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		HalfTurtle[] ta = new HalfTurtle[4];
		ta[0] = new HalfTurtle(Color.YELLOW, Orientation.BACK);
		ta[1] = new HalfTurtle(Color.RED, Orientation.BACK);
		ta[2] = new HalfTurtle(Color.YELLOW, Orientation.FRONT);
		ta[3] = new HalfTurtle(Color.GREEN, Orientation.FRONT);
		tc = new TurtleCard(ta);
	}

	/**
	 * Test method for {@link TurtleCard#rotateCardToRight()}.
	 */
	@Test
	public void testRotateCardToRight() {
		assertFalse(tc.rotateCardToRight());
	}

}
