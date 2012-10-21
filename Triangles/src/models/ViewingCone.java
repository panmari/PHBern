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
		Line[] viewBoarderLines = {	new Line(standPoint, standPoint.sub(vertices[1])),
							new Line(standPoint, standPoint.sub(vertices[2])) 
						};
		for (Line l: viewBoarderLines) {
			for (int i = 0; i < 3; i++) {
				Line tl = new Line(t.vertices[i], t.vertices[i].sub(t.vertices[nextVertexIndex(i)]));
				if (cross(l.direction, tl.direction) == 0)
					break; //lines are parallel
				double magL = cross(tl.start.sub(l.start), tl.direction)/cross(l.direction, tl.direction);
				double magTl = cross(tl.start.sub(l.start), l.direction)/cross(l.direction, tl.direction);
				if (isBetweenZeroAndOne(magL) && isBetweenZeroAndOne(magTl)) {
					intersectionPoints.add(l.getPointOnLine(magL));
				}
			}
		}
		return intersectionPoints;
	}
	
	private boolean isBetweenZeroAndOne(double d) {
		return d <= 1 && d >= 0;
	}

	private double cross(GGVector a, GGVector b) {
		return a.x*b.y - a.y*b.x;
	}
}
