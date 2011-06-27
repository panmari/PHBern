package stage1.copy;


import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

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
			if (moveIsATrap()) {
				tryToAvoidTrap();
			}
			
			turnRandomly();
		}
		else avoidWall();
		move();
	}

	private void tryToAvoidTrap() {
		turn(180);
	}

	private void avoidWall() {
		turn(180);
	}

	private void turnRandomly() {
		double angle = (Math.random() * 20) - 10;
		turn(angle);
	}
	
	private boolean moveIsATrap() {
		int trapRadius = trap.getTrapRadius();
		Location nextMoveLocation = this.getNextMoveLocation();
		if (trap.getNeighbours(trapRadius).size()>0
			&& trap.getNeighbours(trapRadius).get(0).equals(this)) {
			return true;
		}
		return false;
	}
}