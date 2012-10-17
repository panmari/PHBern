package models;
import ch.aplu.jgamegrid.GGVector;


public class ViewingCone extends Triangle{

	private GGVector standPoint;

	public ViewingCone(GGVector a, GGVector b, GGVector c) {
		super(a, b, c);
		this.standPoint = a;
	}

	public GGVector closestPointTo(Triangle t) {
		
		return new GGVector();
	}
}
