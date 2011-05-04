
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

public class GGLabyrinth2 {
	private final int nbHorzCells = 31; // must be odd
	private final int nbVertCells = 31; // ditto
	private final int cellSize = 18;

	public GGLabyrinth2() {
		GameGrid gg = new GameGrid(nbHorzCells, nbVertCells, cellSize, false);
		GGMaze maze = drawMaze(gg);
		Bug2 sbug = new Bug2(gg);
		gg.addActor(sbug, maze.getStartLocation());
		gg.show();
		sbug.startSearch();
	}

	private GGMaze drawMaze(GameGrid gg) {
		GGBackground bg = gg.getBg();
		GGMaze maze = new GGMaze(nbHorzCells, nbVertCells);
		for (int x = 0; x < nbHorzCells; x++)
			for (int y = 0; y < nbVertCells; y++) {
				Location location = new Location(x, y);
				if (maze.isWall(location))
					bg.fillCell(location, Color.black);
				else
					bg.fillCell(location, Color.white);
			}
		return maze;
	}

	public static void main(String[] args) {
		new GGLabyrinth2();
	}
}

// ------------------class Bug ------------------------------------
class Bug2 extends Actor {
	private final Location startLocation = new Location(0, 1);
	private final Location exitLocation;
	private ArrayList<Location> visitedLocations;
	private Location previousLoc = startLocation;
	private GameGrid gg;

	public Bug2(GameGrid gg) {
		super(true, "sprites/smallbug.gif"); // Rotatable
		exitLocation = new Location(gg.getNbHorzCells() - 1, gg
				.getNbVertCells() - 2);
		visitedLocations = new ArrayList<Location>();
		this.gg = gg;
	}

	public void startSearch() {
		searchPath(startLocation, 0);
	}

	private void searchPath(Location loc, int dist) {
		gg.setPaintOrder(Bug2.class, TextActor.class);
		gg.refresh();
		if (visitedLocations.contains(exitLocation)
				|| visitedLocations.contains(loc))
			return;
		else {
			visitedLocations.add(loc);
			setLocationFacing(loc);
			if (loc.equals(exitLocation))
				return;
			else {
				// Aktuelle Zelle markieren und beschriften
				delay(500);
				TextActor distMark = new TextActor("" + dist);
				distMark.setLocationOffset(new Point(-7, 0));
				gg.addActor(distMark, loc);
				
				// Naechste Zelle bestimmen (rekursiv)
				if (canMove(new Location(loc.x, loc.y - 1))) // up
					searchPath(new Location(loc.x, loc.y - 1), dist + 1);
				if (canMove(new Location(loc.x - 1, loc.y))) // left
					searchPath(new Location(loc.x - 1, loc.y), dist + 1);
				if (canMove(new Location(loc.x, loc.y + 1))) // down
					searchPath(new Location(loc.x, loc.y + 1), dist + 1);
				if (canMove(new Location(loc.x + 1, loc.y))) // right
					searchPath(new Location(loc.x + 1, loc.y), dist + 1);

				// Falls das Ziel auf diesem Weg nicht erreicht, rotes X
				if (!visitedLocations.contains(exitLocation)) {
					delay(500); 
					gg.removeActorsAt(loc, TextActor.class); //delete Number
					TextActor wrongMark = new TextActor("x", Color.red, 
							Color.white, new Font("SansSerif", Font.PLAIN, 12));
					distMark.setLocationOffset(new Point(-8, 0));
					gg.addActor(wrongMark, loc);
					setLocationFacing(loc);
				}
			}
		}
	}

	private void setLocationFacing(Location loc) {
		setDirection(previousLoc.getCompassDirectionTo(loc));
		previousLoc = loc;
		setLocation(loc);
	}

	private boolean canMove(Location location) {
		Color c = getBackground().getColor(location);
		return (!c.equals(Color.black));
	}
}
