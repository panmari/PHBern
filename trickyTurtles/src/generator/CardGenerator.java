package generator;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import solver.CardPosition;

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
	public void initiateWithRandomScenario() {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				for (CardPosition pos: CardPosition.values()) {
					DragHalfTurtle randomHalfTurtle = getRandomHalfTurtle();
					addHalfTurtleToGrid(randomHalfTurtle, pos, x, y);
				}
		generatorCardGrid.refresh();
	}

	public void initiateWithSolvableScenario() {
		Card[][] grid = generatorCardGrid.getCardGrid();
		Collections.sort(availableTurtles);
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				for (CardPosition pos: CardPosition.values()) {
					DragHalfTurtle existingHt, ht;
					if (x - 1 >= 0 && pos == CardPosition.LEFT) {
						existingHt = grid[x - 1][y].getTurtleAt(CardPosition.RIGHT);
						ht = getCounterpartHalfTurtle(existingHt);
					} else if (y - 1 >= 0 && pos == CardPosition.UP) {
						existingHt = grid[x][y - 1].getTurtleAt(CardPosition.DOWN);
						ht = getCounterpartHalfTurtle(existingHt);
					} else ht = getRandomHalfTurtle();

					addHalfTurtleToGrid(ht, pos, x, y);
				}
		generatorCardGrid.refresh();
	}
	
	/**
	 * Asummes <code>availableTurtles</code> are ordered by id.
	 * @param existingHt
	 * @return
	 */
	private DragHalfTurtle getCounterpartHalfTurtle(DragHalfTurtle existingHt) {
		return availableTurtles.get(existingHt.getIdOfCounterpart()).clone();
	}

	private DragHalfTurtle getRandomHalfTurtle() {
		int randomInt = (int) (Math.random()*availableTurtles.size());
		return availableTurtles.get(randomInt).clone();
	}
	
	private void addHalfTurtleToGrid(DragHalfTurtle ht, CardPosition pos, int x, int y) {
		Location loc = new Location(x,y);
		generatorCardGrid.addActor(ht, loc);
		ht.setLocationWithinCard(loc, pos);
		generatorCardGrid.getCardGrid()[x][y].setTurtle(ht);
	}
}
