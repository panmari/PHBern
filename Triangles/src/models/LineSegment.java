package models;

import ch.aplu.jgamegrid.GGVector;

public class LineSegment {

	protected GGVector start;
	protected GGVector direction;

	/**
	 * A line that starts at the point <code>start</code>
	 * and has the length (!!) and direction of <code>direction</code>
	 * @param start
	 * @param direction
	 */
	public LineSegment(GGVector start, GGVector direction) {
		this.start = start;
		this.direction = direction;
	}
	
	public GGVector getPointOnLineSegment(double scaleDir) {
		if (scaleDir < 0 || scaleDir > 1)
			throw new IllegalArgumentException("Point would not be on line");
		return start.add(direction.mult(scaleDir));
	}
	
	public GGVector getIntersectionPointWith(LineSegment other) {
		if (cross(this.direction, other.direction) == 0)
			return null; // lines are parallel
		double magL = cross(other.start.sub(this.start), other.direction)/cross(this.direction, other.direction);
		double magTl = cross(other.start.sub(this.start), this.direction)/cross(this.direction, other.direction);
		if (isBetweenZeroAndOne(magL) && isBetweenZeroAndOne(magTl)) {
			return this.getPointOnLineSegment(magL);
		} else
			return null; // line segments do not intersect
	}

	private boolean isBetweenZeroAndOne(double d) {
		return d <= 1 && d >= 0;
	}

	private double cross(GGVector a, GGVector b) {
		return a.x*b.y - a.y*b.x;
	}
	
	public String toString() {
		return "start: " + start + " dir: " + direction;
	}
}
