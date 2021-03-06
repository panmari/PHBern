
import java.awt.Point;
import java.util.LinkedList;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;


public class Stroke extends Actor {

	private GameGrid gg;
	private StrokeDirection direction;
	private boolean drawn;
	private int id;
	private static int drawnStrokes;
	private static int strokeCounter = 0;
	
	/**
	 * The sprite id corresponds to the player the stroke belongs to -1
	 * @param gg
	 * @param d
	 */
	public Stroke(GameGrid gg, StrokeDirection d) {
		super(true, "sprites/strokeBoarder.png", 3);
		this.id = strokeCounter++;
		this.gg = gg;
		this.direction = d;
		this.drawn = false;
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
		if (loc.y != 1 && direction == StrokeDirection.HORIZONTAL) 
			fillLocs.add(new Location(loc.x, loc.y - 1));
		if (loc.x != 1 && direction == StrokeDirection.VERTICAL)
			fillLocs.add(new Location(loc.x - 1, loc.y));
		return fillLocs;
	}
	
	public StrokeDirection getStrokeDirection() {
		return direction;
	}
	
	public void draw(int playerId) {
		drawnStrokes++;
		drawn = true;
		show(1 + playerId);
	}

	public boolean isDrawn() {
		return drawn;
	}

	public static boolean allDrawn() {
		return strokeCounter == drawnStrokes;
	}
	
	public int getId() {
		return id;
	}
	
	public String toString() {
		return getLocation() + " " + direction;
	}
}
