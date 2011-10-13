// PearlG2.java

import ch.aplu.jgamegrid.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PearlG2 extends GameGrid implements GGMouseListener,
		GGButtonListener {
	private int nbPearl = 0;
	private int nbTakenPearl;
	private int nbRows = 4;
	private int activeRow;
	private GGBackground bg;
	private GGButton okBtn = new GGButton("sprites/ok.gif", true);
	private GGButton newBtn = new GGButton("sprites/new.gif", true);
	private ComputerPlayer cp;
	private final boolean misereMode = true;

	public PearlG2() {
		super(8, 6, 60, false);
		setBgColor(new Color(80, 15, 247));
		bg = getBg();
		addMouseListener(this, GGMouse.lPress);
		addActor(okBtn, new Location(6, 4));
		okBtn.addButtonListener(this);
		addActor(newBtn, new Location(6, 4));
		newBtn.addButtonListener(this);
		cp = new ComputerPlayer(this, misereMode);
		init();
		show();
	}

	public void init() {
		int nb = 6;
		cp.reset();
		bg.clear();
		for (int k = 0; k < nbRows; k++) {
			for (int i = 0; i < nb; i++) {
				Pearl pearl = new Pearl();
				addActor(pearl, new Location(1 + i, 1 + k));
				cp.updatePearlArrangement(k + 1, +1);
				nbPearl++;
			}
			nb--;
		}
		prepareNextHumanMove(); // human starts
		okBtn.show();
		newBtn.hide();
		refresh();
		setTitle(nbPearl
				+ " Pearls. Remove any number of pearls from same row and press OK.");
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
					gameOver("You win!");
				}
				refresh();
			}
		}
		return true;
	}

	public void gameOver(String msg) {
		setTitle("Press 'new Game' to play again.");
		bg.setPaintColor(Color.red);
		bg.setFont(new Font("Arial", Font.BOLD, 32));
		bg.drawText(msg, new Point(toPoint(new Location(2, 5))));
		okBtn.hide();
		newBtn.show();
		refresh();
	}
	
	public void buttonClicked(GGButton button) {
		if (nbPearl == 0)
			init();
		else {
			if (nbTakenPearl == 0)
				setTitle("You have to remove at least 1 Pearl!");
			else {
				cp.makeMove();
				refresh();
				nbPearl = getNumberOfActors(Pearl.class);
				if (nbPearl == 0) 
					gameOver("You Lost!");
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

	public void reset() {
		
	}

	public static void main(String[] args) {
		new PearlG2();
	}

}

class Pearl extends Actor {
	public Pearl() {
		super("sprites/pearl.gif");
	}
}