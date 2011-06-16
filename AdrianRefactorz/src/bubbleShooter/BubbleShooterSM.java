package bubbleShooter;
// Reversi.java

import ch.aplu.jgamegrid.*;

import java.awt.Point;
import java.util.*;

public class BubbleShooterSM extends GameGrid implements GGMouseListener {
	int nbOfBubbleColors = 5; // Zahl zwischen 1 - 5
	Location shootLoc = new Location(18, 36); // Location of the shooter
	ArrayList<Location> bubblePreviewLocations = new ArrayList<Location>(); 
	// Location-List of shooter and bubbles to come
	
	public BubbleShooterSM() {
		super(37, 38, 20, false);

		bubblePreviewLocations.add(shootLoc);
		for (int x = 27; x < 37; x += 2)
			bubblePreviewLocations.add(new Location(x, 36));
		for (Location loc : bubblePreviewLocations)
			addRandomBubble(loc);
		// initialize Bubble field:
		for (int y = 1; y < 6; y++) {
			for (int x = y; x < 37 - y; x += 2)
				addRandomBubble(new Location(x, 2 * y - 1));
		}
		addMouseListener(this, GGMouse.lPress);
		addActor(new Pointer(), shootLoc);
		setPaintOrder(Pointer.class, Bubble.class);
		show();
		setSimulationPeriod(30); // => 33 refreshes per second
		doRun();
	}

	public boolean mouseEvent(GGMouse mouse) {
		Bubble shootBubble = (Bubble) getOneActorAt(shootLoc, Bubble.class);
		shootBubble.shoot(new Point(mouse.getX(), mouse.getY()));
		refillBubbles();
		checkForGameOver();
		return true;
	}

	private void addRandomBubble(Location loc) {
		int rN = (int) (Math.random() * nbOfBubbleColors);
		addActor(new Bubble(rN, this), loc);
	}

	/** 
	 * Reloads the shooter and updates the BubbleList
	 */
	private void refillBubbles() {
		for (int b = 1; b < bubblePreviewLocations.size(); b++)
			getOneActorAt(bubblePreviewLocations.get(b)).setLocation(bubblePreviewLocations.get(b - 1));

		addRandomBubble(bubblePreviewLocations.get(bubblePreviewLocations.size() - 1));
	}

	/**
	 *  The game is over if only the shooter & the preview Bubbles are left. 
	 */
	private void checkForGameOver() {
		if (getNumberOfActors() <= 6) {
			addActor(new Actor("sprites/gameover.png"), new Location(18, 15));
			this.stopGameThread();
		}
	}

	public static void main(String[] args) {
		new BubbleShooterSM();
	}
}

/*------------------------------------------------------------------------------
 * Bubble
 */
class Bubble extends Actor {
	private GameGrid gg;
	private double vy, vx;
	private double x, y;
	private final double acceleration = 9.81, bubbleRadius = 20;
	
	public Bubble(int imgId, GameGrid gg) {
		super("sprites/peg.png", 6);
		this.gg = gg;
		show(imgId);
		setActEnabled(false);
	}
	
	public void shoot(Point mousePos) {
		x = getPixelLocation().x;
		y = getPixelLocation().y;
		int vx = mousePos.x - getPixelLocation().x;
		int vy = mousePos.y - getPixelLocation().y;
		double vectorLength = getPixelLocation().distance(mousePos);
		this.vx = vx/vectorLength;
		this.vy = vy/vectorLength;
		setActEnabled(true);
	}
	
	public void act() {
		// collision with wall:
		if (x - bubbleRadius < 0 || x + bubbleRadius > gg.getPgWidth()) 
			vx = -vx;	
		else if (y - bubbleRadius < 0 || y + bubbleRadius > gg.getPgHeight())
			vy = -vy;
		x = vx*acceleration+x;
		y = vy*acceleration+y;
		setPixelLocation(new Point((int)x, (int)y));
		areBubblesAroundMe();
	}
	
	//TODO: refactor this!
	public Location setValidLocation(Location location) {
		if (gg.getOneActorAt(new Location(location.x, location.y - 2)) != null)
			return new Location(location.x + 1, location.y);
		else if (gg.getOneActorAt(new Location(location.x - 1, location.y)) != null)
			return new Location(location.x + 1, location.y);
		else if (gg.getOneActorAt(new Location(location.x + 1, location.y)) != null)
			return new Location(location.x - 1, location.y);
		else if (gg.getOneActorAt(new Location(location.x, location.y - 1)) != null)
			return new Location(location.x + 1, location.y + 1);
		else if (gg.getOneActorAt(new Location(location.x - 1, location.y - 1)) != null)
			return new Location(location.x, location.y + 1);
		else if (gg.getOneActorAt(new Location(location.x + 1, location.y - 1)) != null)
			return new Location(location.x, location.y + 1);
		else if (gg.getOneActorAt(new Location(location.x - 2, location.y - 1)) != null)
			return new Location(location.x - 1, location.y + 1);
		else if (gg.getOneActorAt(new Location(location.x + 2, location.y - 1)) != null)
			return new Location(location.x + 1, location.y + 1);
		else
			return location;
	}
	
	public ArrayList<Bubble> getSameColorNeighbours(Bubble initialBubble) {
		ArrayList<Bubble> sameColorNeighbours = new ArrayList<Bubble>();
		Stack<Bubble> uncheckedNeighbours = new Stack<Bubble>();
		uncheckedNeighbours.push(initialBubble);
		while (!uncheckedNeighbours.isEmpty()) {
			Bubble checkBubble = uncheckedNeighbours.pop();
			for (Actor a: checkBubble.getNeighbours(2, Bubble.class)) {
				Bubble b = (Bubble) a;
				if (b.hasSameColorAs(initialBubble) &&
						!uncheckedNeighbours.contains(b) &&
						!sameColorNeighbours.contains(b))
					uncheckedNeighbours.push(b);
			}
			sameColorNeighbours.add(checkBubble);
		}
		return sameColorNeighbours;
	}
	
	private boolean hasSameColorAs(Bubble compareBubble) {
		return this.getIdVisible() == compareBubble.getIdVisible();
	}
	
	private void areBubblesAroundMe() {
		if (getY() < gg.getNbVertCells()-4 
				&& getNeighbours(2).size() > 0) {
			setActEnabled(false);
			setLocation(setValidLocation(getLocation()));
			this.setLocationOffset(new Point(0,0)); //centers Bubble in Grid
			removeSameColorNeighbours(this);
		}
	}
	
	private void removeSameColorNeighbours(Bubble initialBubble) {
		ArrayList<Bubble> sameColorNeighbours = getSameColorNeighbours(initialBubble);
		if (sameColorNeighbours.size() > 2)
			for (Bubble b: sameColorNeighbours)
				b.removeSelf();
	}
}

/*------------------------------------------------------------------------------
 * Pointer
 */
class Pointer extends Actor {
	public Pointer() {
		super(true, "sprites/pointer.png");
		// this.setLocationOffset(new Point(0,-80));
		// edited picture so it fits
	}

	public void act() {
		Location mousePos = gameGrid.getMouseLocation();
		//SM: could be done preciser
		if (mousePos != null) { // is null if mouse is outside of grid
			double dirToMouse = getLocation().getDirectionTo(mousePos);
			setDirection(dirToMouse);
		}
	}
}