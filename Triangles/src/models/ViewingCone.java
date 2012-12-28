package models;

import java.util.LinkedList;
import java.util.List;

import ch.aplu.jgamegrid.GGVector;

public class ViewingCone extends Triangle{

	private LinkedList<IObstacle> obstacles;
	private LineSegment[] viewBoarderLines;
	private GGVector lookAtPoint;
	private double angle;
	private boolean infinite;
	
	/**
	 * This constructor is mainly for testing, for real use see the other constructor
	 */
	public ViewingCone(GGVector standPoint, GGVector b, GGVector c) {
		super(standPoint, b, c);
		this.obstacles = new LinkedList<IObstacle>();
		this.viewBoarderLines = new LineSegment[2];
		this.viewBoarderLines[0] = new LineSegment(standPoint, vertices[1].sub(standPoint));
		this.viewBoarderLines[1] = new LineSegment(standPoint, vertices[2].sub(standPoint));
		this.infinite = false;
	}
	
	/**
	 * Creates a viewing cone located at standPoint, looking into the direction of lookAtPoint,
	 * lookAtPoint also being the farthest visible point. The cone extends to angle/2
	 * to the left and angle/2 to the right of lookAtPoint.
	 * </br>
	 * Be aware that there may be problems with double precision when using this 
	 * constructor. 
	 * @param standPoint
	 * @param lookAtPoint
	 * @param angle in radian
	 * @param infinite Set to true if the farthest visible point lies in infinity
	 */
	public ViewingCone(GGVector standPoint, GGVector lookAtPoint, double angle, boolean infinite) {
		this(standPoint, 
				makeCorner(standPoint, lookAtPoint, angle/2), 
				makeCorner(standPoint, lookAtPoint, -angle/2));
		this.lookAtPoint = lookAtPoint;
		this.angle = angle;
		this.infinite = infinite;
		for (LineSegment ls: viewBoarderLines)
			ls.setInfinite(infinite);
	}
	
	private static GGVector makeCorner(GGVector standPoint, GGVector lookAtPoint,
			double angle) {
		GGVector corner = lookAtPoint.sub(standPoint);
		corner.rotate(angle);
		corner = corner.add(standPoint);
		return corner;
	}
	
	public void addObstacle(IObstacle o) {
		obstacles.add(o);
	}
	
	public void addObstacles(List<IObstacle> oList) {
		obstacles.addAll(oList);
	}
	
	/**
	 * Returns closest point of any obstacles added to this Viewing cone. 
	 * Returns null if there is no obstacle visible. 
	 * @return
	 */
	public GGVector getClosestObstacle() {
		GGVector best = null;
		for (IObstacle o: obstacles) {
			GGVector closest = o.closestPointTo(getStandPoint());
			if (liesInside(closest) && isCloser(closest, best))
				best = closest; //not possible that intersecting point is closer
			else {
				for (GGVector candidate: o.getIntersectionPointsWith(viewBoarderLines))
					if (isCloser(candidate, best))
						best = candidate;
			}
		}
		return best;
	}
	/**
	 * Returns the distance to the closest obstacle or
	 *  Double.NaN if there is no obstacle in the viewing cone.
	 * @return
	 */
	public double getDistanceToClosestObstacle() {
		GGVector obstaclePoint = getClosestObstacle();
		if (obstaclePoint == null)
			return Double.NaN;
		GGVector fromSPtoOP = obstaclePoint.sub(getStandPoint());
		return fromSPtoOP.magnitude();
	}
	
	private boolean isCloser(GGVector candidate, GGVector best) {
		if (best == null) 
			return true;
		return candidate.sub(getStandPoint()).magnitude2() < best.sub(getStandPoint()).magnitude2();
	}
	
	public boolean liesInside(GGVector p) {
		GGVector pNorm = toTriangleCoordinates(p);
		// don't use magnitude2() or you'll have (more) rounding errors!
		return pNorm.x >= 0 && pNorm.y >= 0 && (infinite || pNorm.magnitude() <= 1); 
	}
	
	@Override
	public String toString() {
		String result = "ViewingCone around:";
		for (GGVector v: vertices)
			result += " " + v;
		return result;
	}
	
	public GGVector getStandPoint() {
		return vertices[0];
	}
	
	public void setStandPoint(GGVector standPoint) {
		vertices[0] = standPoint;
	}
	
	public GGVector getLookAtPoint() {
		return lookAtPoint;
	}
	
	public void setLookAtPoint(GGVector lookAtPoint) {
		this.lookAtPoint = lookAtPoint;
		vertices[1] = makeCorner(getStandPoint(), lookAtPoint, angle/2);
		vertices[2] = makeCorner(getStandPoint(), lookAtPoint, -angle/2);
	}

	public boolean removeObstacle(IObstacle t) {
		return obstacles.remove(t);
	}
}
