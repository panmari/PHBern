package halmaSquareSkeleton;

import ch.aplu.jgamegrid.Actor;

/*
 * TASK: this stuff is kinda tricky, not easy to programm...
 * I wouldn't recommend doing stuff here...
 */
class HalmaStone extends Actor {
	public HalmaPlayer player;
	private HalmaColor hc;
	private boolean pickedUp;

	public HalmaStone(HalmaPlayer player) {
		super("sprites/halmaStone.png", 6);
		show(player.getColor().ordinal() * 2);
		this.player = player;
	}

	public HalmaPlayer getOwningPlayer() {
		return player;
	}

	/**
	 * Same as pickUp()
	 */
	public void putDown() {
		assert pickedUp;
		pickedUp = false;
		this.showPreviousSprite();
	}

	/**
	 * Funny color changes could occur here, if a 
	 * already picked Up stone gets picked up again! 
	 * Make sure this is ONLY called on NOT pickedUp stones!
	 */
	public void pickUp() {
		assert !pickedUp;
		pickedUp = true;
		this.showNextSprite();
	}

	public boolean isPickedUp() {
		return pickedUp;
	}
	public HalmaColor getColor() {
		return hc;
	}
}