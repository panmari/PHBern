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


	Hashtable<Integer, LinkedList<Stroke>> BoxMap = new Hashtable<Integer, LinkedList<Stroke>>();
	
	private int currentPlayer = 0;
	private Color[] colors = { Color.RED, Color.BLUE};
	private int[] scores = new int[2];
	
	public DotsnBoxes() {
		super(8, 12, 50, Color.WHITE, false);
		getBg().clear(Color.WHITE);
		for (int x = 1; x < getNbHorzCells(); x++) {
			for (int y = 1; y < getNbVertCells(); y++) {
				Location loc = new Location(x, y);
				BoxMap.put(makeHash(loc), new LinkedList<Stroke>());
				for (StrokeDirection d: StrokeDirection.values()) {
					if (y == getNbVertCells() - 1 && d == StrokeDirection.VERTICAL
							|| x == getNbHorzCells() - 1 && d == StrokeDirection.HORIZONTAL)
						continue;
					Stroke s = new Stroke(this, d);
					s.addMouseTouchListener(this, GGMouse.lClick);
					BoxMap.get(makeHash(loc)).add(s);
					//very hacky!
					if (!(y == 1 && d == StrokeDirection.HORIZONTAL)
							&& !( x == 1 && d == StrokeDirection.VERTICAL)) {
						if (d == StrokeDirection.HORIZONTAL)
							BoxMap.get(makeHash(new Location(x, y - 1))).add(s);
						else 
							BoxMap.get(makeHash(new Location(x - 1, y))).add(s);
					}
					addActorNoRefresh(s, new Location(x,y));
				}
			}
		}
		addStatusBar(20);
		show();
	}
	
	private Integer makeHash(Location loc) {
		return new Integer(loc.x*1000 + loc.y);
	}
	
	public static void main(String[] args) {
		new DotsnBoxes();
	}
	
	
	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		Stroke s = (Stroke) actor;
		if (s.isDrawn()) //already drawn
			return;
		s.show(1);
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
			currentPlayer = (currentPlayer + 1) % 2;
		setStatusText("Score: " + scores[0] + " vs " + scores[1] + ", current Player is " + currentPlayer);
		refresh();
	}
	/**
	 * Returns true if a box was filled
	 * @param loc
	 * @return
	 */
	private boolean fillBoxes(Location loc) {
		for (Stroke s: BoxMap.get(makeHash(loc)))
			if (s.getIdVisible() == 0)
				return false;
		getPanel().fillCell(loc, colors[currentPlayer]);
		scores[currentPlayer]++;
		return true;
	}
	
}
