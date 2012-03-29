package tests;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;

import ph.CardPosition;

public class CardPositionTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetNext() {
		assertThat(CardPosition.DOWN, is(CardPosition.RIGHT.getNext()));
		assertThat(CardPosition.LEFT, is(CardPosition.DOWN.getNext()));
		assertThat(CardPosition.UP, is(CardPosition.LEFT.getNext()));
		assertThat(CardPosition.RIGHT, is(CardPosition.UP.getNext()));
	}
	
	@Test
	public void testOpposite() {
		assertThat(CardPosition.DOWN, is(CardPosition.UP.getOpposite()));
		assertThat(CardPosition.LEFT, is(CardPosition.RIGHT.getOpposite()));
		assertThat(CardPosition.UP, is(CardPosition.DOWN.getOpposite()));
		assertThat(CardPosition.RIGHT, is(CardPosition.LEFT.getOpposite()));
	}

}
