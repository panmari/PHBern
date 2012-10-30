package models;

import java.util.LinkedList;
import java.util.List;

import ch.aplu.jgamegrid.GGVector;

public class Circle implements IObstacle {

	private GGVector centre;
	private double radius;

	public Circle(GGVector centre, double radius) {
		this.centre = centre;
		this.radius = radius;
	}

	@Override
	public GGVector closestPointTo(GGVector p) {
		GGVector between = p.sub(centre);
		between.normalize();
		between.mult(radius);
		return centre.add(between);
	}

	@Override
	public boolean liesInside(GGVector p) {
		GGVector v = p.sub(centre);
		return v.magnitude() < radius;
	}

	@Override
	public List<GGVector> getIntersectionPointsWith(
		LineSegment[] viewBoarderLines) {
		LinkedList<GGVector> intersectionPoints = new LinkedList<GGVector>();
		for (LineSegment l: viewBoarderLines) {
			//TODO: get Intersection points
		}
		return intersectionPoints;
	}
}
