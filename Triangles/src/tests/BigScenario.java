package tests;

import static org.junit.Assert.*;

import models.Triangle;
import models.ViewingCone;

import org.junit.Before;
import org.junit.Test;

import ch.aplu.jgamegrid.GGVector;

public class BigScenario {

	private ViewingCone vc;
	private Triangle obstacle1, obstacle2, obstacle3;
	private double epsilon = 0.01;

	/**
	 * See bigScenario.ggb for the setting.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		vc = new ViewingCone(new GGVector(6.05, 3.45), new GGVector(3.23, 1.67), Math.PI/3);
		obstacle1 = new Triangle(new GGVector(5.27, 4.79), new GGVector(3.53, 3.67), new GGVector(1.6, 2.52));
		obstacle2 = new Triangle(new GGVector(1.6, 2.52), new GGVector(3.11, 2.61), new GGVector(1.99, 1.35));
		obstacle3 = new Triangle(new GGVector(4.99, 2.24), new GGVector(6.75, 3.03), new GGVector(5.43, 1.21));
	}
	
	@Test
	public void checkViewingConeParameters() {
		GGVector[] vertices = vc.getVertices();
		assertEquals(6.05, vertices[0].x, epsilon);
		assertEquals(3.45, vertices[0].y, epsilon);
		
		assertEquals(4.5, vertices[1].x, epsilon);
		assertEquals(0.5, vertices[1].y, epsilon);
		
		assertEquals(2.72, vertices[2].x, epsilon);
		assertEquals(3.32, vertices[2].y, epsilon);
		
	}

	@Test
	public void noObstacle() {
		assertNull(vc.getClosestObstacle());
		assertEquals(-1, vc.getDistanceToClosestObstacle(), epsilon);
	}
	
	@Test
	public void firstObstacle() {
		vc.addObstacle(obstacle1);
		assertNull(vc.getClosestObstacle());
		assertEquals(-1, vc.getDistanceToClosestObstacle(), epsilon);
	}

	@Test
	public void secondObstacle() {
		vc.addObstacle(obstacle2);
		GGVector v = vc.getClosestObstacle();
		assertNotNull(v);
		assertEquals(3.11, v.x, epsilon);
		assertEquals(2.61, v.y, epsilon);
		assertEquals(3.06, vc.getDistanceToClosestObstacle(), epsilon);
	}
	
	@Test
	public void thirdObstacle() {
		vc.addObstacle(obstacle3);
		GGVector v = vc.getClosestObstacle();
		assertNotNull(v);
		assertEquals(5.55, v.x, epsilon);
		assertEquals(2.49, v.y, epsilon);
		assertEquals(1.08, vc.getDistanceToClosestObstacle(), epsilon);
	}
	
	@Test
	public void secondAndthirdObstacle() {
		vc.addObstacle(obstacle3);
		vc.addObstacle(obstacle2);
		GGVector v = vc.getClosestObstacle();
		assertNotNull(v);
		assertEquals(5.55, v.x, epsilon);
		assertEquals(2.49, v.y, epsilon);
		assertEquals(1.08, vc.getDistanceToClosestObstacle(), epsilon);
	}

}