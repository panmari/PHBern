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


public class BoxGame extends GameGrid implements GGMouseTouchListener{


	/**
	 * If set to true, an arbitrary grid size can be chosen at the start of the game.
	 */
	private static final boolean customizableGrid = false;
	/**
	 * For every location of the valid grid, the strokes surrounding it are saved in this hashtable.
	 * It is then used for look up after a new stroke is drawn.
	 */
	Hashtable<Location, LinkedList<Stroke>> BoxMap = new Hashtable<Location, LinkedList<Stroke>>();
	private Player currentPlayer;
	private Player[] players = new Player[2];
	private static int playerCounter = 0;
	
	public BoxGame(int height, int width) {
		super(width + 2, height + 2, 75, Color.WHITE, false);
		getBg().clear(Color.WHITE);
		players[0] = new Player(Color.BLUE, "Blue");
		players[1] = new Player(Color.RED, "Red");
		currentPlayer = players[0]; //blue begins;
		for (int x = 1; x < getNbHorzCells(); x++) {
			for (int y = 1; y < getNbVertCells(); y++) {
				Location loc = new Location(x, y);
				BoxMap.put(loc, new LinkedList<Stroke>());
				for (StrokeDirection d: StrokeDirection.values()) {
					//prevent loop from drawing unnecessary strokes
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
		setTitle("The box game -- www.java-online.ch"); 
		show();
	}
		
	public static void main(String[] args) {
		int height = 3;
		int width = 3;
		if (customizableGrid) {
			height = new GGInputInt("Height", "Choose the height of the grid.").show();
			width = new GGInputInt("Width", "Choose the width of the grid.").show();
		}
		
		new BoxGame(height, width);
	}
	
	
	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		Stroke s = (Stroke) actor;
		if (s.isDrawn()) 
			return;
		switch (mouse.getEvent()) {
			case GGMouse.enter:
				s.show(1 + currentPlayer.id); //important, that not s.draw is called!
				break;
			case GGMouse.leave:
				s.show(0);
				break;
			case GGMouse.lClick:
				s.draw(currentPlayer.id);
				boolean nextPlayer = true;
				for (Location loc: s.getPossibleFillLocations()) {
					if (players[currentPlayer.id].tryToFillBoxes(loc))
						nextPlayer = false;
				}
				if (nextPlayer)
					currentPlayer = currentPlayer.nextPlayer();
				updateStatusText();
				break;
		}
		refresh();
	}

	private void updateStatusText() {
		String msg = players[0].getLabelledScore() + " vs " + players[1].getLabelledScore();
		if (Stroke.allDrawn())
			msg = "Final Score -- " + msg;
		else msg = msg + " -- current Player is " + currentPlayer;
		setStatusText(msg);
	}

	private boolean outOfValidGrid(Location loc) {
		return loc.y >= getNbVertCells() - 1 || loc.x >= getNbHorzCells() -1 
				|| loc.y < 1 || loc.x < 1;
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
		
		public Player nextPlayer() {
			return players[(id + 1) % playerCounter];
		}
		public String getLabelledScore() {
			return name + ": " + score;
		}
		
		/**
		 * Player tries to fill out the given location with own color, but first checks if it's surrounded
		 * by strokes. 
		 * @param loc
		 * @return true if the given location was filled
		 */
		private boolean tryToFillBoxes(Location loc) {
			if (outOfValidGrid(loc))
				return false;
			for (Stroke s: BoxMap.get(loc))
				if (!s.isDrawn())
					return false;
			getPanel().fillCell(loc, color);
			score++;
			return true;
		}
	}
}
