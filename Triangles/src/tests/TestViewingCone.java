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
	public void shouldNotConsiderIfNotInside() {
		Triangle t = new Triangle(new GGVector(-1, -.5), new GGVector(-1, 5), new GGVector(-5, 1));
		vc.addObstacle(t);
		GGVector v = vc.getClosestObstacle();		
		assertNull(v);
	}
	/**
	 * see test_case files for proof
	 */
	@Test
	public void shouldConsiderIntersectionPointIfClosest() {
		Triangle t = new Triangle(new GGVector(.5, .5), new GGVector(-1, -.9), new GGVector(-5, 1));
		vc.addObstacle(t);
		GGVector v = vc.getClosestObstacle();
		assertEquals(0, v.x, epsilon);
		assertEquals(1/30f, v.y, epsilon);
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
	
	@Test
	public void testAlternativeConstructor() {
		ViewingCone alternativeConstr = new ViewingCone(new GGVector(0,0), new GGVector(1,1), Math.PI/2, false);
		GGVector[] vertices = alternativeConstr.getVertices();
		
		assertEquals(0, vertices[0].x, epsilon);
		assertEquals(0, vertices[0].y, epsilon);
		
		assertEquals(0, vertices[1].x, epsilon);
		assertEquals(Math.sqrt(2), vertices[1].y, epsilon);
		
		assertEquals(Math.sqrt(2), vertices[2].x, epsilon);
		assertEquals(0, vertices[2].y, epsilon);
	}
	
	@Test
	public void testAlternativeAngle() {
		ViewingCone alternativeConstr = new ViewingCone(new GGVector(0,0), new GGVector(0,1), Math.PI/3, false);
		GGVector[] vertices = alternativeConstr.getVertices();
		assertEquals(0, vertices[0].x, epsilon);
		assertEquals(0, vertices[0].y, epsilon);
		
		assertEquals(-0.5, vertices[1].x, epsilon);
		assertEquals(Math.sqrt(3/4d), vertices[1].y, epsilon);
		
		assertEquals(0.5, vertices[2].x, epsilon);
		assertEquals(Math.sqrt(3/4d), vertices[2].y, epsilon);
	}
	
	@Test
	public void testOnePointTriangle() {
		Triangle tiny = new Triangle(new GGVector(0.5,0.5), new GGVector(0.5,0.5), new GGVector(0.5,0.5));
		assertTrue(vc.liesInside(tiny));
		vc.addObstacle(tiny);
		double dist = vc.getDistanceToClosestObstacle();
		assertNotNull(vc.getClosestObstacle());
		assertEquals(dist, Math.sqrt(0.5*0.5 + 0.5 * 0.5), epsilon);
	}
	
	@Test
	public void testRandom() {
		ViewingCone vcAlternative = new ViewingCone(new GGVector(300,300), new GGVector(500, 300), Math.PI/4, true);
		Triangle t = new Triangle(new GGVector(398.0, 407.0), new GGVector(427.0, 386.0), new GGVector(401.0, 379.0));
		assertFalse(vcAlternative.liesInside(t));
		assertFalse(vcAlternative.liesInside(t.getVertices()[0]));
		assertFalse(vcAlternative.liesInside(t.getVertices()[1]));
		assertFalse(vcAlternative.liesInside(t.getVertices()[2]));
		vcAlternative.addObstacle(t);
		assertEquals(Double.NaN, vcAlternative.getDistanceToClosestObstacle(), epsilon);
	}
}
