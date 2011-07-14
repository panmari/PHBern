package gorillaStef;

import ch.aplu.jgamegrid.Actor;

public class WindSock extends Actor {
	
	private static int nrWindStates = 6;
	private int currentWind;
	
	public WindSock() {
		super("sprites/windsock.png", nrWindStates);
	}
	
	public void setRandomWind() {
		//get a number between -5 and 5:
		currentWind = (int) (Math.random()*((nrWindStates-1)*2)) - nrWindStates+1;
		if (currentWind > 0) {
			setHorzMirror(false);
			show(currentWind);
		} else {
			setHorzMirror(true);
			show(-currentWind);
		}
	}
	
	public int getWind() {
		return currentWind;
	}
}
