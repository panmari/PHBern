package ph;

public class HalfTurtle {

	private Color color;
	private Orientation orientation;
	
	/**
	 * This constructor shouldn't be called directly. Utilize the TurtleCardFactory
	 * for adding HalfTurtles to a card.
	 * @param color
	 * @param orientation
	 */
	public HalfTurtle(Color color, Orientation orientation) {
		this.color = color;
		this.orientation = orientation;
	}
	
	public String toString() {
		return "" + color.getCharRepresentation() + orientation.getCharRepresentation();
	}

	public boolean matches(HalfTurtle otherTurtle) {
		return (this.color == otherTurtle.color) 
			&& (this.orientation == otherTurtle.orientation.getOpposite());
	}
}
