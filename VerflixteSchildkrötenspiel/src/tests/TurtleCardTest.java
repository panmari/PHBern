package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ph.TurtleCard;
import ph.TurtleCardFactory;

/**
 * @author panmari
 *
 */
public class TurtleCardTest {

	private TurtleCardFactory tf;

	/**
	 * Creates a card for testing purposes
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tf = TurtleCardFactory.getInstance();
	}

	/**
	 * Test method for {@link TurtleCard#rotateCardToRight()}.
	 */
	@Test
	public void testRotateCardClockwise() {
		TurtleCard tc = tf.makeTurtleCard("yf;gb;rb;bf");
		assertEquals("[yf, gb, rb, bf]" ,tc.toString());
		assertFalse(tc.rotateCardToRight());
		assertEquals("[bf, yf, gb, rb]", tc.toString());
		assertFalse(tc.rotateCardToRight());
		assertEquals("[rb, bf, yf, gb]", tc.toString());
		assertFalse(tc.rotateCardToRight());
		assertEquals("[gb, rb, bf, yf]", tc.toString());
		assertTrue(tc.rotateCardToRight());
		assertEquals("[yf, gb, rb, bf]", tc.toString());
	}

}
