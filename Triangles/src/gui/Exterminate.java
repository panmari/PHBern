package gui;

import java.awt.Color;
import java.awt.event.KeyEvent;

import ch.aplu.jgamegrid.GGKeyListener;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class Exterminate extends GameGrid implements GGMouseListener, GGKeyListener {

	private Dalek dalek;
	private AbstractVictim vTriangle, vCircle;
	public Exterminate() {
		super(500, 500, 1, false);
		this.setBgColor(Color.white);
		dalek = new Dalek(this, 100, 90);
		addActor(dalek, new Location(200, 200));
		addActor(new Cleaner(getPanel()), new Location(-10, -10));
		show();
		this.addMouseListener(this, GGMouse.lPress | GGMouse.rPress);
		this.addKeyListener(this);
		makeVictims();
		setSimulationPeriod(30);
		doRun();
	}

	
	private void makeVictims() {
		vTriangle = new VictimTriangle(this, dalek);
		addActor(vTriangle, new Location(-20, -20));
		vCircle = new VictimCircle(this, dalek);
		addActor(vCircle, new Location(-20, -20));
		setActOrder(Cleaner.class);
	}


	public static void main(String[] args) {
		new Exterminate();
	}


	@Override
	public boolean mouseEvent(GGMouse mouse) {
		switch (mouse.getEvent()) {
		case GGMouse.lPress:
			vTriangle.setLocation(new Location(mouse.getX(), mouse.getY()));
			break;
		case GGMouse.rPress:
			vCircle.setLocation(new Location(mouse.getX(), mouse.getY()));
			break;
		}
		return true;
	}


	@Override
	public boolean keyPressed(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_DELETE:
			//TODO: remove all actors?
			break;
		}
		return false;
	}


	@Override
	public boolean keyReleased(KeyEvent evt) {
		// TODO Auto-generated method stub
		return false;
	}
}
