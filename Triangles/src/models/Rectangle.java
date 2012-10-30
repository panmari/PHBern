package models;

import java.util.LinkedList;
import java.util.List;

import ch.aplu.jgamegrid.GGRectangle;
import ch.aplu.jgamegrid.GGVector;

public class Rectangle implements IObstacle {

	LinkedList<Triangle> triangles;
	
	/**
	 * Could also be more generalized
	 * @param r
	 */
	public Rectangle(GGRectangle r) {
		GGVector[] vertices = r.getVertexes();
		triangles = new LinkedList<Triangle>();
		triangles.add(new Triangle(vertices[0], vertices[1], vertices[3]));
		triangles.add(new Triangle(vertices[1], vertices[2], vertices[3]));
	}
	
	@Override
	public GGVector closestPointTo(GGVector p) {
		GGVector best = new GGVector(Double.MAX_VALUE, Double.MAX_VALUE);
		for (Triangle t: triangles) {
			GGVector candidate = t.closestPointTo(p);
			if (p.distanceTo(candidate) < p.distanceTo(best))
				best = candidate;
		}
		return best;
	}

	@Override
	public boolean liesInside(GGVector p) {
		for (Triangle t: triangles)
			if (t.liesInside(p))
				return true;
		return false;
	}

	@Override
	public List<GGVector> getIntersectionPointsWith(LineSegment[] viewBoarderLines) {
		LinkedList<GGVector> intersectionPoints = new LinkedList<GGVector>();
		for (Triangle t: triangles)
			intersectionPoints.addAll(t.getIntersectionPointsWith(viewBoarderLines));
		return intersectionPoints;
	}

}
