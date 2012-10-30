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
		between = between.mult(radius);
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
			GGVector startCentered = l.start.sub(centre);
			double a = l.direction.magnitude2();
			double b = 2*startCentered.dot(l.direction);
			double c = startCentered.magnitude2() - radius*radius;
			double discriminant = b*b - 4*a*c;
			if (discriminant < 0)
				continue; //only imaginary solutions
			else {
				int[] plusminus = {-1, 1};
				for (int i: plusminus) {
					double t = (-b + i*Math.sqrt(discriminant))/(2*a);
					if (t >= 0 && t <= 1)
						intersectionPoints.add(l.getPointOnLineSegment(t));
				}
			}
		}
		return intersectionPoints;
	}
	
	public String toString() {
		return "Circle around " + centre + ", r=" + radius;
	}
}
