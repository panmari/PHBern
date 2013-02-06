import java.awt.Color;
import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;


public class DotsnBoxes extends GameGrid implements GGMouseTouchListener{


	
	public DotsnBoxes() {
		super(8, 12, 50, Color.RED, false);
		getBg().clear(Color.WHITE);
		show();
		for (int x = 0; x < getNbHorzCells(); x++) {
			for (int y = 0; y < getNbVertCells(); y++) {
				for (StrokeDirection d: StrokeDirection.values()) {
					Stroke s = new Stroke(this, new Location(x,y), d);
					s.addMouseTouchListener(this, GGMouse.lClick);
					addActor(s, new Location(x,y));
				}
			}
		}
		refresh();
		doRun();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DotsnBoxes();
	}
	
	boolean isMiddleOfTile(int pixel) {
		int offset = pixel % getCellSize();
		return  offset > getCellSize() / 4 && offset < getCellSize()*3/4;
	}
	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		actor.show(1);
		
	}
	
}
