package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ph.CardNotReadyException;
import ph.Color;
import ph.Orientation;
import ph.TurtleCard;
import ph.TurtleCardFactory;

public class TurtleCardFactoryTest {

	private TurtleCardFactory tf;

	@Before
	public void setUp() throws Exception {
		tf = new TurtleCardFactory();
	}

	@Test(expected=CardNotReadyException.class)
	public void notReadyCardShouldThrowException() {
		tf.prepareEmptyCard();
		tf.addHalfTurtle(Color.BLUE, Orientation.BACK);
		tf.getNewCard();
	}
	
	@Test
	public void shouldReturnCardWhenFourAdded() {
		tf.prepareEmptyCard();
		tf.addHalfTurtle(Color.BLUE, Orientation.BACK);
		tf.addHalfTurtle(Color.BLUE, Orientation.BACK);
		tf.addHalfTurtle(Color.BLUE, Orientation.BACK);
		tf.addHalfTurtle(Color.BLUE, Orientation.BACK);
		TurtleCard tc = tf.getNewCard();
	}
	
	@Test
	public void shouldMakeCardOutOfString() {
		TurtleCard tc = tf.makeTurtleCard("yf;gb;rb;bf");
		assertEquals("[yf, gb, rb, bf]" ,tc.toString());
	}

}
