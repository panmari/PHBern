// PearlG2.java

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class PearlVsComputer extends GameGrid implements GGMouseListener,
		GGButtonListener {
	private int nbPearl = 0;
	private int nbTakenPearl;
	private int nbRows = nbVertCells - 2;
	private int activeRow;
	private GGBackground bg;
	private GGButton okBtn = new GGButton("sprites/ok.gif", true);
	private GGButton newBtn = new GGButton("sprites/new.gif", true);
	private ComputerPlayer cp;
	private final boolean misereMode = true;

	public PearlVsComputer() {
		//size decides how many pearls are placed
		super(8, 7, 60, false);
		setBgColor(new Color(80, 15, 247));
		bg = getBg();
		addMouseListener(this, GGMouse.lPress);
		addActor(okBtn, new Location(nbHorzCells - 1, nbVertCells - 2));
		okBtn.addButtonListener(this);
		addActor(newBtn, new Location(nbHorzCells - 1, nbVertCells - 1));
		newBtn.addButtonListener(this);
		cp = new ComputerPlayer(this, misereMode);
		init();
		show();
	}

	public void init() {
		nbPearl = 0;
		removeActors(Pearl.class);
		cp.reset();
		bg.clear();
		makeRandomLevel();
		prepareNextHumanMove(); // human starts
		okBtn.show();
		refresh();
		setTitle(nbPearl
				+ " Pearls. Remove any number of pearls from same row and press OK.");
	}
	
	private void makeRandomLevel() {
		for (int k = 0; k < nbRows; k++) {
			int nb = (int) (Math.random()*(nbHorzCells-2)) + 1;
			for (int i = 0; i < nb; i++) {
				Pearl pearl = new Pearl();
				addActor(pearl, new Location(1 + i, 1 + k));
				cp.updatePearlArrangement(k + 1, +1);
				nbPearl++;
			}
		}
	}

	private void makeStandardLevel() {
		int nb = nbHorzCells - 2;
		for (int k = 0; k < nbRows; k++) {
			for (int i = 0; i < nb; i++) {
				Pearl pearl = new Pearl();
				addActor(pearl, new Location(1 + i, 1 + k));
				cp.updatePearlArrangement(k + 1, +1);
				nbPearl++;
			}
			nb--;
		}
	}
	public boolean mouseEvent(GGMouse mouse) {
		Location loc = toLocationInGrid(mouse.getX(), mouse.getY());
		Actor pearlAtClick = getOneActorAt(new Location(loc), Pearl.class);
		if (pearlAtClick != null) {
			int y = pearlAtClick.getY();

			if (activeRow != 0 && activeRow != y)
				setTitle("You must remove pearls from the same row.");
			else {
				activeRow = y;
				pearlAtClick.removeSelf();
				nbPearl--;
				setTitle(nbPearl + " Pearls remaining. Click 'OK' to continue.");
				nbTakenPearl++;
				cp.updatePearlArrangement(y, -1);
				if (nbPearl == 0) {
					if (misereMode)
						gameOver("You lost!");
					else gameOver("You won!");
				}
				refresh();
			}
		}
		return true;
	}

	public void gameOver(String msg) {
		setTitle("Press 'new Game' to play again.");
		bg.setPaintColor(Color.yellow);
		bg.setFont(new Font("Arial", Font.BOLD, 32));
		bg.drawText(msg, new Point(toPoint(new Location(2, 5))));
		okBtn.hide();
		refresh();
	}
	
	public void buttonClicked(GGButton button) {
		if (button == newBtn)
			init();
		else {
			if (nbTakenPearl == 0)
				setTitle("You have to remove at least 1 Pearl!");
			else {
				cp.makeMove();
				refresh();
				nbPearl = getNumberOfActors(Pearl.class);
				if (nbPearl == 0) {
					if (misereMode)
						gameOver("You won!");
					else gameOver("You lost!");
				}
				else prepareNextHumanMove();
			}
		}
	}

	private void prepareNextHumanMove() {
		nbTakenPearl = 0;
		setTitle(nbPearl + " pearls remaining. Your move now.");
		activeRow = 0; // Spieler darf neue "Ziehreihe" bestimmen		
	}

	public void buttonPressed(GGButton button) {
	}

	public void buttonReleased(GGButton button) {
	}

	public static void main(String[] args) {
		new PearlVsComputer();
	}

}

class Pearl extends Actor {
	public Pearl() {
		super("sprites/pearl.gif");
	}
}