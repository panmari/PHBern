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
		//if 
		return false;
	}
	
	/**
	 * idea: transform to standard coordinates, solve there easily
	 * 
	 * @param p
	 * @return
	 */
	public boolean liesInside(GGVector p) {
		return true;
	}
	
	public GGVector toTriangleCoordinates(GGVector p) {
		GGVector edgeOne = vertices[2].sub(vertices[0]);
		GGVector edgeTwo = vertices[1].sub(vertices[0]);
		GGVector translatedP = p.sub(vertices[0]);
		GGVector normOne = edgeOne.sub(vertices[0]);
		GGVector normTwo = edgeTwo.sub(vertices[0]);
		
		double b = 1/(-(normTwo.y*normOne.x/normTwo.x) + normOne.y);
		double a = -b*normTwo.y/normTwo.x;
		double d = 1/(-(normOne.y*normTwo.x/normOne.x) + normTwo.y);
		double c = -d*normOne.y/normOne.x;
		return new GGVector(a*translatedP.x + b*translatedP.y, c*translatedP.x + d*translatedP.y);
	}

	private int nextVertexIndex(int i) {
		return (i + 1) % 3;
	}
}
