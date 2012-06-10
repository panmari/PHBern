package generator;

import java.util.LinkedList;
import java.util.List;

public class CardGenerator {

	List<DragHalfTurtle> availableTurtles;
	CardField cardField;

	public CardGenerator() {
		initiateTurtles();
		cardField = new CardField(availableTurtles);
	}
	
	private void initiateTurtles() {
		availableTurtles = new LinkedList<DragHalfTurtle>();
		availableTurtles.add(new DragHalfTurtle("bf", "sprites/blau_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("bb", "sprites/blau_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("gf", "sprites/gruen_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("gb", "sprites/gruen_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("rf", "sprites/braun_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("rb", "sprites/braun_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("yf", "sprites/br_bla_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("yb", "sprites/br_bla_hinten.png"));	
	}
	
	public static void main(String[] args) {
		new CardGenerator();
	}

}
