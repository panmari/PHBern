package tests;

import static org.junit.Assert.*;

import java.util.Random;

import models.Triangle;
import models.ViewingCone;

import org.junit.Before;
import org.junit.Test;

import ch.aplu.jgamegrid.GGVector;

public class PerformanceTest {

	private ViewingCone vc;
	private Random rand;
	private final boolean mute = false;

	@Before
	public void setUp() throws Exception {
		vc = new ViewingCone(new GGVector(0,0), new GGVector(1,0), new GGVector(0,1));
		rand = new Random(100);
	}

	/**
	 * This takes 1.446 seconds for a million triangles
	 */
	@Test
	public void addHugeLotOfTriangles() {
		Triangle t;
		for (int i = 0; i < 1000000; i++) {
			t = new Triangle(new GGVector(rand.nextDouble(), rand.nextDouble()),
					new GGVector(rand.nextDouble(), rand.nextDouble()),
					new GGVector(rand.nextDouble(), rand.nextDouble()));
			vc.addObstacle(t);
		}
		GGVector v = vc.getClosestObstacle();
		assertNotNull(v);
		print(v);
	}
	
	private void print(Object o) {
		if (!mute)
			System.out.println(o);
	}

}
