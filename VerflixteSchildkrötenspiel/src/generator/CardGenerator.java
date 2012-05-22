package generator;

import java.util.LinkedList;
import java.util.List;

import ch.aplu.jgamegrid.GameGrid;

public class CardGenerator {

	List<DragHalfTurtle> availableTurtles;
	
	public CardGenerator() {
		CardField cardField = new CardField();
		initiateTurtles();
		new HalfTurtleField(cardField, availableTurtles);
	}
	
	private void initiateTurtles() {
		availableTurtles = new LinkedList<DragHalfTurtle>();
	}
	
	public static void main(String[] args) {
		new CardGenerator();
	}
	
}
