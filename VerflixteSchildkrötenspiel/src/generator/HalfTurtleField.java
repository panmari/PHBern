package generator;

import java.util.List;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class HalfTurtleField extends GameGrid {
	
	private CardField cardField;
	private List<DragHalfTurtle> availableTurtles;

	public HalfTurtleField(CardField cardField, List<DragHalfTurtle> availableTurtles) {
		super(2, 6, 82, false);		
		this.cardField = cardField;
		this.availableTurtles = availableTurtles;
		initiateTurtles();
		doRun();
		show();
	}
	
	
	
	private void initiateTurtles() {
		int x = 0;
		int y = 0;
		for (DragHalfTurtle ht: availableTurtles) {
			addActor(ht, new Location(x,y));
			y += x;
			x = x % 2;
		}
	}



	public void act() {
		setPosition(cardField.getPosition().x + 3*164, cardField.getPosition().y);
	}
	
}
