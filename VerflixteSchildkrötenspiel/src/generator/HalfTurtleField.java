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

public class HalfTurtleField extends GameGrid {
	
	private CardField cardField;
	private List<DragHalfTurtle> availableTurtles;

	public HalfTurtleField(CardField cardField, List<DragHalfTurtle> availableTurtles, TurtleDragger turtleDragger) {
		super(2, 6, 82, Color.gray, false);
		setBgColor(Color.white);
		this.cardField = cardField;
		this.availableTurtles = availableTurtles;
		initiateTurtles();
		addMouseListener(turtleDragger, GGMouse.lPress | GGMouse.lDrag | GGMouse.lRelease);
		doRun();
		show();
	}
	
	private void initiateTurtles() {
		int x = 0;
		int y = 0;
		for (DragHalfTurtle ht: availableTurtles) {
			addActor(ht, new Location(x,y));
			y += x;
			x = (x + 1) % 2;
		}
	}



	public void act() {
		setPosition(cardField.getPosition().x + 3*164, cardField.getPosition().y);
	}
	
}
