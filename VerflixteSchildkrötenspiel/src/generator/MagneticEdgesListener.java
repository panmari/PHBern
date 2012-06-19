package generator;

import gg.CardPosition;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.Location.CompassDirection;

public class MagneticEdgesListener implements GGMouseListener, GGMouseTouchListener {

	private int cellSize;
	private DragHalfTurtle dragTurtle;
	private CardField gg;
	private Card[][] cardGrid;
	
	public MagneticEdgesListener(CardField gg, int cellSize, Card[][] cardGrid) {
		this.cellSize = cellSize;
		this.gg = gg;
		this.cardGrid = cardGrid;
	}

	/**
	 * TODO: remove ugliness
	 */
	@Override
	public boolean mouseEvent(GGMouse mouse) {
		Location loc = gg.toLocation(mouse.getX(), mouse.getY());
		if (dragTurtle != null) {
			if (isInTurtleGrid(loc)) {
				int offsetx = mouse.getX() - loc.getX() * cellSize;
				int offsety = mouse.getY() - loc.getY() * cellSize;

				if (offsetx < offsety) {
					if (cellSize - offsetx < offsety) {
						dragTurtle.setLocationWithinCard(loc, CardPosition.UP);
					} else {
						dragTurtle.setLocationWithinCard(loc, CardPosition.LEFT);
					}
				} else if (cellSize - offsetx < offsety) {
					dragTurtle.setLocationWithinCard(loc, CardPosition.RIGHT);
				} else {
					dragTurtle.setLocationWithinCard(loc, CardPosition.DOWN);
				}
			} else {
				dragTurtle.setLocation(new Location(-1, -1)); // out of sight }
			}
			gg.refresh();
		}
		return true;
	}

	private boolean isInTurtleGrid(Location loc) {
		return loc.x < 3 && loc.y < 3;
	}
	
	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		switch (mouse.getEvent()) {
		case GGMouse.lPress:
			dragTurtle = ((DragHalfTurtle) actor).clone();
			gg.addActorNoRefresh(dragTurtle, actor.getLocation());
			break;
		case GGMouse.lRelease:
			Location loc = gg.toLocationInGrid(mouse.getX(), mouse.getY());
			cardGrid[loc.x][loc.y].setTurtle(dragTurtle);
			dragTurtle = null;
			gg.refresh();
			break;
		}
	}
}
