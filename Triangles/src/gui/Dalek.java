package gui;

import java.awt.Color;

import models.Triangle;
import models.ViewingCone;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGPanel;
import ch.aplu.jgamegrid.GGTextField;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;

/**
 * This class represents a Dalek, the evil in its most robotic form.
 * Still, a Dalek only consists of it's viewing cone and some drawing methods.
 * (and lots of EXTERMINATEEE)
 * 
 */
public class Dalek extends Actor {

	private ViewingCone vc;
	private int sightDistance;
	private double sightAngle;
	private GGPanel panel;
	private GGTextField text;
	private GGVector walkDirection;
	
	public Dalek(Exterminate gg, int sightDistance, double sightAngle) {
		this.sightDistance = sightDistance;
		this.sightAngle = sightAngle;
		this.panel = gg.getPanel();
		this.walkDirection = new GGVector(0,2); //set to null vector if you want it to stand still
		walkDirection.rotate(Math.random()*360);
		text = new GGTextField(gg, new Location(10, 10), true);
		text.show();
	}
	
	public void reset() {
		GGVector standPoint = Util.toVector(getLocation());
		GGVector lookAtPoint = new GGVector(sightDistance, 0);
		lookAtPoint.rotate(Math.toRadians(getDirection()));
		lookAtPoint = lookAtPoint.add(standPoint);
		vc = new ViewingCone(standPoint, lookAtPoint, Math.toRadians(sightAngle));
	}
	
	public void act() {
		GGVector lap = vc.getLookAtPoint();
		lap = lap.sub(vc.getStandPoint());
		lap.rotate(Math.PI/30);
		double dir = Math.toDegrees(Math.acos(new GGVector(1,0).dot(lap)/lap.magnitude()));
		if (lap.y > 0)
			dir = 360 - dir;
		setDirection(dir); //for drawing later on
		lap = lap.add(vc.getStandPoint()).add(walkDirection);
		vc.setStandPoint(vc.getStandPoint().add(walkDirection));
		vc.setLookAtPoint(lap);
		walkDirection.rotate(Math.random()*10 - 5);
		draw();
	}

	private void draw() {
		panel.setPaintColor(Color.red);
		GGVector[] vs = vc.getVertices();
		panel.drawCircle(Util.toPoint(vs[0]), 2);
		panel.drawArc(Util.toPoint(vs[0]), sightDistance, (getDirection() - sightAngle/2 + 360) % 360, sightAngle);
		panel.drawLine(Util.toPoint(vs[0]), Util.toPoint(vs[1]));
		panel.drawLine(Util.toPoint(vs[0]), Util.toPoint(vs[2]));
		
		GGVector v = vc.getClosestObstacle();
		if (v != null) {
			panel.setPaintColor(Color.black);
			panel.drawCircle(Util.toPoint(v), 3);
		}
		text.setText(v + ": "+ vc.getDistanceToClosestObstacle() );
	}
	
	public void addEnemy(Triangle t) {
		vc.addObstacle(t);
	}
}
