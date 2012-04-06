package tests;

import static org.junit.Assert.*;
import gg.Color;
import gg.HalfTurtle;
import gg.Orientation;

import org.junit.Test;



public class HalfTurtleTest {
	
	@Test
	public void shouldMatch() {
		HalfTurtle t = new HalfTurtle(Color.BLUE, Orientation.BACK);
		HalfTurtle s = new HalfTurtle(Color.BLUE, Orientation.FRONT);
		assertTrue(t.matches(s));
	}
	
	@Test
	public void shouldNotMatchBcOrientation() {
		HalfTurtle t = new HalfTurtle(Color.BLUE, Orientation.BACK);
		HalfTurtle s = new HalfTurtle(Color.BLUE, Orientation.BACK);
		assertFalse(t.matches(s));
	}
	
	@Test
	public void shouldNotMatchBcColor() {
		HalfTurtle t = new HalfTurtle(Color.BLUE, Orientation.BACK);
		HalfTurtle s = new HalfTurtle(Color.YELLOW, Orientation.FRONT);
		assertFalse(t.matches(s));
	}
	
	@Test
	public void shouldNotMatchBcBoth() {
		HalfTurtle t = new HalfTurtle(Color.BLUE, Orientation.BACK);
		HalfTurtle s = new HalfTurtle(Color.YELLOW, Orientation.BACK);
		assertFalse(t.matches(s));
	}
}
