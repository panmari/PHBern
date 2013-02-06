
import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;


public class Stroke extends Actor {

	private DotsnBoxes gg;
	private StrokeDirection direction;
	
	public Stroke(DotsnBoxes gg, Location location, StrokeDirection d) {
		super(true, "sprites/stroke.png", 3);
		this.gg = gg;
		this.direction = d;
		this.setLocationOffset(scaleOffset(d.getOffset()));
		this.setMouseTouchCircle(new Point(0,0), gg.getCellSize()/3);
	}
	
	public void reset() {
		this.turn(direction.ordinal()*90);
	}

	private Point scaleOffset(GGVector offset) {
		int scaleFactor = gg.getCellSize()/2;
		return new Point((int) (offset.x * scaleFactor), (int) (offset.y * scaleFactor));
	}

}
