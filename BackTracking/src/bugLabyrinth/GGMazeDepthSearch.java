package bugLabyrinth;

import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

public class GGMazeDepthSearch extends GameGrid {

	private SearchingBug sbug;
	
	public GGMazeDepthSearch() {
		super(31, 31, 18, false);
		GGMaze maze = drawMaze();
		setTitle("Depth Search - executed by a bug!");
		sbug = new SearchingBug(this, maze.getStartLocation(), maze.getExitLocation());
		addActor(sbug, maze.getStartLocation());
		show();
		sbug.startSearch();
	}

	private GGMaze drawMaze() {
		GGBackground bg = getBg();
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
	
	public void reset() {
		this.removeActors(TextActor.class);
		System.out.println("Reseting");
		GGMaze maze = drawMaze();
		sbug.reset();
		sbug.setLocation(maze.getStartLocation());
		sbug.startSearch();
	}

	public static void main(String[] args) {
		new GGMazeDepthSearch();
	}
}

// ------------------class Bug ------------------------------------
class SearchingBug extends Actor {
	private final Location startLocation;
	private final Location exitLocation;
	private final int delayDuration = 50;
	private ArrayList<Location> visitedLocations;
	private Location previousLoc;
	private GameGrid gg;

	public SearchingBug(GameGrid gg, Location startLoc, Location exitLoc) {
		super(true, "sprites/smallbug.gif"); // Rotatable
	    this.startLocation = startLoc;
	    this.exitLocation = exitLoc;
	    previousLoc = startLocation;
	    visitedLocations = new ArrayList<Location>();
	    this.gg = gg;
	}

	public void startSearch() {
		searchPath(startLocation, 0);
		System.out.println("Found exit!");
		delay(5000); //wait 5 seconds, then reset
		gg.reset();
	}

	public void reset() {
		visitedLocations.clear();
	}
	
	private void searchPath(Location loc, int dist) {
		gg.setPaintOrder(SearchingBug.class, TextActor.class);
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
		delay(delayDuration); 
	}

	private boolean canMove(Location location) {
		Color c = getBackground().getColor(location);
		return (!c.equals(Color.black));
	}
}
