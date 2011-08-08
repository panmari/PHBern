import ch.aplu.jgamegrid.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import ch.aplu.jgamegrid.GGKeyListener;
import ch.aplu.jgamegrid.GGActorCollisionListener;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
//import java.awt.Point;
import java.awt.event.KeyAdapter;

public class Spiel_1 extends GameGrid implements GGActorCollisionListener, GGKeyListener {

	private Black black = new Black();
	private White white = new White();

	public Spiel_1() {
		super(820, 450, 1); // Spielfeld
		Ball green = new Ball();
		addActor(white, new Location(795, 225)); // Startpositionen der Actors
		addActor(black, new Location(35, 225));
		addActor(green, new Location(400, 225));
		black.addCollisionActor(green);
		black.addActorCollisionListener(this);
		white.addCollisionActor(green);
		white.addActorCollisionListener(this);
		addKeyListener(this);
		show();

		int nbPunkte = 0;
		setTitle("Ping-Pong Punkte  " + nbPunkte);
		nbPunkte++;

	}

	public int collide(Actor actor1, Actor actor2) { // Kolisionsverhalten
		actor1.setDirection(actor1.getDirection() + 180);
		actor2.setDirection(actor2.getDirection() + 180);
		return (10);
	}

	public interface BorderListener {
		void bordercrossed(double x, double y);
	}

	public static void main(String[] args) {
		new Spiel_1();
	}

	@Override
	public boolean keyPressed(KeyEvent evt) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyReleased(KeyEvent evt) {
		int keyCode = evt.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			white.setY(white.getY() - 15);
			break;
		case KeyEvent.VK_DOWN:
			white.setY(white.getY() + 15);
			break;
		case KeyEvent.VK_W:
			black.setY(black.getY() - 15);
			break;
		case KeyEvent.VK_S:
			black.setY(black.getY() + 15);
			break;
		}
		return true; // Consume
	}
}

// ----------------------------Actors-------------------------------------------

class Black extends Actor // Eigenschaften des scwarzen
{
	boolean isFinished = false; // Balkesn

	public Black() {
		super("sprites/Balken.gif");
	}
}

class White extends Actor // Eigenschaften des weißen
{
	boolean isFinished = false; // Balkens

	public White() {
		super("sprites/Balken.gif");
	}
}

class Ball extends Actor // Eigenschaften des Balls
{
	public Ball() {
		super("sprites/ball.gif");
	}

	private final double step = 0.1; // Winkel des Balls

	public int collide(Actor Ball, Actor bar) // Kollisionsverhalten
	{
		Ball.setDirection(Ball.getDirection() + 180);
		bar.setDirection(bar.getDirection() + 180);
		return (10);
	}

	public void act() {

		Location loc = getLocation();
		double dir = (getDirection() + step) % 360;

		if (loc.x < 30) // Abprallen: wann? wohin?
		{
			dir = 180 - dir;
			setLocation(new Location(31, loc.y));
			// Ball.delay(1500);
		}
		if (loc.x > 790) {
			// dir = 180 - dir;
			setLocation(new Location(400, 225));
			Ball.delay(1500);
		}
		if (loc.y < 30) {
			dir = 360 - dir;
			setLocation(new Location(loc.x, 31));
		}
		if (loc.y > 420) {
			dir = 360 - dir;
			setLocation(new Location(loc.x, 419));
		}
		setDirection(dir);
		move();

	}
}