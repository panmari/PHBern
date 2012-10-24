package gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import models.Triangle;
import models.ViewingCone;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGPanel;
import ch.aplu.jgamegrid.GGTextField;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;

/**
 * This class represents a Dalek, the evil in its most robotic form.
 * Still, a Dalek only consists of it's viewing cone (and lots of EXTERMINATEEE)
 *
 */
public class Dalek extends Actor {

	private ViewingCone vc;
	private int sightDistance;
	private double sightAngle;
	private GGPanel panel;
	private GGTextField text;

	public Dalek(Exterminate gg, int sightDistance, double sightAngle) {
		this.sightDistance = sightDistance;
		this.sightAngle = sightAngle;
		this.panel = gg.getPanel();
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
		draw();
		GGVector v = vc.getClosestObstacle();
		if (v != null) {
			panel.setPaintColor(Color.black);
			panel.drawCircle(Util.toPoint(v), 5);
		}
		text.setText(v + ": "+ vc.getDistanceToClosestObstacle() );
		
	}

	private void draw() {
		panel.setPaintColor(Color.red);
		GGVector[] vs = vc.getVertices();
		panel.drawArc(Util.toPoint(vs[0]), sightDistance, (getDirection() - sightAngle/2 + 360) % 360, sightAngle);
		panel.drawLine(Util.toPoint(vs[0]), Util.toPoint(vs[1]));
		panel.drawLine(Util.toPoint(vs[0]), Util.toPoint(vs[2]));
	}
	
	public void addEnemy(Triangle t) {
		vc.addObstacle(t);
	}
}
