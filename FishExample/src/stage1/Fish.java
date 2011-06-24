package stage1;

import ch.aplu.jgamegrid.Actor;

class Fish extends Actor {

	FishTrap trap;

	public Fish(FishTrap trap) {
		super(true, "sprites/fish.gif");
		this.trap = trap;
	}

	public void act() {
		if (isMoveValid())
			moveRandomly();
		else
			avoidWall();
	}

	private void avoidWall() {
		setDirection(getDirection() + 180);
		move();
	}

	private void moveRandomly() {
		double angle = (Math.random() * 20) - 10;
		turn(angle);
		move();
	}
}