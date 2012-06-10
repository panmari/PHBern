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

public class CardField extends GameGrid implements GGMouseTouchListener, GGMouseListener{
	
	private List<DragHalfTurtle> availableTurtles;
	DragHalfTurtle dragTurtle;
	
	CardField(List<DragHalfTurtle> availableTurtles) {
		super(4,3,164, Color.gray, false);
		this.availableTurtles = availableTurtles;
		setBgColor(Color.white);
		setTitle("Turtles Generator");
		initiateTurtles();
		addMouseListener(this, GGMouse.lDrag);
		show();
	}
	
	private void initiateTurtles() {
		int x = 0;
		int y = 0;
		boolean newRow = true;
		for (DragHalfTurtle ht: availableTurtles) {
			addActor(ht, new Location(0, 0));	
			ht.addMouseTouchListener(this, GGMouse.lPress | GGMouse.lRelease);
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
			dragTurtle = ((DragHalfTurtle) actor).clone();
			addActorNoRefresh(dragTurtle, actor.getLocation());
			dragTurtle.setPixelLocation(actor.getPixelLocation());
			break;
		case GGMouse.lRelease:
			dragTurtle = null;
			break;
		}
	}

	@Override
	public boolean mouseEvent(GGMouse mouse) {
		Location loc = toLocation(mouse.getX(), mouse.getY());
		if (dragTurtle != null && isInGrid(loc)) {
			int offsetx = mouse.getX() - loc.getX()*cellSize;
			int offsety = mouse.getY() - loc.getY()*cellSize;
			
			if (offsetx < offsety) {
				if (cellSize - offsetx < offsety) {
					dragTurtle.setLocationWithinCard(loc, CompassDirection.NORTH);
				}
				else {
				    dragTurtle.setLocationWithinCard(loc, CompassDirection.WEST);
				}
			}
			else if (cellSize - offsetx < offsety) {
				dragTurtle.setLocationWithinCard(loc, CompassDirection.EAST);
			}
			else {
			    dragTurtle.setLocationWithinCard(loc, CompassDirection.SOUTH);
			}
			refresh();
		}
		return true;
		
	}
			
}
