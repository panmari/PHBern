package halmaSquare;

import java.awt.Color;
import java.util.ArrayList;

import ch.aplu.jgamegrid.Location;

public class HalmaPlayer {
	private HalmaColor color;
	private Halma halmaBoard;
	private ArrayList<Location> startLocations;
	private ArrayList<Location> endLocations;
	
	public HalmaPlayer(Halma halmaBoard, HalmaColor color, 
			ArrayList<Location> startLocations, ArrayList<Location> endLocations) {
		this.halmaBoard = halmaBoard;
		this.color = color;
		this.startLocations = startLocations;
		this.endLocations = endLocations;
	}
	
	public void initializeStones() {
		for (Location loc : startLocations) {
			halmaBoard.getBg().fillCell(loc, Color.lightGray);
			halmaBoard.addActor(new HalmaStone(this), loc);
		}
		halmaBoard.getBg().drawGridLines(Color.black);
	}

	/**
	 * Player wins if every Location of endLocation is 
	 * occupied by a stone of his color.
	 * @return true if the current Player wins
	 */
	public boolean isWinner() {
		for (Location loc : endLocations) {
			HalmaStone hs = halmaBoard.getHalmaStoneOfCurrentPlayerAt(loc);
			if (hs == null || hs.getColor() != this.color)
				return false;
		}
		return true;
	}
	
	public String toString() {
		return color.name();
	}
	
	public ArrayList<Location> getAllLocations() {
		ArrayList<Location> allLocations = new ArrayList<Location>(startLocations);
		allLocations.addAll(endLocations);
		return allLocations;
	}
	
	public HalmaColor getColor() {
		return color;
	}
}
