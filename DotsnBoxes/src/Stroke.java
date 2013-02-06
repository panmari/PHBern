
import java.awt.Point;
import java.util.LinkedList;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;


public class Stroke extends Actor {

	private DotsnBoxes gg;
	private StrokeDirection direction;
	
	public Stroke(DotsnBoxes gg, StrokeDirection d) {
		super(true, "sprites/stroke.png", 2);
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
	
	public String toString() {
		//TODO: better toString
		return super.toString();
	}
	
	public LinkedList<Location> getPossibleFillLocations() {
		LinkedList<Location> fillLocs = new LinkedList<Location>();
		fillLocs.add(getLocation());
		if (direction == StrokeDirection.VERTICAL)
			fillLocs.add(new Location(getX() - 1, getY()));
		else fillLocs.add(new Location(getX(), getY() - 1));
		return fillLocs;
	}
	
	public StrokeDirection getStrokeDirection() {
		return direction;
	}

	public boolean isDrawn() {
		return getIdVisible() == 1;
	}
}
