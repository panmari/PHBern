package generator;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class CardField extends GameGrid implements GGMouseTouchListener{
	
	private List<DragHalfTurtle> availableTurtles;
	Actor dragTurtle;
	
	CardField(List<DragHalfTurtle> availableTurtles) {
		super(4,3,164, Color.gray, false);
		this.availableTurtles = availableTurtles;
		setBgColor(Color.white);
		setTitle("Turtles Generator");
		initiateTurtles();
		show();
		doRun();
	}
	
	private void initiateTurtles() {
		int x = 0;
		int y = 0;
		boolean newRow = true;
		for (DragHalfTurtle ht: availableTurtles) {
			addActor(ht, new Location(0, 0));	
			ht.addMouseTouchListener(this, GGMouse.lPress | GGMouse.lDrag | GGMouse.lRelease);
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

	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		switch (mouse.getEvent()) {
		case GGMouse.lPress:
			dragTurtle = actor;
			break;
		case GGMouse.lDrag:
			if (dragTurtle != null) {
				dragTurtle.setPixelLocation(new Point(mouse.getX(), mouse.getY()));
			}
			break;
		case GGMouse.lRelease:
			dragTurtle = null;
			break;
		}
	}
			
}
