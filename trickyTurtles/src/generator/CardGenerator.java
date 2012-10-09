package generator;

import gg.CardPosition;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.aplu.jgamegrid.Location;

public class CardGenerator {

	private List<DragHalfTurtle> availableTurtles;
	private static GeneratorCardGrid generatorCardGrid;

	public CardGenerator() {
		initiateTurtles();
		generatorCardGrid = new GeneratorCardGrid(availableTurtles);
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

	/**
	 * @return true, if program should terminate, false if not
	 */
	public static boolean showGeneratorWindow() {
		if (generatorCardGrid == null)
			return true;
		else {
			generatorCardGrid.show();
			return false;
		}
	}
	
	/**
	 * Fills the grid totally random. It is not given that there is a solution
	 * for this turtle set. Mainly for debugging purposes.
	 */
	public void initiateWithTurtles() {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				for (CardPosition pos: CardPosition.values()) {
				    Collections.shuffle(availableTurtles);
					DragHalfTurtle randomHalfTurtle = availableTurtles.get(0).clone();
					Location loc = new Location(x,y);
					generatorCardGrid.addActor(randomHalfTurtle, loc);
					randomHalfTurtle.setLocationWithinCard(loc, pos);
					generatorCardGrid.getCardGrid()[loc.x][loc.y].setTurtle(randomHalfTurtle);
				}
		generatorCardGrid.refresh();
	}
}
