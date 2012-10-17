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

	private int nextVertexIndex(int i) {
		return (i + 1) % 3;
	}
	
	public GGVector[] getVertices() {
		return vertices;
	}
}
