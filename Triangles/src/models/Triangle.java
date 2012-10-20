package models;
import ch.aplu.jgamegrid.GGVector;

public class Triangle {
	
	private GGVector[] vertices = new GGVector[3];
	
	public Triangle(GGVector a, GGVector b, GGVector c) {
		vertices[0] = a;
		vertices[1] = b;
		vertices[2] = c;
	}
	
	public boolean liesInside(Triangle t) {
		for (GGVector v: vertices) {
			if (!liesInside(v))
				return false;
		}
		return true;
	}
	
	/**
	 * idea: transform to standard coordinates, solve there easily
	 * 
	 * @param p
	 * @return
	 */
	public boolean liesInside(GGVector p) {
		GGVector pNorm = toTriangleCoordinates(p);
		return pNorm.x >= 0 && pNorm.y >= 0 && pNorm.x + pNorm.y <= 1 ;
	}
	
	public GGVector toTriangleCoordinates(GGVector p) {
		GGVector edgeOne = vertices[1].sub(vertices[0]);
		GGVector edgeTwo = vertices[2].sub(vertices[0]);
		GGVector translatedP = p.sub(vertices[0]);
		
		double divisor = (edgeOne.x*edgeTwo.y-edgeOne.y*edgeTwo.x);
		double x = (translatedP.x*edgeTwo.y - edgeOne.y*translatedP.y)/divisor;
		double y = (edgeOne.x*translatedP.y - translatedP.x*edgeTwo.x)/divisor;

		return new GGVector(x, y);
	}
	
	public GGVector closestPointTo(GGVector p) {
		//TODO: make iterator for points?
		GGVector best = closestPointOfLineTo(vertices[0], vertices[nextVertexIndex(0)], p);
		for (int i = 1; i < vertices.length; i++) {
			GGVector candidate = closestPointOfLineTo(vertices[i], vertices[nextVertexIndex(i)], p);
			if (candidate.sub(p).magnitude2() < best.sub(p).magnitude2())
				best = candidate;
		}
		return best;
	}
	
	private GGVector closestPointOfLineTo(GGVector a, GGVector b, GGVector p) {
		GGVector ap = p.sub(a);
		GGVector ab = b.sub(a);
		double abMag = ab.magnitude2();
		
		double apDotAb = ap.dot(ab);
		
		double t = apDotAb / abMag;
		//only points on the line are valid:
		if (t <= 0)
			return a;
		else if (t >= 1)
			return b;
		else return a.add(ab.mult(t));
	}

	private int nextVertexIndex(int i) {
		return (i + 1) % 3;
	}
	
	public GGVector[] getVertices() {
		return vertices;
	}
}
