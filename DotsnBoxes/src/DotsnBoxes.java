import java.awt.Color;
import java.awt.Point;
import java.util.Hashtable;
import java.util.LinkedList;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;


public class DotsnBoxes extends GameGrid implements GGMouseTouchListener{


	Hashtable<Location, LinkedList<Stroke>> BoxMap = new Hashtable<Location, LinkedList<Stroke>>();
	
	private int currentPlayerId = 0;
	private Player[] players = new Player[2];
	private static int playerCounter = 0;
	
	public DotsnBoxes() {
		super(5, 5, 50, Color.WHITE, false);
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
					s.addMouseTouchListener(this, GGMouse.lClick);
					for (Location l: s.getPossibleFillLocations())
						BoxMap.get(l).add(s);
				}
			}
		}
		setStatusText("Click on an edge to start");
		addStatusBar(20);
		show();
	}
		
	public static void main(String[] args) {
		new DotsnBoxes();
	}
	
	
	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		Stroke s = (Stroke) actor;
		if (s.isDrawn()) //already drawn
			return;
		s.show(1 + currentPlayerId);
		boolean nextPlayer = true;
		for (Location loc: s.getPossibleFillLocations()) {
			try {
				if (fillBoxes(loc))
					nextPlayer = false;
			} catch (NullPointerException e) {
				//don't care
			}
		}
		if (nextPlayer)
			currentPlayerId = (currentPlayerId + 1) % playerCounter;
		setStatusText("Score: " + players[0].score + " vs " + players[1].score + ", current Player is " + players[currentPlayerId] );
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
	}
}
