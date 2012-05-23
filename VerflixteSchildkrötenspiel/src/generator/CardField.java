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
		setTitle("Turtles");
		show();
	}
	
	private void initiateTurtles() {
		int x = 0;
		int y = 0;
		for (DragHalfTurtle ht: availableTurtles) {
			addActor(ht, new Location(x,y));
			ht.addMouseTouchListener(this, GGMouse.lPress | GGMouse.lDrag | GGMouse.lRelease);
			y += x;
			x = (x + 1) % 2;
		}
	}

	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		switch (mouse.getEvent()) {
		case GGMouse.lPress:
			dragTurtle = getOneActorAt(toLocationInGrid(mouse.getX(), mouse.getY()));
			break;
		case GGMouse.lDrag:
			if (dragTurtle != null) {
				dragTurtle.setPixelLocation(new Point(mouse.getX(), mouse.getY()));
				//refresh();
			}
			break;
		case GGMouse.lRelease:
			dragTurtle = null;
			break;
		}
	}
			
}
