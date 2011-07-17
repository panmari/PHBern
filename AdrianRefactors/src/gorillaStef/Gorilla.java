package gorillaStef;

import java.awt.Point;

import javax.swing.JOptionPane;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class Gorilla extends Actor{

	private Location lunchLoc;
	private GorillaMain gg;
	private int ownPlayerNb;
	
	public Gorilla(GorillaMain gg, Location bananaLunchLoc, int playerNb) {
		 super("sprites/gorilla.png", 2);
		 this.lunchLoc = bananaLunchLoc;
		 this.gg = gg;
		 this.ownPlayerNb = playerNb;
		 setCollisionCircle(new Point(0,0), 29);
	}
	
	public void reset() {
		 if (isRightGorilla())
			 setHorzMirror(true);
	}
	
	public void lunchBanana() {
		gg.setGorillaTitle(isRightGorilla());
		show(1); //show throwing sprite
		double angle = 0, speed = 0;
		while (angle <= 0 || angle >= Math.PI/2)
			angle = Math.toRadians(requestNumber("Angle (between 0 and 90): "));
		while (speed <= 0 || speed >= 200)
			speed = requestNumber("Speed (between 0 and 200): ");
		double vx = Math.cos(angle)*speed;
		double vy = Math.sin(angle)*speed;
		if (isRightGorilla())
			vx = -vx;
		Banana b = new Banana(gg, vx, -vy);
		b.addActorCollisionListener(b);
		b.addCollisionActor(gg.getEnemyGorilla(ownPlayerNb));
		gg.addActor(b, lunchLoc);
		show(0);
	}
	
	private boolean isRightGorilla() {
		return ownPlayerNb == 1;
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
