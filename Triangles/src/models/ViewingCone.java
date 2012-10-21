package models;

import java.util.LinkedList;

import ch.aplu.jgamegrid.GGVector;

public class ViewingCone extends Triangle{

	private GGVector standPoint;
	private LinkedList<Triangle> obstacles;
	
	public ViewingCone(GGVector a, GGVector b, GGVector c) {
		super(a, b, c);
		this.standPoint = a;
		this.obstacles = new LinkedList<Triangle>();
	}
	
	public void addObstacle(Triangle t) {
		obstacles.add(t);
	}
	
	public GGVector getClosestObstacle() {
		//TODO add special case, where intersection of viewing cone & obstacle is closest value
		GGVector best = new GGVector(Double.MAX_VALUE, Double.MAX_VALUE);
		for (Triangle t: obstacles) {
			LinkedList<GGVector> candidates = new LinkedList<GGVector>();
			candidates.add(t.closestPointTo(standPoint));
			candidates.addAll(getIntersectionPoints(t));
			for (GGVector candidate: candidates)
				if (liesInside(candidate) && isCloser(candidate, best))
					best = candidate;
		}
		return best;
	}
	
	private boolean isCloser(GGVector candidate, GGVector best) {
		return candidate.sub(standPoint).magnitude2() < best.sub(candidate).magnitude2();
	}

	private LinkedList<GGVector> getIntersectionPoints(Triangle t) {
		LinkedList<GGVector> intersectionPoints = new LinkedList<GGVector>();
		LineSegment[] viewBoarderLines = {	new LineSegment(standPoint, standPoint.sub(vertices[1])),
											new LineSegment(standPoint, standPoint.sub(vertices[2])) };
		for (LineSegment l: viewBoarderLines) {
			for (int i = 0; i < 3; i++) {
				LineSegment tl = new LineSegment(t.vertices[i], t.vertices[i].sub(t.vertices[nextVertexIndex(i)]));
				GGVector p = l.getIntersectionPointWith(tl);
				if (p != null)
					intersectionPoints.add(p);
			}
		}
		return intersectionPoints;
	}
}
