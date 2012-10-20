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
			GGVector candidate = t.closestPointTo(standPoint);
			//TODO: add isShorterThan for Vectors
			if (liesInside(candidate) && 
					candidate.sub(standPoint).magnitude2() < best.sub(candidate).magnitude2())
				best = candidate;
		}
			
		return best;
	}
}
