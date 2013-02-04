
import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;


public class Stroke extends Actor {

	private DotsnBoxes gg;
	private StrokeDirection direction;

	public static enum StrokeDirection {
		North, East, South, West
	}
	
	public Stroke(DotsnBoxes gg, Location location, StrokeDirection d) {
		this.gg = gg;
		this.direction = d;
		this.setMouseTouchCircle(new Point(0,0), gg.getCellSize()/3);
	}

}
