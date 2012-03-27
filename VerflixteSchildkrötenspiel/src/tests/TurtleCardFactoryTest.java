package tests;

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
		tf.makeNewEmptyCard();
		tf.addHalfTurtle(Color.BLUE, Orientation.BACK);
		tf.getNewCard();
	}
	
	@Test
	public void shouldReturnCardWhenFourAdded() {
		tf.makeNewEmptyCard();
		tf.addHalfTurtle(Color.BLUE, Orientation.BACK);
		tf.addHalfTurtle(Color.BLUE, Orientation.BACK);
		tf.addHalfTurtle(Color.BLUE, Orientation.BACK);
		tf.addHalfTurtle(Color.BLUE, Orientation.BACK);
		TurtleCard tc = tf.getNewCard();
	}

}
