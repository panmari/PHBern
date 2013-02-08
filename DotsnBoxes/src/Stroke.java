
import java.awt.Point;
import java.util.LinkedList;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;


public class Stroke extends Actor {

	private DotsnBoxes gg;
	private StrokeDirection direction;
	
	public Stroke(DotsnBoxes gg, StrokeDirection d) {
		super(true, "sprites/stroke.png", 3);
		this.gg = gg;
		this.direction = d;
	}
	
	public void reset() {
		this.turn(direction.ordinal()*90);
		this.setLocationOffset(scaleOffset(direction.getOffset()));
		this.setMouseTouchCircle(new Point(0,0), gg.getCellSize()/3);
	}

	private Point scaleOffset(GGVector offset) {
		int scaleFactor = gg.getCellSize()/2;
		return new Point((int) (offset.x * scaleFactor), (int) (offset.y * scaleFactor));
	}
	
	public LinkedList<Location> getPossibleFillLocations() {
		LinkedList<Location> fillLocs = new LinkedList<Location>();
		Location loc = getLocation();
		fillLocs.add(loc);
		if (!(loc.y == 1 && direction == StrokeDirection.HORIZONTAL) 
				&& !( loc.x == 1 && direction == StrokeDirection.VERTICAL)) {
			if (direction == StrokeDirection.HORIZONTAL)
				fillLocs.add(new Location(loc.x, loc.y - 1));
			else 
				fillLocs.add(new Location(loc.x - 1, loc.y));
		}
		return fillLocs;
	}
	
	public StrokeDirection getStrokeDirection() {
		return direction;
	}

	public boolean isDrawn() {
		return getIdVisible() != 0;
	}
	
}
