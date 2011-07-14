package gorillaStef;

import javax.swing.JOptionPane;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class Gorilla extends Actor{

	private Location lunchLoc;
	private GorillaMain gg;
	
	public Gorilla(GorillaMain gg, Location bananaLunchLoc) {
		 super("sprites/gorilla.png", 2);
		 this.lunchLoc = bananaLunchLoc;
		 this.gg = gg;
	}
	
	public void reset() {
		 if (isRightGorilla())
			 setHorzMirror(true);
	}
	
	public void lunchBanana() {
		show(1);
		double angle = Math.toRadians(requestNumber("Angle: "));
		double speed = requestNumber("Speed: ");
		double vx = Math.cos(angle)*speed;
		double vy = Math.sin(angle)*speed;
		if (isRightGorilla()) //we're left gorilla
			vx = -vx;
		gg.addActor(new Banana(vx, -vy), lunchLoc);
		show(0);
	}
	
	private boolean isRightGorilla() {
		return getX() > gg.nbHorzCells/2;
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
			} catch (NullPointerException e) {
				gg.setTitle("Quiting Game");
				delay(2000);
				System.exit(0);
			}
		}
		return number;
	}
}
