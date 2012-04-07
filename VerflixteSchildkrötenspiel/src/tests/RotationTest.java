package tests;

import static org.junit.Assert.*;
import gg.CardPosition;

import org.junit.Before;
import org.junit.Test;

public class RotationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getOppositeShouldReturnOpposite() {
		assertEquals(CardPosition.LEFT, CardPosition.RIGHT.getOpposite());
		assertEquals(CardPosition.UP, CardPosition.DOWN.getOpposite());
		assertEquals(CardPosition.DOWN, CardPosition.UP.getOpposite());
		assertEquals(CardPosition.RIGHT, CardPosition.LEFT.getOpposite());
	}
	
	@Test
	public void getNextShouldReturnNextClockwise() {
		assertEquals(CardPosition.UP, CardPosition.LEFT.getNext());
		assertEquals(CardPosition.DOWN, CardPosition.RIGHT.getNext());
		assertEquals(CardPosition.LEFT, CardPosition.DOWN.getNext());
		assertEquals(CardPosition.RIGHT, CardPosition.UP.getNext());
	}

}
