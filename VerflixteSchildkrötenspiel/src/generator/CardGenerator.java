package generator;

import java.util.LinkedList;
import java.util.List;

public class CardGenerator {

	List<DragHalfTurtle> availableTurtles;
	
	public CardGenerator() {
		CardField cardField = new CardField();
		initiateTurtles();
		HalfTurtleField htf = new HalfTurtleField(cardField, availableTurtles);
		cardField.getFrame().toFront();
		cardField.getFrame().repaint();
	}
	
	private void initiateTurtles() {
		availableTurtles = new LinkedList<DragHalfTurtle>();
		availableTurtles.add(new DragHalfTurtle("bf", "sprites/blau_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("bh", "sprites/blau_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("gf", "sprites/gruen_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("gh", "sprites/gruen_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("rf", "sprites/braun_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("rb", "sprites/braun_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("yf", "sprites/br_bla_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("yb", "sprites/br_bla_hinten.png"));	
	}
	
	public static void main(String[] args) {
		new CardGenerator();
	}
	
}
