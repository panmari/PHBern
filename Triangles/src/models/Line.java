package models;

import ch.aplu.jgamegrid.GGVector;

public class Line {

	protected GGVector start;
	protected GGVector direction;

	/**
	 * A line that starts at the point <code>start</code>
	 * and has the length (!!) and direction of <code>direction</code>
	 * @param start
	 * @param direction
	 */
	public Line(GGVector start, GGVector direction) {
		this.start = start;
		this.direction = direction;
	}
	
	public GGVector getPointOnLine(double scaleDir) {
		if (scaleDir < 0 || scaleDir > 1)
			throw new IllegalArgumentException("Point would not be on line");
		return start.add(direction.mult(scaleDir));
	}

}
