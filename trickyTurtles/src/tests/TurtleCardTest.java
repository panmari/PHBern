package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gg.TurtleCard;
import gg.TurtleCardFactory;

import org.junit.Before;
import org.junit.Test;


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
		tf = new TurtleCardFactory();
	}

	/**
	 * Test method for {@link TurtleCard#rotateCardClockwise()}.
	 */
	@Test
	public void testRotateCardClockwise() {
		TurtleCard tc = tf.makeTurtleCard("yf;gb;rb;bf", "sprites/tc1.jpg");
		assertEquals("[yf, gb, rb, bf]" ,tc.toString());
		assertFalse(tc.rotateCardClockwise());
		assertEquals("[bf, yf, gb, rb]", tc.toString());
		assertFalse(tc.rotateCardClockwise());
		assertEquals("[rb, bf, yf, gb]", tc.toString());
		assertFalse(tc.rotateCardClockwise());
		assertEquals("[gb, rb, bf, yf]", tc.toString());
		assertTrue(tc.rotateCardClockwise());
		assertEquals("[yf, gb, rb, bf]", tc.toString());
	}
	
	@Test
	public void testIds() {
		TurtleCard tc = tf.makeTurtleCard("yf;gb;rb;bf", "sprites/tc1.jpg");
		assertEquals(0, tc.getId());
		TurtleCard tc2 = tf.makeTurtleCard("yf;gb;rb;bf", "sprites/tc1.jpg");
		assertEquals(1, tc2.getId());
		TurtleCard tc3 = tf.makeTurtleCard("yf;gb;rb;bf", "sprites/tc1.jpg");
		assertEquals(2, tc3.getId());
	}

}
