package bubbleShooter;

import ch.aplu.jgamegrid.*;
import java.awt.Point;
import java.util.*;

public class BubbleShooter extends GameGrid implements GGMouseListener {
	int nbOfBubbleColors = 5; // Zahl zwischen 1 - 5
	Location shootLoc = new Location(18, 36);
	ArrayList<Location> bubblePreviewLocations = new ArrayList<Location>();
	private boolean previousBubbleArrived = true;

	public BubbleShooter() {
		super(37, 38, 20, false);
		addMouseListener(this, GGMouse.lPress);
		initializeActors();
		show();
		setSimulationPeriod(30); // => 33 refreshes per second
		doRun();
	}

	private void initializeActors() {
		bubblePreviewLocations.add(shootLoc);
		for (int x = 27; x < 37; x += 2)
			bubblePreviewLocations.add(new Location(x, 36));
		for (Location loc : bubblePreviewLocations)
			addRandomBubble(loc);
		// initialize Bubblefield:
		for (int y = 1; y < 6; y++) {
			for (int x = y; x < 37 - y; x += 2)
				addRandomBubble(new Location(x, 2 * y - 1));
		}
		addActor(new Pointer(), shootLoc);
		setPaintOrder(Bubble.class, Pointer.class);
	}

	public boolean mouseEvent(GGMouse mouse) {
		if (previousBubbleArrived) {
			Bubble shootBubble = (Bubble) getOneActorAt(shootLoc, Bubble.class);
			shootBubble.addCollisionActors(getFieldBubbles());
			shootBubble.addActorCollisionListener(shootBubble);
			shootBubble.shoot(new Point(mouse.getX(), mouse.getY()));
			reloadPreviewBubbles();
			checkForGameOver();
			previousBubbleArrived = false;
		}
		return true;
	}
	
	public void setReadyForNextBubble() {
		previousBubbleArrived = true;
	}

	private ArrayList<Actor> getFieldBubbles() {
		ArrayList<Actor> fieldBubbles = getActors(Bubble.class);
		for (Location loc : bubblePreviewLocations)
			fieldBubbles.remove(getOneActorAt(loc, Bubble.class));
		return fieldBubbles;
	}

	private void addRandomBubble(Location loc) {
		int rN = (int) (Math.random() * nbOfBubbleColors);
		addActor(new Bubble(rN, this), loc);
	}

	/**
	 * Reloads the shooter, advances the preview bubbles &
	 * adds a new preview bubble at the end of the preview list.
	 */
	private void reloadPreviewBubbles() {
		for (int b = 1; b < bubblePreviewLocations.size(); b++)
			getOneActorAt(bubblePreviewLocations.get(b)).setLocation(
					bubblePreviewLocations.get(b - 1));
		addRandomBubble(bubblePreviewLocations.get(bubblePreviewLocations.size() - 1));
	}

	/**
	 * The game is over if only the shooter & the preview Bubbles are left.
	 */
	private void checkForGameOver() {
		if (getNumberOfActors() <= 6) {
			addActor(new Actor("sprites/gameover.png"), new Location(18, 15));
			stopGameThread();
		}
	}

	public static void main(String[] args) {
		new BubbleShooter();
	}
}

/*------------------------------------------------------------------------------
 * Bubble
 */
class Bubble extends Actor {
	private BubbleShooter gg;
	private double vy, vx, x, y;
	private final double acceleration = 10, bubbleRadius = 20;

	public Bubble(int imgId, BubbleShooter gg) {
		super("sprites/peg.png", 6);
		this.gg = gg;
		show(imgId);
		setActEnabled(false);
		setCollisionCircle(new Point(0, 0), (int) bubbleRadius);
	}

	public void shoot(Point mousePos) {
		x = getPixelLocation().x;
		y = getPixelLocation().y;
		int vx = mousePos.x - getPixelLocation().x;
		int vy = mousePos.y - getPixelLocation().y;
		double vectorLength = getPixelLocation().distance(mousePos);
		this.vx = vx / vectorLength;
		this.vy = vy / vectorLength;
		setActEnabled(true);
	}

	/**
	 * Handles the arriving of the bubble at the bubblefield.
	 */
	@Override
	public int collide(Actor shootActor, Actor hitActor) {
		setActEnabled(false);
		setLocation(setOnValidLocation(shootActor.getLocation()));
		addActorCollisionListener(null);
		this.setLocationOffset(new Point(0, 0)); // centers sprite
		removeSameColorNeighbours(this);
		gg.setReadyForNextBubble();
		return 100;
	}

	@Override
	public void act() {
		// collision with wall:
		if (x - bubbleRadius < 0 || x + bubbleRadius > gg.getPgWidth())
			vx = -vx;
		else if (y - bubbleRadius < 0 || y + bubbleRadius > gg.getPgHeight())
			vy = -vy;
		// movement:
		x = vx * acceleration + x;
		y = vy * acceleration + y;
		setPixelLocation(new Point((int) x, (int) y));
	}
	
	/**
	 * Transforms the given Location into a valid location of the 
	 * Bubble-Grid. Means: an even row is not allowed, and 
	 * every second row starting with 1 changes behaviour on the column-grid
	 * To see more: Change the GameGrid Constructor so it shows the grid.
	 */
	private Location setOnValidLocation(Location loc) {
		if (loc.y % 2 == 0)
			return setOnValidLocation(new Location(loc.x, loc.y + 1));
		else if ((loc.y % 4 == 1 && loc.x % 2 == 1)
				|| (loc.y % 4 == 3 && loc.x % 2 == 0))
			return loc;
		else return fixX(loc, x);
	}

	private Location fixX(Location loc, double x) {
		int fix;
		if ((x / gg.cellSize )% 1 > 0.5)
			fix = 1;
		else fix = -1;
		return new Location(loc.x + fix, loc.y);
	}
	
	public ArrayList<Bubble> getSameColorNeighbours(Bubble initialBubble) {
		ArrayList<Bubble> sameColorNeighbours = new ArrayList<Bubble>();
		Stack<Bubble> uncheckedNeighbours = new Stack<Bubble>();
		uncheckedNeighbours.push(initialBubble);
		while (!uncheckedNeighbours.isEmpty()) {
			Bubble checkBubble = uncheckedNeighbours.pop();
			for (Actor a : checkBubble.getNeighbours(2, Bubble.class)) {
				Bubble b = (Bubble) a;
				if (b.hasSameColorAs(initialBubble)
						&& !uncheckedNeighbours.contains(b)
						&& !sameColorNeighbours.contains(b))
					uncheckedNeighbours.push(b);
			}
			sameColorNeighbours.add(checkBubble);
		}
		return sameColorNeighbours;
	}

	private boolean hasSameColorAs(Bubble compareBubble) {
		return this.getIdVisible() == compareBubble.getIdVisible();
	}

	private void removeSameColorNeighbours(Bubble initialBubble) {
		ArrayList<Bubble> sameColorNeighbours = getSameColorNeighbours(initialBubble);
		if (sameColorNeighbours.size() > 2)
			for (Bubble b : sameColorNeighbours)
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
		// edited picture so it fits, so the line above
		// isn't necessary anymore.
	}

	public void act() {
		Location mousePos = gameGrid.getMouseLocation();
		if (mousePos != null) { // is null if mouse is outside of grid
			double dirToMouse = getLocation().getDirectionTo(mousePos);
			setDirection(dirToMouse);
		}
	}
}