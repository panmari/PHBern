package generator;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.Location.CompassDirection;

public class MagneticEdgesListener implements GGMouseListener, GGMouseTouchListener {

	private int cellSize;
	DragHalfTurtle dragTurtle;
	private CardField gg;
	
	public MagneticEdgesListener(CardField gg, int cellSize) {
		this.cellSize = cellSize;
		this.gg = gg;
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
						dragTurtle.setLocationWithinCard(loc, CompassDirection.NORTH);
					} else {
						dragTurtle.setLocationWithinCard(loc, CompassDirection.WEST);
					}
				} else if (cellSize - offsetx < offsety) {
					dragTurtle.setLocationWithinCard(loc, CompassDirection.EAST);
				} else {
					dragTurtle.setLocationWithinCard(loc, CompassDirection.SOUTH);
				}
			} else {
				dragTurtle.setLocation(new Location(-1, -1)); // out of sight }
			}
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
			dragTurtle.setPixelLocation(actor.getPixelLocation());
			break;
		case GGMouse.lRelease:
			dragTurtle = null;
			break;
		}
	}
}
