package ph;

public class HalfTurtle {

	private Color color;
	private Orientation orientation;
	
	public HalfTurtle(Color color, Orientation orientation) {
		this.color = color;
		this.orientation = orientation;
	}
	
	public String toString() {
		return "" + color.getCharRepresentation() + orientation.getCharRepresentation();
	}
}
