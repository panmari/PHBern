package gui;

import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class Exterminate extends GameGrid implements GGMouseListener {

	private Dalek dalek;
	private VictimTriangle vt;
	public Exterminate() {
		super(500, 500, 1, false);
		dalek = new Dalek(this, 100, 90);
		vt = new VictimTriangle(this, dalek);
		addActor(dalek, new Location(200, 200));
		addActor(new Cleaner(getPanel()), new Location(-10, -10));
		show();
		this.addMouseListener(this, GGMouse.lPress | GGMouse.rPress);
		addActor(vt, new Location (-20, -20));
		reset();
		setSimulationPeriod(30);
		setActOrder(Cleaner.class);
		doRun();
	}

	
	public static void main(String[] args) {
		new Exterminate();
	}


	@Override
	public boolean mouseEvent(GGMouse mouse) {
		switch (mouse.getEvent()) {
		case GGMouse.lPress:
			vt.setLocation(new Location(mouse.getX(), mouse.getY()));
			break;
		case GGMouse.rPress:
			vt = new VictimTriangle(this, dalek);
			addActor(vt, new Location(mouse.getX(), mouse.getY()));
			setActOrder(Cleaner.class);
			break;
		}
		return true;
	}
}
