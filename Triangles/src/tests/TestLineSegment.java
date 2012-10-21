package tests;

import static org.junit.Assert.*;

import models.LineSegment;

import org.junit.Test;

import ch.aplu.jgamegrid.GGVector;

public class TestLineSegment {

	private final double epsilon = 0.0001;
	@Test
	public void testGetPointOnLineSegment() {
		LineSegment ls = new LineSegment(new GGVector(0,0), new GGVector(10,0));
		GGVector v = ls.getPointOnLineSegment(0.5);
		
		assertEquals(5, v.x, epsilon);
		assertEquals(0, v.y, epsilon);
		
		ls = new LineSegment(new GGVector(1,2), new GGVector(1,1));
		v = ls.getPointOnLineSegment(0.5);
		
		assertEquals(1 + .5, v.x, epsilon);
		assertEquals(2 + .5, v.y, epsilon);
		
		ls = new LineSegment(new GGVector(1,2), new GGVector(5,1));
		v = ls.getPointOnLineSegment(0.2);
		
		assertEquals(1 + 1, v.x, epsilon);
		assertEquals(2 + .2, v.y, epsilon);
	}
	
	@Test
	public void testGetIntersectionPointWith() {
		LineSegment ls = new LineSegment(new GGVector(0,0), new GGVector(10,0));
		LineSegment ls2 =  new LineSegment(new GGVector(-5,-5), new GGVector(5,5));
		GGVector v = ls.getIntersectionPointWith(ls2);
		
		assertEquals(0, v.x, epsilon);
		assertEquals(0, v.y, epsilon);
		
		ls2 = new LineSegment(new GGVector(1,2), new GGVector(4, -5));
		v = ls.getIntersectionPointWith(ls2);

		assertEquals(2.6, v.x, epsilon);
		assertEquals(0, v.y, epsilon);
	}

	@Test
	public void testParallelLinesShouldHaveNoIntersection() {
		LineSegment ls = new LineSegment(new GGVector(0,0), new GGVector(10,0));
		LineSegment ls2 =  new LineSegment(new GGVector(0,1), new GGVector(10,0));
		GGVector v = ls.getIntersectionPointWith(ls2);
		assertNull(v);
	}

}
