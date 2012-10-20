package tests;

import static org.junit.Assert.*;

import models.Triangle;

import org.junit.Before;
import org.junit.Test;

import ch.aplu.jgamegrid.GGVector;
public class TriangleTest {

	private Triangle standardT, scaledT, translatedT;
	private final double epsilon = 0.00001;
	
	@Before
	public void setUp() {
		standardT = new Triangle(new GGVector(0,0), new GGVector(1, 0), new GGVector(0,1));
		scaledT = new Triangle(new GGVector(0,0), new GGVector(2, 0), new GGVector(0,1));
		translatedT = new Triangle(new GGVector(1,1), new GGVector(2, 1), new GGVector(1, 2));
	}
	
	@Test
	public void testLiesInsideTriangle() {
		new GGVector(1,0);
		
	}

	@Test
	public void testLiesInsideGGVector() {
		assertTrue(standardT.liesInside(new GGVector(1/2f, 1/2f)));
		assertFalse(standardT.liesInside(new GGVector(2, 1/2f)));
		assertFalse(standardT.liesInside(new GGVector(-2, 1/2f)));
	}

	@Test
	public void testToTriangleCoordinatesWithStandardTriangle() {
		GGVector v = new GGVector(1,0);
		GGVector vTriangle = standardT.toTriangleCoordinates(v);
		assertEquals(v.x, vTriangle.x, epsilon);
		assertEquals(v.y, vTriangle.y, epsilon);
		
		v = new GGVector(3,0);
		vTriangle = standardT.toTriangleCoordinates(v);
		assertEquals(v.x, vTriangle.x, epsilon);
		assertEquals(v.y, vTriangle.y, epsilon);
		
		v = new GGVector(3,23);
		vTriangle = standardT.toTriangleCoordinates(v);
		assertEquals(v.x, vTriangle.x, epsilon);
		assertEquals(v.y, vTriangle.y, epsilon);
		
		v = new GGVector(0,0);
		vTriangle = standardT.toTriangleCoordinates(v);
		assertEquals(v.x, vTriangle.x, epsilon);
		assertEquals(v.y, vTriangle.y, epsilon);
	}
	
	@Test
	public void testToTriangleCoordinatesWithScaledTriangle() {
		GGVector v = new GGVector(1,0);
		GGVector vTriangle = scaledT.toTriangleCoordinates(v);
		assertEquals(v.x/2, vTriangle.x, epsilon);
		assertEquals(v.y, vTriangle.y, epsilon);
		
		v = new GGVector(3,0);
		vTriangle = scaledT.toTriangleCoordinates(v);
		assertEquals(v.x/2, vTriangle.x, epsilon);
		assertEquals(v.y, vTriangle.y, epsilon);
		
		v = new GGVector(3,23);
		vTriangle = scaledT.toTriangleCoordinates(v);
		assertEquals(v.x/2, vTriangle.x, epsilon);
		assertEquals(v.y, vTriangle.y, epsilon);
		
		v = new GGVector(0,0);
		vTriangle = scaledT.toTriangleCoordinates(v);
		assertEquals(v.x/2, vTriangle.x, epsilon);
		assertEquals(v.y, vTriangle.y, epsilon);
	}
	
	@Test
	public void testToTriangleCoordinatesWithTranslatedTriangle() {
		GGVector v = new GGVector(1,0);
		GGVector vTriangle = translatedT.toTriangleCoordinates(v);
		assertEquals(v.x - 1, vTriangle.x, epsilon);
		assertEquals(v.y - 1, vTriangle.y, epsilon);
		
		v = new GGVector(3,0);
		vTriangle = translatedT.toTriangleCoordinates(v);
		assertEquals(v.x - 1, vTriangle.x, epsilon);
		assertEquals(v.y - 1, vTriangle.y, epsilon);
		
		v = new GGVector(3,23);
		vTriangle = translatedT.toTriangleCoordinates(v);
		assertEquals(v.x - 1, vTriangle.x, epsilon);
		assertEquals(v.y - 1, vTriangle.y, epsilon);
		
		v = new GGVector(0,0);
		vTriangle = translatedT.toTriangleCoordinates(v);
		assertEquals(v.x - 1, vTriangle.x, epsilon);
		assertEquals(v.y - 1, vTriangle.y, epsilon);
	}
	
	@Test
	public void testClosestPointTo() {
		GGVector upperVertex = new GGVector(1,0);
		assertEquals(upperVertex.x, standardT.closestPointTo(upperVertex).x, epsilon);
		assertEquals(upperVertex.y, standardT.closestPointTo(upperVertex).y, epsilon);
		
		GGVector farRight = new GGVector(3,0);
		assertEquals(upperVertex.x, standardT.closestPointTo(farRight).x, epsilon);
		assertEquals(upperVertex.y, standardT.closestPointTo(farRight).y, epsilon);
		
		GGVector highUp = new GGVector(0, 10);
		assertEquals(0, standardT.closestPointTo(highUp).x, epsilon);
		assertEquals(1, standardT.closestPointTo(highUp).y, epsilon);
		
		GGVector onBisectingLine = new GGVector(10, 10);
		assertEquals(0.5, standardT.closestPointTo(onBisectingLine).x, epsilon);
		assertEquals(0.5, standardT.closestPointTo(onBisectingLine).y, epsilon);
	}
}
