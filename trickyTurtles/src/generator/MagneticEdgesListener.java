package generator;

import gg.CardPosition;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.Location;

/**
 * There are only 4 possible Locations for a DragHalfTurtle. This Listener automatically
 * puts them to the closest of them.
 * If the drag occurs outside of the turtlegrid, the pixellocation of the turtle is set
 * to the mouse location to make it clear it's being dragged.
 */
public class MagneticEdgesListener implements GGMouseListener, GGMouseTouchListener {

	private int cellSize;
	private DragHalfTurtle dragTurtle;
	private GeneratorCardGrid gg;
	private Card[][] cardGrid;
	
	public MagneticEdgesListener(GeneratorCardGrid gg, int cellSize, Card[][] cardGrid) {
		this.cellSize = cellSize;
		this.gg = gg;
		this.cardGrid = cardGrid;
	}

	/**
	 * See class description
	 */
	@Override
	public boolean mouseEvent(GGMouse mouse) {
		if (dragTurtle == null)
			return false;
		Location loc = gg.toLocation(mouse.getX(), mouse.getY());
		switch (mouse.getEvent()) {
		case GGMouse.lDrag:
			if (isInTurtleGrid(loc)) {
				int offsetx = mouse.getX() - loc.getX() * cellSize;
				int offsety = mouse.getY() - loc.getY() * cellSize;
				if (offsetx < offsety) {
					if (cellSize - offsetx < offsety) {
						dragTurtle.setLocationWithinCard(loc, CardPosition.DOWN);
					} else {
						dragTurtle.setLocationWithinCard(loc, CardPosition.LEFT);
					}
				} else if (cellSize - offsetx < offsety) {
					dragTurtle.setLocationWithinCard(loc, CardPosition.RIGHT);
				} else {
					dragTurtle.setLocationWithinCard(loc, CardPosition.UP);
				}
			} else {
				if (gg.isInGrid(loc)) {
					dragTurtle.setDirection(0);
					dragTurtle.setPixelLocation(new Point(mouse.getX(), mouse.getY()));
				}
			}
			break;
		case GGMouse.lRelease:
			if (isInTurtleGrid(loc))
				cardGrid[loc.x][loc.y].setTurtle(dragTurtle);
			else
				dragTurtle.removeSelf();
			dragTurtle = null;
			break;
		}
		gg.refresh();
		return true;
	}

	/**
	 * Only the left part of the gamegrid is valid for positioning HalfTurtles.
	 * @param loc
	 * @return true, if the given Location is valid for HalfTurtles
	 */
	private boolean isInTurtleGrid(Location loc) {
		return loc.x >= 0 && loc.y >= 0 && loc.x < 3 && loc.y < 3;
	}
	
	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		switch (mouse.getEvent()) {
		case GGMouse.lPress:
			dragTurtle = ((DragHalfTurtle) actor).clone();
			gg.addActorNoRefresh(dragTurtle, actor.getLocation());
			break;
		}
	}
}
