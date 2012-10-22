package tests;

import static org.junit.Assert.*;

import models.Triangle;
import models.ViewingCone;

import org.junit.Before;
import org.junit.Test;

import ch.aplu.jgamegrid.GGVector;

public class TestViewingCone {

	private ViewingCone vc;
	private final double epsilon = 0.0001;
	
	@Before
	public void setUp() {
		vc = new ViewingCone(new GGVector(0,0), new GGVector(1,0), new GGVector(0,1));
	}
	@Test
	public void testWithCornerAsClosestPoint() {
		Triangle t = new Triangle(new GGVector(.5, .5), new GGVector(5,5), new GGVector(5.1,5.1));
		vc.addObstacle(t);
		GGVector v = vc.getClosestObstacle();
		
		assertEquals(.5, v.x, epsilon);
		assertEquals(.5, v.y, epsilon);
	}
	
	@Test
	public void testWithMultipleTriangles() {
		Triangle t = new Triangle(new GGVector(.5, .5), new GGVector(5,5), new GGVector(5.1,5.1));
		vc.addObstacle(t);
		t = new Triangle(new GGVector(1,1), new GGVector(-5,5), new GGVector(5.1,5.1));
		vc.addObstacle(t);
		GGVector v = vc.getClosestObstacle();
		
		assertEquals(.5, v.x, epsilon);
		assertEquals(.5, v.y, epsilon);
	}

	@Test
	public void middleOfSideShouldBeClosest() {
		Triangle t = new Triangle(new GGVector(0, .5), new GGVector(5,5), new GGVector(.5,0));
		vc.addObstacle(t);
		GGVector v = vc.getClosestObstacle();
		
		assertEquals(.25, v.x, epsilon);
		assertEquals(.25, v.y, epsilon);
	}
	
	@Test
	public void shouldConsiderIfNotInside() {
		Triangle t = new Triangle(new GGVector(-1, -.5), new GGVector(-1, 5), new GGVector(-5, 1));
		vc.addObstacle(t);
		GGVector v = vc.getClosestObstacle();		
		assertNull(v);
	}
	
	@Test
	public void liesInsideShouldConsiderConeShape() {
		Triangle t = new Triangle(new GGVector(0,0), new GGVector(1,0), new GGVector(0,1));
		GGVector g = new GGVector(0.51, 0.51);
		assertFalse(t.liesInside(g));
		assertTrue(vc.liesInside(g));
		g = new GGVector(Math.sqrt(1/2d), Math.sqrt(1/2d));
		assertFalse(t.liesInside(g));
		assertTrue(vc.liesInside(g));
		g = new GGVector(Math.sqrt(1/2d) + 0.1, Math.sqrt(1/2d) + 0.1);
		assertFalse(t.liesInside(g));
		assertFalse(vc.liesInside(g));
	}
}
