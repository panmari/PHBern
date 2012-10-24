package models;

import java.util.LinkedList;

import ch.aplu.jgamegrid.GGVector;

public class ViewingCone extends Triangle{

	private GGVector standPoint;
	private LinkedList<Triangle> obstacles;
	private LineSegment[] viewBoarderLines;
	
	/**
	 * Creates a viewing cone located in standPoint
	 * @param standPoint
	 * @param b
	 * @param c
	 */
	public ViewingCone(GGVector standPoint, GGVector b, GGVector c) {
		super(standPoint, b, c);
		this.standPoint = standPoint;
		this.obstacles = new LinkedList<Triangle>();
		this.viewBoarderLines = new LineSegment[2];
		this.viewBoarderLines[0] = new LineSegment(standPoint, vertices[1].sub(standPoint));
		this.viewBoarderLines[1] = new LineSegment(standPoint, vertices[2].sub(standPoint));

	}
	
	/**
	 * Creates a viewing cone located at standPoint, looking into the direction of lookAtPoint,
	 * lookAtPoint also beeing the furthest visible point. The cone extends to angle/2
	 * to the left and angle/2 to the right of lookAtPoint.
	 * </br>
	 * Be aware that there may be problems with double precision when using this 
	 * constructor. 
	 * @param standPoint
	 * @param lookAtPoint
	 * @param angle in radian
	 */
	public ViewingCone(GGVector standPoint, GGVector lookAtPoint, double angle) {
		this(standPoint, 
				makeCorner(standPoint, lookAtPoint, angle/2), 
				makeCorner(standPoint, lookAtPoint, -angle/2));
	}
	
	private static GGVector makeCorner(GGVector standPoint, GGVector lookAtPoint,
			double d) {
		GGVector corner = lookAtPoint.sub(standPoint);
		corner.rotate(d);
		corner = corner.add(standPoint);
		return corner;
	}
	
	public void addObstacle(Triangle t) {
		obstacles.add(t);
	}
	
	/**
	 * Returns closest point of any obstacles. Returns null if there is no
	 * obstacle visible. 
	 * @return
	 */
	public GGVector getClosestObstacle() {
		GGVector best = null;
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
	/**
	 * Returns the distance to the closest obstacle or
	 *  -1 if there is no obstacle in the viewing cone.
	 * @return
	 */
	public double getDistanceToClosestObstacle() {
		GGVector obstaclePoint = getClosestObstacle();
		if (obstaclePoint == null)
			return -1;
		GGVector fromSPtoOP = obstaclePoint.sub(standPoint);
		return fromSPtoOP.magnitude();
	}
	
	private boolean isCloser(GGVector candidate, GGVector best) {
		if (best == null) 
			return true;
		return candidate.sub(standPoint).magnitude2() < best.sub(standPoint).magnitude2();
	}

	private LinkedList<GGVector> getIntersectionPoints(Triangle t) {
		LinkedList<GGVector> intersectionPoints = new LinkedList<GGVector>();
		for (LineSegment l: viewBoarderLines) {
			for (int i = 0; i < 3; i++) {
				LineSegment tl = new LineSegment(t.vertices[i], t.vertices[nextVertexIndex(i)].sub(t.vertices[i]));
				GGVector p = l.getIntersectionPointWith(tl);
				if (p != null)
					intersectionPoints.add(p);
			}
		}
		return intersectionPoints;
	}
	
	public boolean liesInside(GGVector p) {
		GGVector pNorm = toTriangleCoordinates(p);
		// don't use magnitude2() or you'll have (more) rounding errors!
		return pNorm.x >= 0 && pNorm.y >= 0 && pNorm.magnitude() <= 1; 
	}
	
	@Override
	public String toString() {
		String result = "ViewingCone around:";
		for (GGVector v: vertices)
			result += " " + v;
		return result;
	}
}
