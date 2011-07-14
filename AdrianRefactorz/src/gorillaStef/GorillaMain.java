package gorillaStef;

import javax.swing.JOptionPane;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class GorillaMain extends GameGrid {

	WindSock ws = new WindSock();
	
	public GorillaMain() {
		super(1200, 600, 1, null, "sprites/townBig.jpg", true);
		setSimulationPeriod(30);
		addActor(ws, new Location(600, 40));
		show();
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
}
