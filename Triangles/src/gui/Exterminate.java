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
		dalek = new Dalek(this, 100, 90);
		addActor(dalek, new Location(200, 200));
		//this.getBg().clear(Color.white);
		show();
		this.addMouseListener(this, GGMouse.lPress);
		reset();
		doRun();
	}

	
	public static void main(String[] args) {
		new Exterminate();
	}


	@Override
	public boolean mouseEvent(GGMouse mouse) {
		VictimTriangle vt = new VictimTriangle(this, dalek);
		addActor(vt, new Location (mouse.getX(), mouse.getY()));
		return true;
	}
	
	public void reset() {
		getPanel().clear(Color.white);
	}
}
