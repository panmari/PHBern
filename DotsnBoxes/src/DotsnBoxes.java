import java.awt.Color;

import Stroke.StrokeDirection;

import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;


public class DotsnBoxes extends GameGrid implements GGMouseListener{


	
	public DotsnBoxes() {
		super(8, 12, 50, Color.GRAY, false);
		getBg().clear(Color.WHITE);
		addMouseListener(this, GGMouse.lClick);
		show();
		for (int x = 0; x < getNbHorzCells(); x++) {
			for (int y = 0; y < getNbVertCells(); y++) {
				for (StrokeDirection d: StrokeDirection.values()) {
					new Stroke(this, new Location(x,y), d);
				}
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DotsnBoxes();
	}
	@Override
	public boolean mouseEvent(GGMouse mouse) {
		if (isMiddleOfTile(mouse.getX())) {
			this.toLocation(mouse.getX(), mouse.getY());
		}
		else if (isMiddleOfTile(mouse.getY())) {
			
		}
		return true;
	}

	boolean isMiddleOfTile(int pixel) {
		int offset = pixel % getCellSize();
		return  offset > getCellSize() / 4 && offset < getCellSize()*3/4;
	}
	
}
