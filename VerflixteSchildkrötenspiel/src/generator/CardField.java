package generator;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.Location.CompassDirection;

public class CardField extends GameGrid {

	private List<DragHalfTurtle> availableTurtles;
	private MagneticEdgesListener mouseListener;

	CardField(List<DragHalfTurtle> availableTurtles) {
		super(4, 3, 164, Color.gray, false);
		this.availableTurtles = availableTurtles;
		this.mouseListener = new MagneticEdgesListener(this, cellSize);
		setBgColor(Color.white);
		setTitle("Turtles Generator");
		initiateTurtles();
		addMouseListener(mouseListener, GGMouse.lDrag);
		show();
	}

	private void initiateTurtles() {
		int x = 0;
		int y = 0;
		boolean newRow = true;
		for (DragHalfTurtle ht : availableTurtles) {
			addActor(ht, new Location(0, 0));
			ht.addMouseTouchListener(mouseListener, GGMouse.lPress | GGMouse.lRelease);
			if (newRow) {
				x = 535;
				y = y + 100;
				newRow = false;
			} else {
				x = x + 80;
				newRow = true;
			}
			ht.setPixelLocation(new Point(x, y));
		}
	}

}
