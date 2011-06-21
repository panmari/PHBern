package halma;

import java.util.ArrayList;

import ch.aplu.jgamegrid.Location;

public class HalmaPlayer {
	private HalmaColor color;
	private Halma halmaBoard;
	private ArrayList<Location> startLocations;
	private ArrayList<Location> endLocations;
	
	public HalmaPlayer(Halma halmaBoard, HalmaColor color, 
			ArrayList<Location> startLocations, ArrayList<Location> endLocations) {
		this.color = color;
		this.startLocations = startLocations;
		this.endLocations = endLocations;
	}
	
	public void initializeStones() {
		for (Location loc : startLocations)
			halmaBoard.addActor(new HalmaStone(color), loc);
	}

	/**
	 * Player wins if every Location of endLocation is 
	 * occupied by a stone of his color
	 * @return
	 */
	public boolean isWinner() {
		for (Location loc : endLocations) {
			HalmaStone hs = halmaBoard.getHalmaStoneAt(loc);
			if (hs == null || hs.getColor() != this.color)
				return false;
		}
		return true;
	}
}
