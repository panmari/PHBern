package gui;

import models.IObstacle;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGPanel;

public abstract class AbstractVictim extends Actor {

	protected IObstacle shape;
	protected GGPanel panel;
	protected Dalek dalek;
	protected boolean isDead = false;
	
	public AbstractVictim(Exterminate gg, Dalek d) {
		super();
		this.panel = gg.getPanel();
		this.dalek = d;
	}
	
	public void act() {
		if (shape.liesInside(dalek.getStandPoint())) {
			isDead = true;
			dalek.removeEnemy(shape);
		}
		draw();
	}
	
	protected abstract void draw();
}
