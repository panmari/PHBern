package stage1;

import ch.aplu.jgamegrid.Actor;

class Fish extends Actor {

	private FishTrap trap;
	private FishPond pond;

	public Fish(FishPond pond, FishTrap trap) {
		super(true, "sprites/fish.gif");
		this.trap = trap;
		this.pond = pond;
	}

	public void act() {
		if (isMoveValid()) {
			tryToAvoidTrap();
			turnRandomly();
		}
		else avoidWall();
		move();
	}

	private void tryToAvoidTrap() {
		//TODO: Teach the fish how to avoid the trap
	}

	private void avoidWall() {
		turn(180);
	}

	private void turnRandomly() {
		double angle = (Math.random() * 20) - 10;
		turn(angle);
	}
}