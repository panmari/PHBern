// TcpPearl_5.java

import ch.aplu.tcp.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;
import java.awt.*;
import javax.swing.JOptionPane;

public class PearlCompuer extends GameGrid implements GGMouseListener {

	private final static int size = 10;
	private int activeRow;
	private int nbPearl = 0;
	private int nbTakenPearl;
	private ComputerPlayer cp;
	
	public PearlCompuer() {
		super(size, size, 60, false);
		setBgColor(new Color(80, 15, 247));
		setTitle("Remove any number of pearls from same row and right click if finish");
		addStatusBar(30);
		show();
		cp = new ComputerPlayer(this, true);
		init();
	}

	public void init() {
		int nb = size;
		for (int k = 0; k < size; k++) {
			for (int i = 0; i < nb; i++) {
				Actor pearl = new Actor("sprites/pearl.gif");
				addActor(pearl, new Location(i, k));
				addMouseListener(this, GGMouse.lPress | GGMouse.rPress);
				nbPearl++;
			}
			nb--;
		}
		System.out.println("nbPearl = " + nbPearl);
		activeRow = -1;
		nbTakenPearl = 0;
		refresh();
		setMouseEnabled(true);
	}

	public boolean mouseEvent(GGMouse mouse) {
		if (mouse.getEvent() == GGMouse.lPress) {
			Location loc = toLocationInGrid(mouse.getX(), mouse.getY());
			int y = loc.y;
			if (activeRow != -1 && activeRow != y)
				setStatusText("You must remove pearls from the same row");
			else {
				Actor actor = getOneActorAt(loc);
				if (actor != null) {
					actor.removeSelf();
					nbPearl--;
					activeRow = y;
					nbTakenPearl++;
					System.out.println("nbPearl " + nbPearl);
					if (nbPearl == 0) {
						setStatusText("You lost!");
					}
				}
			}
			refresh();
		}
		if (mouse.getEvent() == GGMouse.rPress) {
			if (nbTakenPearl == 0)
				setStatusText("You must remove at least 1 pearl.");
			else {
				setStatusText("Wait.");
				activeRow = -1;
				nbTakenPearl = 0;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		new PearlCompuer();
	}
}
