package gui;

import java.awt.Color;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGPanel;

public class Cleaner extends Actor {

	private GGPanel panel;

	public Cleaner(GGPanel p) {
		super();
		this.panel = p;
	}
	
	public void act() {
		panel.clear(Color.white);
	}
}
