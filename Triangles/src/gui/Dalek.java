package gui;

import java.awt.Color;

import models.IObstacle;
import models.Triangle;
import models.ViewingCone;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGPanel;
import ch.aplu.jgamegrid.GGTextField;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

/**
 * This class represents a Dalek, the evil in its most robotic form.
 * Still, a Dalek only consists of it's viewing cone and some drawing methods.
 * (and lots of EXTERMINATEEE)
 * </br>
 * When a Dalek doesn't see any enimies, it walks around aimlessly. Once it sees an
 * Enemy, it purchases it merciless. The condition, that a Dalek has to rotate
 * continuously is only a slight hindrance for the mighty race of the Dalek!
 */
public class Dalek extends Actor {

	private ViewingCone vc;
	private int sightDistance;
	private double sightAngle;
	private GGPanel panel;
	private GGTextField text;
	private GGVector walkDirection;
	private TextActor EXTERMINATE;
	
	/**
	 * @param gg
	 * @param sightDistance
	 * @param sightAngle
	 */
	public Dalek(Exterminate gg, int sightDistance, double sightAngle) {
		this.sightDistance = sightDistance;
		this.sightAngle = sightAngle;
		this.panel = gg.getPanel();
		setRandomWalkDirection();
		EXTERMINATE = new TextActor("EXTERMINATE!!!");
		gg.addActor(EXTERMINATE, new Location(300, 10));
		text = new GGTextField(gg, new Location(10, 10), true);
		text.show();
	}
	
	private void setRandomWalkDirection() {
		this.walkDirection = new GGVector(0,2); //set to null vector if you want it to stand still
		walkDirection.rotate(Math.random()*360);
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
		//walkDirection = new GGVector(0,0); //UNCOMMENT to make it stand still
		lap = lap.add(vc.getStandPoint()).add(walkDirection);
		vc.setStandPoint(vc.getStandPoint().add(walkDirection));
		vc.setLookAtPoint(lap);
		walkDirection.rotate(Math.random()*10 - 5);
		
		analyzeSurroundings();
		draw();
	}

	/**
	 * Analyzes the objects in the viewing cone if there
	 * are any living victimTriangles, it will be hunted down!
	 */
	private void analyzeSurroundings() {
		GGVector v = vc.getClosestObstacle();
		if (v != null) {
			panel.setPaintColor(Color.black);
			panel.drawCircle(Util.toPoint(v), 3);
			chaseVictim(v);
			EXTERMINATE.show();
		} else  {
			setRandomWalkDirection();
			EXTERMINATE.hide();
		}
		text.setText(v + ": "+ vc.getDistanceToClosestObstacle() );
	}

	private void draw() {
		panel.setPaintColor(Color.red);
		GGVector[] vs = vc.getVertices();
		panel.drawCircle(Util.toPoint(vs[0]), 2);
		panel.drawArc(Util.toPoint(vs[0]), sightDistance, (getDirection() - sightAngle/2 + 360) % 360, sightAngle);
		panel.drawLine(Util.toPoint(vs[0]), Util.toPoint(vs[1]));
		panel.drawLine(Util.toPoint(vs[0]), Util.toPoint(vs[2]));
	}
	
	private void chaseVictim(GGVector v) {
		GGVector dir = v.sub(vc.getStandPoint());
		dir.normalize();
		dir = dir.mult(2);
		walkDirection = dir;
	}

	public void addEnemy(Triangle t) {
		vc.addObstacle(t);
	}
	
	public Location getALocation() {
		return Util.toLocation(vc.getStandPoint());
	}

	public GGVector getStandPoint() {
		return vc.getStandPoint();
	}

	public boolean removeEnemy(IObstacle t) {
		return vc.removeObstacle(t);
	}
}
