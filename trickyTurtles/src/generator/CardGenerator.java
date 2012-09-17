package generator;

import java.util.LinkedList;
import java.util.List;

public class CardGenerator {

	private List<DragHalfTurtle> availableTurtles;
	private static GeneratorCardGrid cardField;

	public CardGenerator() {
		initiateTurtles();
		cardField = new GeneratorCardGrid(availableTurtles);
	}
	
	private void initiateTurtles() {
		availableTurtles = new LinkedList<DragHalfTurtle>();
		availableTurtles.add(new DragHalfTurtle("bf", "sprites/blau_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("bb", "sprites/blau_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("gf", "sprites/gruen_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("gb", "sprites/gruen_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("rf", "sprites/braun_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("rb", "sprites/braun_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("yf", "sprites/br_bl_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("yb", "sprites/br_bl_hinten.png"));	
	}
	
	public static void main(String[] args) {
		new CardGenerator();
	}

	public static boolean showGeneratorWindow() {
		if (cardField == null)
			return false;
		else {
			cardField.show();
			return true;
		}
	}

}
