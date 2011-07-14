package gorillaStef;

import ch.aplu.jgamegrid.Actor;

public class Banana extends Actor {
	
	public Banana() {
		super(true, "sprites/banana_0.png");
	}
	
	public void act() {
		turn(20);
	}
}
