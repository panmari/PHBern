package tests;

import static org.junit.Assert.*;

import models.Triangle;

import org.junit.Before;
import org.junit.Test;
import ch.aplu.jgamegrid.GGVector;

public class TriangleTest {

	private Triangle standardT;

	@Before
	public void setUp() {
		standardT = new Triangle(new GGVector(0,0), new GGVector(1, 0), new GGVector(0,1));
	}
	
	@Test
	public void testLiesInsideTriangle() {
		new GGVector(1,0);
		
	}

	@Test
	public void testLiesInsideGGVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testToTriangleCoordinates() {
		GGVector v = new GGVector(1,0);
		GGVector vTriangle = standardT.toTriangleCoordinates(v);
		assertEquals(v, vTriangle);
	}

}
