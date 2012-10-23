package gui;

import java.awt.Color;

import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class Exterminate extends GameGrid implements GGMouseListener {

	private Dalek dalek;

	public Exterminate() {
		super(500, 500, 1);
		dalek = new Dalek(this.getPanel());
		addActor(dalek, new Location(200, 200));
		this.getBg().clear(Color.white);
		show();
		dalek.act();
		this.addMouseListener(this, GGMouse.lPress);
	}

	
	public static void main(String[] args) {
		new Exterminate();
	}


	@Override
	public boolean mouseEvent(GGMouse mouse) {
		VictimTriangle vt = new VictimTriangle(getPanel(), dalek);
		addActor(vt, new Location (mouse.getX(), mouse.getY()));
		return true;
	}
}
