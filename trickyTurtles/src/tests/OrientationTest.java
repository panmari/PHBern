package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import solver.Orientation;


public class OrientationTest {

	@Test
	public void shouldBeOpposite() {
		assertEquals(Orientation.BACK, Orientation.FRONT.getOpposite());
		assertEquals(Orientation.FRONT, Orientation.BACK.getOpposite());
	}
	
	@Test
	public void shouldNotBeOpposite() {
		assertThat(Orientation.BACK, is(not(Orientation.BACK.getOpposite())));
		assertThat(Orientation.FRONT, is(not(Orientation.FRONT.getOpposite())));
	}

}
