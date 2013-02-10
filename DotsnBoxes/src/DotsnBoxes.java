import java.awt.Color;
import java.awt.Point;
import java.util.Hashtable;
import java.util.LinkedList;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGInputInt;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;


public class DotsnBoxes extends GameGrid implements GGMouseTouchListener{


	/**
	 * For every location of the valid grid, the strokes surrounding it are saved in this hashtable.
	 * It is then used for look up after a new stroke is drawn.
	 */
	Hashtable<Location, LinkedList<Stroke>> BoxMap = new Hashtable<Location, LinkedList<Stroke>>();
	
	private int currentPlayerId = 0;
	private Player[] players = new Player[2];

	private Stroke activeStroke;
	private static int playerCounter = 0;
	
	public DotsnBoxes(int height, int width) {
		super(width + 2, height + 2, 50, Color.WHITE, false);
		getBg().clear(Color.WHITE);
		players[0] = new Player(Color.BLUE, "Blue");
		players[1] = new Player(Color.RED, "Red");
		for (int x = 1; x < getNbHorzCells(); x++) {
			for (int y = 1; y < getNbVertCells(); y++) {
				Location loc = new Location(x, y);
				BoxMap.put(loc, new LinkedList<Stroke>());
				for (StrokeDirection d: StrokeDirection.values()) {
					//prevent program from drawing unnecessary strokes
					if (y == getNbVertCells() - 1 && d == StrokeDirection.VERTICAL
							|| x == getNbHorzCells() - 1 && d == StrokeDirection.HORIZONTAL)
						continue;
					Stroke s = new Stroke(this, d);
					addActorNoRefresh(s, new Location(x,y));
					s.addMouseTouchListener(this, GGMouse.lClick | GGMouse.enter | GGMouse.leave);
					for (Location l: s.getPossibleFillLocations())
						BoxMap.get(l).add(s);
				}
			}
		}
		addStatusBar(20);
		setStatusText("Click on an edge to start");
		show();
	}
		
	public static void main(String[] args) {
		int height = new GGInputInt("Height", "Choose the height of the grid.").show();
		int width = new GGInputInt("Width", "Choose the width of the grid.").show();
		new DotsnBoxes(height, width);
	}
	
	
	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		Stroke s = (Stroke) actor;
		if (!s.isDrawn()) {
			if (mouse.getEvent() == GGMouse.enter)
			{
				s.show(1 + currentPlayerId);
			} else if (mouse.getEvent() == GGMouse.leave)
				s.show(0);
		} 
		if (mouse.getEvent() == GGMouse.lClick) {
			s.draw(currentPlayerId);
			boolean nextPlayer = true;
			for (Location loc: s.getPossibleFillLocations()) {
				if (fillBoxes(loc))
					nextPlayer = false;
			}
			if (nextPlayer)
				currentPlayerId = (currentPlayerId + 1) % playerCounter;
			setStatusText(players[0].getLabelledScore() + " vs " + players[1].getLabelledScore() +
					", current Player is " + players[currentPlayerId] );
		}
		refresh();
	}
	
	/**
	 * Returns true if a box was filled
	 * @param loc
	 * @return
	 */
	private boolean fillBoxes(Location loc) {
		if (outOfValidGrid(loc))
			return false;
		for (Stroke s: BoxMap.get(loc))
			if (!s.isDrawn())
				return false;
		getPanel().fillCell(loc, players[currentPlayerId].color);
		players[currentPlayerId].score++;
		return true;
	}

	private boolean outOfValidGrid(Location loc) {
		return loc.y >= getNbVertCells() - 1 || loc.x >= getNbHorzCells() -1;
	}
	
	class Player {
		private int id;
		private Color color;
		private int score;
		private String name;
		
		public Player(Color color, String name) {
			this.name = name;
			this.id = playerCounter++;
			this.color = color;
			this.score = 0;
		}
		
		public String toString() {
			return name;
		}
		
		public String getLabelledScore() {
			return name + ": " + score;
		}
	}
}
