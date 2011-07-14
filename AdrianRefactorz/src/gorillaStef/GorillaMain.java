package gorillaStef;

import javax.swing.JOptionPane;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class GorillaMain extends GameGrid {

	WindSock ws = new WindSock();
	Location[] gorillaLoc = {new Location(35,565), new Location(1165,565)};
	Location[] bananaLunchLoc = {new Location(70, 535), new Location(1130, 535)};
	
	public GorillaMain() {
		super(1200, 600, 1, null, "sprites/townBig.jpg", true);
		setSimulationPeriod(30);
		addActor(ws, new Location(nbHorzCells/2, 40));
		ws.setRandomWind();
		addActor(new Banana(50, -100), bananaLunchLoc[0]);
		for (Location loc: gorillaLoc)
			addActor(new Gorilla(), loc);
		show();
		//doRun();
	}
	
	public void act() {
		if (getNbCycles() % 100 == 0)
			ws.setRandomWind();
	}

	private double requestNumber(String prompt) {
		double number = 0;
		Boolean invalidEntry = true;

		while (invalidEntry) {
			try {
				String entry = JOptionPane.showInputDialog(null, prompt, "",
						JOptionPane.PLAIN_MESSAGE);
				number = Double.parseDouble(entry);
				invalidEntry = false;
			} catch (NumberFormatException e) {
				System.out.println("Entry was not valid!");
			}
		}
		return number;
	}
	
	public static void main(String[] args) {
		new GorillaMain();
	}
}
