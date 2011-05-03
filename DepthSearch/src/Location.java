
public class Location extends ch.aplu.jgamegrid.Location {

	public Location() {
		super();
	}

	public Location(Location location) {
		super(location);
	}

	public Location(int x, int y) {
		super(x, y);
	}

	/**
	 * is this still not in the library?
	 */
	public boolean equals(Object o) {
		if (o.getClass() != Location.class)
			return false;
		else {
			Location testLoc = (Location) o;
			return (super.x == testLoc.x && super.y == testLoc.y);
		}
	} 
}
