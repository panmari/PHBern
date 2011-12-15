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
	
	/**
	 * Puts a stone of the players own color on every Location
	 * of startLocations. Also fills these cells with lightGray.
	 */
	public void initializeStones() {
		//-------------- TASK: very easy to programmm ----------------
		for (Location loc : startLocations) {
			halmaBoard.getBg().fillCell(loc, Color.lightGray);
			halmaBoard.addActor(new HalmaStone(this), loc);
		}
		halmaBoard.getBg().drawGridLines(Color.black);
		//-------------- very easy to programmm ----------------
	}

	/**
	 * Player wins if every Location of endLocation is 
	 * occupied by a stone of his color.
	 * @return true if the current Player wins
	 */
	public boolean isWinner() {
		//----------------TASK: also easy to add  ---------------------
		for (Location loc : endLocations) {
			HalmaStone hs = halmaBoard.getHalmaStoneOfCurrentPlayerAt(loc);
			if (hs == null || hs.getColor() != this.color)
				return false;
		}
		return true;
		//--------------------------------------------------------
	}
	
	public String toString() {
		return color.name();
	}
	
	public HalmaColor getColor() {
		return color;
	}
}
