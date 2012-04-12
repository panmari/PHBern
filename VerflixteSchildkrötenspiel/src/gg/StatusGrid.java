package gg;

import java.util.LinkedList;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class StatusGrid extends GameGrid {

	private final static double factor = 0.3;
	private LinkedList<Actor> tinyCardSet;
	
	public StatusGrid(LinkedList<TurtleCard> cardSet) {
		super(3,3,(int) (200*factor), false);
		setPosition(0, 0);
		setActEnabled(false);
		tinyCardSet = new LinkedList<Actor>();
		for(TurtleCard tc: cardSet) {
			Actor a = new Actor(tc.getScaledImage(factor, 0));
			tinyCardSet.add(a);
			addActor(a, getNextEmptyLocation());
		}
		show();
	}

	private Location getNextEmptyLocation() {
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 3; x++) {
				Location loc = new Location(x, y);
				if (getActorsAt(loc).isEmpty())
					return loc;
			}
		return null;
	}

	public void hideCard(int id) {
		tinyCardSet.get(id).hide();
		refresh();
	}

	public void showCard(int id) {
		tinyCardSet.get(id).show();
		refresh();
	}
}
